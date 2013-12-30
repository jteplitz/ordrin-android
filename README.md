# Ordr.in Android Library

A android library for the ordr.in API.
See full API documentation at http://hackfood.ordr.in

## Table of Contents

 - [Installation](#installation)
 - [Usage](#usage)
   - [Initialization](#initialization)
   
   - [Order Endpoints](#order-endpoints-api-reference)
     - [Guest Order](#guest-order-api-reference) (`order_guest`)
     - [User Order](#user-order-api-reference) (`order_user`)
     
   - [Restaurant Endpoints](#restaurant-endpoints-api-reference)
     - [Delivery Check](#delivery-check-api-reference) (`delivery_check`)
     - [Delivery List](#delivery-list-api-reference) (`delivery_list`)
     - [Fee](#fee-api-reference) (`fee`)
     - [Restaurant Details](#restaurant-details-api-reference) (`restaurant_details`)
     
   - [User Endpoints](#user-endpoints-api-reference)
     - [Change Password](#change-password-api-reference) (`change_password`)
     - [Create Account](#create-account-api-reference) (`create_account`)
     - [Create Address](#create-address-api-reference) (`create_addr`)
     - [Create Credit Card](#create-credit-card-api-reference) (`create_cc`)
     - [Remove address](#remove-address-api-reference) (`delete_addr`)
     - [Remove Credit Card](#remove-credit-card-api-reference) (`delete_cc`)
     - [Get Account Information](#get-account-information-api-reference) (`get_account_info`)
     - [Get All Saved Addresses](#get-all-saved-addresses-api-reference) (`get_all_saved_addrs`)
     - [Get all saved credit cards](#get-all-saved-credit-cards-api-reference) (`get_all_saved_ccs`)
     - [Get an Order](#get-an-order-api-reference) (`get_order`)
     - [Get Order History](#get-order-history-api-reference) (`get_order_history`)
     - [Get a single saved address](#get-a-single-saved-address-api-reference) (`get_saved_addr`)
     - [Get a single saved credit card](#get-a-single-saved-credit-card-api-reference) (`get_saved_cc`)
     

## Installation

First import this project into your eclipse workspace using the Existing android project.

Then add this library to your project by right clicking on your project, going to properties->android, and under the library section click add and select the android-api project.

## Usage


### Callbacks

All of the Api methods are performed asynchronously on a seperate thread using Android Async_Task so that they don't cause your application to hang. This means that the actual api calls do not return any data, rather they take an object of type OrdrinApiListener. 

OrdrinApiListener is an abstract class. Extend it and override the onComplete method like so:
```java
OrdrinApiListener listener = new OrdrinApiListener(){
  @Override
  public void onComplete(JSONObject responseData, int status) {
    if (status != OrdrinApiListener.STATUS_OK){
      // there was an error performing the request.
      // if this was an api releated error status will equal OrdrinApiListener.STATUS_ORDRIN_ERROR, else if this was caused by the user input status will equal OrdrinApiListener.STATUS_USER_ERROR.
      return;
    }
    
    // perform app logic with the responseData object here.
}
```

Some ordrin api cals return JSON arrays instead of a single object. For consistency though this library will only ever call onComplete with a JSONObject. For api calls where the response data was an array, the array will be stored in property called data in the responseData object. 

For example if you call get_all_saved_addrs, the api returns an array. You can access the address objects like so:
```java
OrdrinApiListener listener = new OrdrinApiListener(){
  @Override
  public void onComplete(JSONObject responseData, int status) {
    if (status != OrdrinApiListener.STATUS_OK){
      // there was an error performing the request.
      // if this was an api releated error status will equal OrdrinApiListener.STATUS_ORDRIN_ERROR, else if this was caused by the user input status will equal OrdrinApiListener.STATUS_USER_ERROR.
      return;
    }
    try{
      JSONArray addresses = responseData.getJSONArray(responseDAta
      for (int i = 0; i < addresses.size(); i++){
        JSONObject address = addresses.get(i);
        // do something with the address here
      }
    } catch (JSONException e){
      // something went wrong while parsing the JSON data
    }
}


