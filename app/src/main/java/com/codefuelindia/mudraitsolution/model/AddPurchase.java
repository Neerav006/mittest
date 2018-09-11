package com.codefuelindia.mudraitsolution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPurchase {

@SerializedName("shop_id")
@Expose
private String shopId;
@SerializedName("cus_name")
@Expose
private String cusName;
@SerializedName("cus_addr")
@Expose
private String cusAddr;
@SerializedName("cus_city")
@Expose
private String cusCity;
@SerializedName("cus_state")
@Expose
private String cusState;
@SerializedName("cus_mobile")
@Expose
private String cusMobile;
@SerializedName("cus_pincode")
@Expose
private String cusPincode;
@SerializedName("product_name")
@Expose
private String productName;
@SerializedName("product_info")
@Expose
private String productInfo;
@SerializedName("total_amount")
@Expose
private String totalAmount;
@SerializedName("received_amount")
@Expose
private String receivedAmount;

public String getShopId() {
return shopId;
}

public void setShopId(String shopId) {
this.shopId = shopId;
}

public String getCusName() {
return cusName;
}

public void setCusName(String cusName) {
this.cusName = cusName;
}

public String getCusAddr() {
return cusAddr;
}

public void setCusAddr(String cusAddr) {
this.cusAddr = cusAddr;
}

public String getCusCity() {
return cusCity;
}

public void setCusCity(String cusCity) {
this.cusCity = cusCity;
}

public String getCusState() {
return cusState;
}

public void setCusState(String cusState) {
this.cusState = cusState;
}

public String getCusMobile() {
return cusMobile;
}

public void setCusMobile(String cusMobile) {
this.cusMobile = cusMobile;
}

public String getCusPincode() {
return cusPincode;
}

public void setCusPincode(String cusPincode) {
this.cusPincode = cusPincode;
}

public String getProductName() {
return productName;
}

public void setProductName(String productName) {
this.productName = productName;
}

public String getProductInfo() {
return productInfo;
}

public void setProductInfo(String productInfo) {
this.productInfo = productInfo;
}

public String getTotalAmount() {
return totalAmount;
}

public void setTotalAmount(String totalAmount) {
this.totalAmount = totalAmount;
}

public String getReceivedAmount() {
return receivedAmount;
}

public void setReceivedAmount(String receivedAmount) {
this.receivedAmount = receivedAmount;
}

}