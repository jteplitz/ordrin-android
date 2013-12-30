package com.ordrin.api_android;

public class ApiConstants{

  public enum Servers{
    TEST, PRODUCTION
  };

	public enum RequestType {
		GET, POST, PUT, DELETE;
		
		@Override 
		public String toString(){
			switch (this){
				case GET:
					return "GET";
				case POST:
					return "POST";
				case PUT:
					return "PUT";
				case DELETE:
					return "DELETE";
			}
			return null;
		}
	}
}
