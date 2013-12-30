package com.ordrin.api_android;

import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ordrin.api_android.ApiConstants.RequestType;
import com.ordrin.api_android.ApiConstants.Servers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

public class ApiHelper{

	private final String TAG = this.getClass().getSimpleName();

	private String api_key;
	private Context context;
	private HashMap<String, String> urls = new HashMap<String, String>();

	private JSONObject schemas;

	public ApiHelper(Context context, String api_key, Servers servers){
		this.context = context;
		this.api_key = api_key;
		schemas = readSchemas();
		Log.i(TAG, "parsed schemas");

		if (servers == Servers.PRODUCTION){
			urls.put("restaurant", "r.ordr.in");
			urls.put("user", "u.ordr.in");
			urls.put("order", "o.ordr.in");
		} else {
			urls.put("restaurant", "r-test.ordr.in");
			urls.put("user", "u-test.ordr.in");
			urls.put("order", "o-test.ordr.in");
		}
	}


	public ApiHelper(Context context, String api_key){
		this(context, api_key, Servers.TEST);
	}


	public JSONObject callEndpoint(String api, String endpoint, HashMap<String, String> params)
			throws MalformedURLException, IOException, JSONException{
		JSONObject schema = schemas.getJSONObject(api).getJSONObject(endpoint);

		String base 	  = urls.get(api);
		JSONObject meta = schema.getJSONObject("meta");


		RequestType request = RequestType.valueOf(meta.getString("method"));
		if (meta.getBoolean("userAuth")){
			Log.i(TAG, "Doing authed request");
			return doAuthenticatedRequest(base, meta.getString("uri"), schema, null, params, request);
		} else {
			// build the path
			ArrayList<String> urlParams = new ArrayList<String>();
			String path	  				= buildPath(meta.getString("uri"), params, urlParams);
			for (int i = 0; i < urlParams.size(); i++){
				params.remove(urlParams.get(i));
			}
			return doApiRequest(base, path, schema, null, params, request);
		}
	}
	

	/*
	 * Adds the X-NAAMA-AUTHENTICATION header and calls doApiRequest
	 */
	public JSONObject doAuthenticatedRequest(String base, String pathSpec, JSONObject schema,
			HashMap<String, String> headers,
			HashMap<String, String> params, RequestType method)
					throws MalformedURLException, IOException, JSONException{
		if (headers == null){
			headers = new HashMap<String, String>();
		}
		
		// build the path
		ArrayList<String> urlParams = new ArrayList<String>();
		String path	  				= buildPath(pathSpec, params, urlParams);
		

		headers.put("X-NAAMA-AUTHENTICATION", String.format(
				"username=\"%s\", response=\"%s\", version=\"1\"", params.get("email"),
				shaString(params.get("current_password") + params.get("email") + path)));
	
		// remove url params from request body
		for (int i = 0; i < urlParams.size(); i++){
			params.remove(urlParams.get(i));
		}
		
		params.remove("email");
		params.remove("current_password");
		return doApiRequest(base, path, schema, headers, params, method);
	}

	/*
	 * Adds the X-NAAMA-CLIENT-AUTHENTICATION header and calls doRequest
	 */
	public JSONObject doApiRequest(String base, String path, JSONObject schema, HashMap<String, String> headers, 
			HashMap<String, String> params, RequestType method)
					throws MalformedURLException, IOException, JSONException{

		if (headers == null){
			headers = new HashMap<String, String>();
		}

		headers.put("X-NAAMA-CLIENT-AUTHENTICATION", String.format(
				"id=\"%s\", version=\"1\"", api_key));
		return doRequest(base, path, headers, params, method);
	}

	// performs a request to the ordr.in api
	public JSONObject doRequest(String base, String path, HashMap<String, String> headers,
			HashMap<String, String> params, RequestType method)
					throws MalformedURLException, IOException, JSONException{
		if (headers == null){ // make sure we have a headers object
			headers = new HashMap<String, String>();
		}
		
		Log.i(TAG, "Request type " + method.name());


		String charset = "UTF-8";
		String url;
		Uri uri = buildUri(base, path, params);

		if (method == RequestType.GET){
			url = uri.toString();
		} else {
			Uri.Builder builder = new Uri.Builder();
			builder.scheme("https").authority(base).path(path);
			url = builder.build().toString();
			
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			headers.put("Content-Length", Integer.toString(uri.getEncodedQuery().length()));
		}
		
		HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
		connection.setUseCaches(false);
		Log.i(TAG, "Setting method " + method.toString());
		connection.setRequestMethod(method.toString());

		// add in the headers
		Iterator<Map.Entry<String, String>> headerIterator = headers.entrySet().iterator();
		while(headerIterator.hasNext()){
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) headerIterator.next();
			Log.i(TAG, "Adding header " + pairs.getKey() + " " + pairs.getValue());
			connection.addRequestProperty(pairs.getKey(), pairs.getValue());
		}

		connection.setRequestProperty("Accept-Charset", charset);
		// write the post data if necessary
		Log.i(TAG, "Writing post data " + uri.getEncodedQuery());
		if (method != RequestType.GET){
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(uri.getEncodedQuery());
			wr.flush();
			wr.close();
		}
		InputStream response;
		try{
			response = connection.getInputStream();
		} catch (IOException e){
			// this is misleading. It could be a 404, but it could also be a 400 or a 401. Clarify it
			int status = ((HttpURLConnection) connection).getResponseCode();
			Log.i(TAG, "Got the following response: " +
					getStringFromInputStream(connection.getErrorStream(), charset));
			throw new IOException ("The server returned the following status code: " + status);
		}

		int status = ((HttpURLConnection) connection).getResponseCode();
		if (status != 200 && status != 304){
			throw new IOException("The server returned the following status code " + status);
		}

		String data = getStringFromInputStream(response, charset);

		connection.disconnect();

		try{
			JSONObject dataObj = new JSONObject(data);
			return dataObj;
		} catch (JSONException e){
			JSONArray dataArray = new JSONArray(data);
			JSONObject dataObj  = new JSONObject();
			dataObj.put("data", dataArray);
			return dataObj;
		}
	}



	/*
	 * Constructs the full uri from the given base pathSpec and parameters
	 */
	public Uri buildUri(String base, String path, HashMap<String, String> params){
		Uri.Builder builder = new Uri.Builder();
		
		builder.scheme("https").authority(base).encodedPath(path);

		if (params == null){
			return builder.build();
		}

		Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			builder.appendQueryParameter(entry.getKey(), entry.getValue());
		}
		return builder.build();
	}
	
	
	/*
	 * Builds a path string based on the pathspec from the schema JSON file.
	 * Also fills the urlParams ArrayList with the names of the parameters used in the url
	 */
	private String buildPath(String pathSpec, HashMap<String, String> params, ArrayList<String> urlParams){
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(pathSpec);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String key = m.group(1);
			urlParams.add(key);
			String value = params.get(key);
			try {
				m.appendReplacement(sb, URLEncoder.encode((value == null ? "" : value), "UTF-8"));
			} catch (UnsupportedEncodingException e) {}
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String getStringFromInputStream(InputStream stream, String charset) throws IOException{
		String data = "";

		BufferedReader r = new BufferedReader(new InputStreamReader(stream, charset));
		StringBuilder total = new StringBuilder(stream.available());
		String line;
		while ((line = r.readLine()) != null) {
			total.append(line);
		}
		return total.toString();
	}
	


	/*
	 * Reads the schemas in from the json file and stores them as a JSONObject in the object's schemas property.
	 */
	private JSONObject readSchemas(){
		InputStream instream = context.getResources().openRawResource(R.raw.schemas);
		try {
			String schemas = getStringFromInputStream(instream, "UTF-8");
			Log.i(TAG, "About to parse JSON");
			return new JSONObject(schemas);
		} catch (Exception e) {
			Log.i(TAG, "Error reading schemas");
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Creates a hex encoded SHA-256 hash of the value
	 * @param value The value to hash
	 * @return The hash
	 */
	public static String shaString(String value){
		// SHA encode the password
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(value.getBytes());
			byte[] messageDigest = digest.digest();

			// convert to Hex string
			StringBuffer hexPassword = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++){
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while(h.length() < 2){
					h = "0" + h;
				}
				hexPassword.append(h);
			}
			value = hexPassword.toString();
		} catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
		return value;
	}
}
