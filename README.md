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

### Initialization

```java
import com.ordrin.api_android.Api;
import com.ordrin.api_android.ApiConstants.Server;

Api ordrinApi = new Api(context, api_key, Servers.TEST);
```

In the initializer, the third argument sets the servers that Api requests will
be sent to, and must be set to either Servers.TEST Servers.PRODUCTION.
(The server argument is optional. It defaults to Servers.TEST) 


### Order Endpoints ([API Reference](http://hackfood.ordr.in/docs/order))

#### Guest Order ([API Reference](http://hackfood.ordr.in/docs/order#order_guest))

  ordrinApi.order_guest(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.rid` : Ordr.in's unique restaurant identifier for the restaurant. (A number)
- `args.em` : The customer's email address
- `args.tray` : Represents a tray of menu items in the format '[menu item id]/[qty],[option id],...,[option id]'
- `args.tip` : Tip amount in dollars and cents
- `args.first_name` : The customer's first name
- `args.last_name` : The customer's last name
- `args.phone` : The customer's phone number
- `args.zip` : The zip code part of the address (5 digits)
- `args.addr` : The street address
- `args.addr2` : The second part of the street address, if needed
- `args.city` : The city part of the address
- `args.state` : The state part of the address (Two letters)
- `args.card_name` : Full name as it appears on the credit card
- `args.card_number` : Credit card number (16 digits)
- `args.card_cvc` : 3 or 4 digit security code (3 or 4 digits)
- `args.card_expiry` : The credit card expiration date. (mm/yyyy)
- `args.card_bill_addr` : The credit card's billing street address
- `args.card_bill_addr2` : The second part of the credit card's biling street address.
- `args.card_bill_city` : The credit card's billing city
- `args.card_bill_state` : The credit card's billing state (2 letters)
- `args.card_bill_zip` : The credit card's billing zip code (5 digits)
- `args.card_bill_phone` : The credit card's billing phone number


###### Either
- `args.delivery_date` : Delivery date (mm-dd)
- `args.delivery_time` : Delivery time (HH:MM)

###### Or
- `args.delivery_date` : Delivery date (ASAP)



#### User Order ([API Reference](http://hackfood.ordr.in/docs/order#order_user))

  ordrinApi.order_user(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.rid` : Ordr.in's unique restaurant identifier for the restaurant. (A number)
- `args.tray` : Represents a tray of menu items in the format '[menu item id]/[qty],[option id],...,[option id]'
- `args.tip` : Tip amount in dollars and cents
- `args.first_name` : The customer's first name
- `args.last_name` : The customer's last name
- `args.email` : The user's email address
- `args.current_password` : The user's current password

###### Either
- `args.addr` : The street address
- `args.addr2` : The second part of the street address, if needed
- `args.city` : The city part of the address
- `args.phone` : The customer's phone number
- `args.state` : The state part of the address (Two letters)
- `args.zip` : The zip code part of the address (5 digits)

###### Or
- `args.nick` : The delivery location nickname. (From the user's addresses)



###### Either
- `args.card_bill_addr` : The credit card's billing street address
- `args.card_bill_addr2` : The second part of the credit card's biling street address.
- `args.card_bill_city` : The credit card's billing city
- `args.card_bill_phone` : The credit card's billing phone number
- `args.card_bill_state` : The credit card's billing state (2 letters)
- `args.card_bill_zip` : The credit card's billing zip code (5 digits)
- `args.card_cvc` : 3 or 4 digit security code (3 or 4 digits)
- `args.card_expiry` : The credit card expiration date. (mm/yyyy)
- `args.card_name` : Full name as it appears on the credit card
- `args.card_number` : Credit card number (16 digits)

###### Or
- `args.card_nick` : The credit card nickname. (From the user's credit cards)



###### Either
- `args.delivery_date` : Delivery date (mm-dd)
- `args.delivery_time` : Delivery time (HH:MM)

###### Or
- `args.delivery_date` : Delivery date (ASAP)




### Restaurant Endpoints ([API Reference](http://hackfood.ordr.in/docs/restaurant))

#### Delivery Check ([API Reference](http://hackfood.ordr.in/docs/restaurant#delivery_check))

  ordrinApi.delivery_check(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.datetime` : Delivery date and time (ASAP or mm-dd+HH:MM)
- `args.rid` : Ordr.in's unique restaurant identifier for the restaurant. (A number)
- `args.addr` : Delivery location street address
- `args.city` : Delivery location city
- `args.zip` : The zip code part of the address (5 digits)


#### Delivery List ([API Reference](http://hackfood.ordr.in/docs/restaurant#delivery_list))

  ordrinApi.delivery_list(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.datetime` : Delivery date and time (ASAP or mm-dd+HH:MM)
- `args.addr` : Delivery location street address
- `args.city` : Delivery location city
- `args.zip` : The zip code part of the address (5 digits)


#### Fee ([API Reference](http://hackfood.ordr.in/docs/restaurant#fee))

  ordrinApi.fee(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.datetime` : Delivery date and time (ASAP or mm-dd+HH:MM)
- `args.rid` : Ordr.in's unique restaurant identifier for the restaurant. (A number)
- `args.subtotal` : The cost of all items in the tray in dollars and cents.
- `args.tip` : The tip in dollars and cents.
- `args.addr` : Delivery location street address
- `args.city` : Delivery location city
- `args.zip` : The zip code part of the address (5 digits)


#### Restaurant Details ([API Reference](http://hackfood.ordr.in/docs/restaurant#restaurant_details))

  ordrinApi.restaurant_details(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.rid` : Ordr.in's unique restaurant identifier for the restaurant. (A number)



### User Endpoints ([API Reference](http://hackfood.ordr.in/docs/user))

#### Change Password ([API Reference](http://hackfood.ordr.in/docs/user#change_password))

  ordrinApi.change_password(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.password` : The user's new password (SHA256 hex encoded)
- `args.current_password` : The user's current password

#### Create Account ([API Reference](http://hackfood.ordr.in/docs/user#create_account))

  ordrinApi.create_account(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.pw` : The user's password
- `args.first_name` : The user's first name
- `args.last_name` : The user's last name


#### Create Address ([API Reference](http://hackfood.ordr.in/docs/user#create_addr))

  ordrinApi.create_addr(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.nick` : The nickname of this address
- `args.phone` : The customer's phone number
- `args.zip` : The zip code part of the address (5 digits)
- `args.addr` : The street address
- `args.addr2` : The second part of the street address, if needed
- `args.city` : The city part of the address
- `args.state` : The state part of the address (Two letters)
- `args.current_password` : The user's current password

#### Create Credit Card ([API Reference](http://hackfood.ordr.in/docs/user#create_cc))

  ordrinApi.create_cc(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.nick` : The nickname of this address
- `args.card_number` : Credit card number (16 digits)
- `args.card_cvc` : 3 or 4 digit security code (3 or 4 digits)
- `args.card_expiry` : The credit card expiration date. (Two digits/Four digits)
- `args.bill_addr` : The credit card's billing street address
- `args.bill_addr2` : The second part of the credit card's biling street address.
- `args.bill_city` : The credit card's billing city
- `args.bill_state` : The credit card's billing state (2 letters)
- `args.bill_zip` : The credit card's billing zip code (5 digits)
- `args.bill_phone` : The credit card's billing phone number
- `args.current_password` : The user's current password

#### Remove address ([API Reference](http://hackfood.ordr.in/docs/user#delete_addr))

  ordrinApi.delete_addr(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.nick` : The nickname of this address
- `args.current_password` : The user's current password

#### Remove Credit Card ([API Reference](http://hackfood.ordr.in/docs/user#delete_cc))

  ordrinApi.delete_cc(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.nick` : The nickname of this address
- `args.current_password` : The user's current password

#### Get Account Information ([API Reference](http://hackfood.ordr.in/docs/user#get_account_info))

  ordrinApi.get_account_info(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.current_password` : The user's current password

#### Get All Saved Addresses ([API Reference](http://hackfood.ordr.in/docs/user#get_all_saved_addrs))

  ordrinApi.get_all_saved_addrs(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.current_password` : The user's current password

#### Get all saved credit cards ([API Reference](http://hackfood.ordr.in/docs/user#get_all_saved_ccs))

  ordrinApi.get_all_saved_ccs(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.current_password` : The user's current password

#### Get an Order ([API Reference](http://hackfood.ordr.in/docs/user#get_order))

  ordrinApi.get_order(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.oid` : Ordr.in's unique order id number. (A number)
- `args.current_password` : The user's current password

#### Get Order History ([API Reference](http://hackfood.ordr.in/docs/user#get_order_history))

  ordrinApi.get_order_history(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.current_password` : The user's current password

#### Get a single saved address ([API Reference](http://hackfood.ordr.in/docs/user#get_saved_addr))

  ordrinApi.get_saved_addr(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.nick` : The nickname of this address
- `args.current_password` : The user's current password

#### Get a single saved credit card ([API Reference](http://hackfood.ordr.in/docs/user#get_saved_cc))

  ordrinApi.get_saved_cc(args, new OrdrinApiListener(){
    @Override
    public void onComplete(JSONObject responseData, int status){
      // handle response here
    }
  });

##### Arguments
- `args.email` : The user's email address
- `args.nick` : The nickname of this address
- `args.current_password` : The user's current password



