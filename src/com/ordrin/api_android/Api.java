package com.ordrin.api_android;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ordrin.api_android.ApiConstants.*;

public class Api {
	private ApiHelper helper;
	public Api(Context context, String api_key, Servers servers){
		helper = new ApiHelper(context, api_key, servers);
	}
	
	public Api(Context context, String api_key){
		this(context, api_key, Servers.TEST);
	}

  
  // order endpoints
  
  
  public void order_guest(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    rid--Ordr.in's unique restaurant identifier for the restaurant.
    em--The customer's email address
    tray--Represents a tray of menu items in the format '[menu item id]/[qty],[option id],...,[option id]'
    tip--Tip amount in dollars and cents
    first_name--The customer's first name
    last_name--The customer's last name
    phone--The customer's phone number
    zip--The zip code part of the address
    addr--The street address
    city--The city part of the address
    state--The state part of the address
    card_number--Credit card number
    card_cvc--3 or 4 digit security code
    card_expiry--The credit card expiration date.
    card_bill_addr--The credit card's billing street address
    card_bill_city--The credit card's billing city
    card_bill_state--The credit card's billing state
    card_bill_zip--The credit card's billing zip code
    card_bill_phone--The credit card's billing phone number

    Keyword Arguments:
    addr2--The second part of the street address, if needed
    card_name--Full name as it appears on the credit card
    card_bill_addr2--The second part of the credit card's biling street address.


    Either
    delivery_date--Delivery date
    delivery_time--Delivery time
    OR
    delivery_date--Delivery date
     */
     ApiParams params = new ApiParams("order", "order_guest", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void order_user(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    rid--Ordr.in's unique restaurant identifier for the restaurant.
    tray--Represents a tray of menu items in the format '[menu item id]/[qty],[option id],...,[option id]'
    tip--Tip amount in dollars and cents
    first_name--The customer's first name
    last_name--The customer's last name
    email -- The user's email
    current_password -- The user's current password

    Keyword Arguments:


    Either
    phone--The customer's phone number
    zip--The zip code part of the address
    addr--The street address
    addr2--The second part of the street address, if needed
    city--The city part of the address
    state--The state part of the address
    OR
    nick--The delivery location nickname. (From the user's addresses)
    Either
    card_name--Full name as it appears on the credit card
    card_number--Credit card number
    card_cvc--3 or 4 digit security code
    card_expiry--The credit card expiration date.
    card_bill_addr--The credit card's billing street address
    card_bill_addr2--The second part of the credit card's biling street address.
    card_bill_city--The credit card's billing city
    card_bill_state--The credit card's billing state
    card_bill_zip--The credit card's billing zip code
    card_bill_phone--The credit card's billing phone number
    OR
    card_nick--The credit card nickname. (From the user's credit cards)
    Either
    delivery_date--Delivery date
    delivery_time--Delivery time
    OR
    delivery_date--Delivery date
     */
     ApiParams params = new ApiParams("order", "order_user", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  // restaurant endpoints
  
  
  public void delivery_check(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    datetime--Delivery date and time
    rid--Ordr.in's unique restaurant identifier for the restaurant.
    addr--Delivery location street address
    city--Delivery location city
    zip--The zip code part of the address

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("restaurant", "delivery_check", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void delivery_list(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    datetime--Delivery date and time
    addr--Delivery location street address
    city--Delivery location city
    zip--The zip code part of the address

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("restaurant", "delivery_list", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void fee(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    datetime--Delivery date and time
    rid--Ordr.in's unique restaurant identifier for the restaurant.
    subtotal--The cost of all items in the tray in dollars and cents.
    tip--The tip in dollars and cents.
    addr--Delivery location street address
    city--Delivery location city
    zip--The zip code part of the address

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("restaurant", "fee", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void restaurant_details(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    rid--Ordr.in's unique restaurant identifier for the restaurant.

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("restaurant", "restaurant_details", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  // user endpoints
  
  
  public void change_password(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    password--The user's new password
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "change_password", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void create_account(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    pw--The user's password
    first_name--The user's first name
    last_name--The user's last name

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "create_account", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void create_addr(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    nick--The nickname of this address
    phone--The customer's phone number
    zip--The zip code part of the address
    addr--The street address
    city--The city part of the address
    state--The state part of the address
    current_password -- The user's current password

    Keyword Arguments:
    addr2--The second part of the street address, if needed


     */
     ApiParams params = new ApiParams("user", "create_addr", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void create_cc(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    nick--The nickname of this address
    card_number--Credit card number
    card_cvc--3 or 4 digit security code
    card_expiry--The credit card expiration date.
    bill_addr--The credit card's billing street address
    bill_city--The credit card's billing city
    bill_state--The credit card's billing state
    bill_zip--The credit card's billing zip code
    bill_phone--The credit card's billing phone number
    current_password -- The user's current password

    Keyword Arguments:
    bill_addr2--The second part of the credit card's biling street address.


     */
     ApiParams params = new ApiParams("user", "create_cc", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void delete_addr(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    nick--The nickname of this address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "delete_addr", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void delete_cc(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    nick--The nickname of this address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "delete_cc", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void get_account_info(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "get_account_info", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void get_all_saved_addrs(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "get_all_saved_addrs", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void get_all_saved_ccs(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "get_all_saved_ccs", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void get_order(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    oid--Ordr.in's unique order id number.
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "get_order", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void get_order_history(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "get_order_history", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void get_saved_addr(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    nick--The nickname of this address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "get_saved_addr", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  
  public void get_saved_cc(HashMap<String, String> args, OrdrinApiListener listener){
    /*
      Arguments:
    email--The user's email address
    nick--The nickname of this address
    current_password -- The user's current password

    Keyword Arguments:


     */
     ApiParams params = new ApiParams("user", "get_saved_cc", args, listener);

     new ApiRequestTask().execute(params);
  }
  
  

	private class ApiParams{
		String api;
		String endpoint;
		
		HashMap<String, String> args;
		OrdrinApiListener listener;
		
		public ApiParams(String api, String endpoint, HashMap<String, String> args, OrdrinApiListener listener){
			this.api 	  = api;
			this.endpoint = endpoint;
			this.args	  = args;
			this.listener = listener;
		}
	}
	
	private class ApiRequestTask extends AsyncTask<ApiParams, Void, JSONObject>{
		private OrdrinApiListener listener;
		protected JSONObject doInBackground(ApiParams... paramsList){
			ApiParams params = paramsList[0];
			listener 		= params.listener;
			
			try {
				return helper.callEndpoint(params.api, params.endpoint, params.args);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				listener.onComplete(null, OrdrinApiListener.STATUS_ORDRIN_ERROR);
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				listener.onComplete(null, OrdrinApiListener.STATUS_ORDRIN_ERROR);
				return null;
			} catch (JSONException e) {
				e.printStackTrace();
				listener.onComplete(null, OrdrinApiListener.STATUS_ORDRIN_ERROR);
				return null;
			}
		}
		
		protected void onPostExecute(JSONObject data){
			if (data == null){
				// there was an error. This should have triggered the onComplete handler already;
				return;
			}
			listener.onComplete(data, OrdrinApiListener.STATUS_OK);
		}
	}
	
	public static abstract class OrdrinApiListener{
		public static final int STATUS_OK  			= 1;
		public static final int STATUS_ORDRIN_ERROR = -1;
		public static final int STAUTS_USER_ERROR   = -2;
		public abstract void onComplete(JSONObject data, int status);
	}
}
