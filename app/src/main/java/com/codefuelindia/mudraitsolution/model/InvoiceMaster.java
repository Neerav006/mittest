package com.codefuelindia.mudraitsolution.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceMaster {

    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("sr_no")
    @Expose
    private String srNo;
    @SerializedName("est_time")
    @Expose
    private String estTime;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("advance_pay")
    @Expose
    private String advancePay;
    @SerializedName("est_charge")
    @Expose
    private String estCharge;
    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("inner_product")
    @Expose
    private List<InnerProduct> innerProduct = null;
    @SerializedName("status_arrival")
    @Expose
    private List<StatusArrival> statusArrival = null;
    @SerializedName("del_status")
    @Expose
    private int del_status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("email")
    @Expose
    private String email;

    public int getDel_status() {
        return del_status;
    }

    public void setDel_status(int del_status) {
        this.del_status = del_status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getAdvancePay() {
        return advancePay;
    }

    public void setAdvancePay(String advancePay) {
        this.advancePay = advancePay;
    }

    public String getEstCharge() {
        return estCharge;
    }

    public void setEstCharge(String estCharge) {
        this.estCharge = estCharge;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public List<InnerProduct> getInnerProduct() {
        return innerProduct;
    }

    public void setInnerProduct(List<InnerProduct> innerProduct) {
        this.innerProduct = innerProduct;
    }

    public List<StatusArrival> getStatusArrival() {
        return statusArrival;
    }

    public void setStatusArrival(List<StatusArrival> statusArrival) {
        this.statusArrival = statusArrival;
    }

}