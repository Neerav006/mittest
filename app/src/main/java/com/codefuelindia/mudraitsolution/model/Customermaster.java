package com.codefuelindia.mudraitsolution.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customermaster implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("rec_by")
    @Expose
    private String recBy;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pc")
    @Expose
    private String pc;
    @SerializedName("laptop")
    @Expose
    private String laptop;
    @SerializedName("mini_notebook")
    @Expose
    private String miniNotebook;
    @SerializedName("all_in_pc_tablate")
    @Expose
    private String allInPcTablate;
    @SerializedName("printer_aiopsc")
    @Expose
    private String printerAiopsc;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("sn_pn")
    @Expose
    private String snPn;
    @SerializedName("adapter")
    @Expose
    private String adapter;
    @SerializedName("power_cord")
    @Expose
    private String powerCord;
    @SerializedName("battery")
    @Expose
    private String battery;
    @SerializedName("drive")
    @Expose
    private String drive;
    @SerializedName("bag")
    @Expose
    private String bag;
    @SerializedName("os_disk")
    @Expose
    private String osDisk;
    @SerializedName("drive_disk")
    @Expose
    private String driveDisk;
    @SerializedName("printer_drive_disk")
    @Expose
    private String printerDriveDisk;
    @SerializedName("printer_adapter")
    @Expose
    private String printerAdapter;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("advance_pay")
    @Expose
    private String advancePay;
    @SerializedName("est_charge")
    @Expose
    private String estCharge;
    @SerializedName("est_time")
    @Expose
    private String estTime;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("sr_no")
    @Expose
    private String srNo;
    @SerializedName("dead_no_power")
    @Expose
    private String deadNoPower;
    @SerializedName("dim_display")
    @Expose
    private String dimDisplay;
    @SerializedName("body_damage")
    @Expose
    private String bodyDamage;
    @SerializedName("no_bootup")
    @Expose
    private String noBootup;
    @SerializedName("job_number")
    @Expose
    private String jobNumber;
    @SerializedName("remaining_amount")
    @Expose
    private String remaining_amount;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    @SerializedName("total_amount")
    @Expose
    private String total_amount;


    public String getReady_status() {
        return ready_status;
    }

    public void setReady_status(String ready_status) {
        this.ready_status = ready_status;
    }

    @SerializedName("ready_status")
    @Expose
    private String ready_status;





    public String getTotal_amount_status() {
        return total_amount_status;
    }

    public void setTotal_amount_status(String total_amount_status) {
        this.total_amount_status = total_amount_status;
    }

    @SerializedName("total_amount_status")
    @Expose
    private String total_amount_status;


    public String getOs_reload() {
        return os_reload;
    }

    public void setOs_reload(String os_reload) {
        this.os_reload = os_reload;
    }

    public String getServicing() {
        return servicing;
    }

    public void setServicing(String servicing) {
        this.servicing = servicing;
    }

    public String getDisplay_fliker() {
        return display_fliker;
    }

    public void setDisplay_fliker(String display_fliker) {
        this.display_fliker = display_fliker;
    }

    public String getBody_repair() {
        return body_repair;
    }

    public void setBody_repair(String body_repair) {
        this.body_repair = body_repair;
    }

    @SerializedName("os_reload")
    @Expose
    private String os_reload;


    @SerializedName("servicing")
    @Expose
    private String servicing;


    @SerializedName("display_fliker")
    @Expose
    private String display_fliker;



    @SerializedName("body_repair")
    @Expose
    private String body_repair;






    public Customermaster() {
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(String remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecBy() {
        return recBy;
    }

    public void setRecBy(String recBy) {
        this.recBy = recBy;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getLaptop() {
        return laptop;
    }

    public void setLaptop(String laptop) {
        this.laptop = laptop;
    }

    public String getMiniNotebook() {
        return miniNotebook;
    }

    public void setMiniNotebook(String miniNotebook) {
        this.miniNotebook = miniNotebook;
    }

    public String getAllInPcTablate() {
        return allInPcTablate;
    }

    public void setAllInPcTablate(String allInPcTablate) {
        this.allInPcTablate = allInPcTablate;
    }

    public String getPrinterAiopsc() {
        return printerAiopsc;
    }

    public void setPrinterAiopsc(String printerAiopsc) {
        this.printerAiopsc = printerAiopsc;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSnPn() {
        return snPn;
    }

    public void setSnPn(String snPn) {
        this.snPn = snPn;
    }

    public String getAdapter() {
        return adapter;
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }

    public String getPowerCord() {
        return powerCord;
    }

    public void setPowerCord(String powerCord) {
        this.powerCord = powerCord;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public String getOsDisk() {
        return osDisk;
    }

    public void setOsDisk(String osDisk) {
        this.osDisk = osDisk;
    }

    public String getDriveDisk() {
        return driveDisk;
    }

    public void setDriveDisk(String driveDisk) {
        this.driveDisk = driveDisk;
    }

    public String getPrinterDriveDisk() {
        return printerDriveDisk;
    }

    public void setPrinterDriveDisk(String printerDriveDisk) {
        this.printerDriveDisk = printerDriveDisk;
    }

    public String getPrinterAdapter() {
        return printerAdapter;
    }

    public void setPrinterAdapter(String printerAdapter) {
        this.printerAdapter = printerAdapter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getDeadNoPower() {
        return deadNoPower;
    }

    public void setDeadNoPower(String deadNoPower) {
        this.deadNoPower = deadNoPower;
    }

    public String getDimDisplay() {
        return dimDisplay;
    }

    public void setDimDisplay(String dimDisplay) {
        this.dimDisplay = dimDisplay;
    }

    public String getBodyDamage() {
        return bodyDamage;
    }

    public void setBodyDamage(String bodyDamage) {
        this.bodyDamage = bodyDamage;
    }

    public String getNoBootup() {
        return noBootup;
    }

    public void setNoBootup(String noBootup) {
        this.noBootup = noBootup;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.date);
        dest.writeString(this.recBy);
        dest.writeString(this.customerName);
        dest.writeString(this.mobile);
        dest.writeString(this.address);
        dest.writeString(this.pc);
        dest.writeString(this.laptop);
        dest.writeString(this.miniNotebook);
        dest.writeString(this.allInPcTablate);
        dest.writeString(this.printerAiopsc);
        dest.writeString(this.brandName);
        dest.writeString(this.snPn);
        dest.writeString(this.adapter);
        dest.writeString(this.powerCord);
        dest.writeString(this.battery);
        dest.writeString(this.drive);
        dest.writeString(this.bag);
        dest.writeString(this.osDisk);
        dest.writeString(this.driveDisk);
        dest.writeString(this.printerDriveDisk);
        dest.writeString(this.printerAdapter);
        dest.writeString(this.status);
        dest.writeString(this.advancePay);
        dest.writeString(this.estCharge);
        dest.writeString(this.estTime);
        dest.writeString(this.issue);
        dest.writeString(this.srNo);
        dest.writeString(this.deadNoPower);
        dest.writeString(this.dimDisplay);
        dest.writeString(this.bodyDamage);
        dest.writeString(this.noBootup);
        dest.writeString(this.jobNumber);
        dest.writeString(this.remaining_amount);
        dest.writeString(this.total_amount);
        dest.writeString(this.ready_status);
        dest.writeString(this.total_amount_status);
        dest.writeString(this.os_reload);
        dest.writeString(this.servicing);
        dest.writeString(this.display_fliker);
        dest.writeString(this.body_repair);
    }

    protected Customermaster(Parcel in) {
        this.id = in.readString();
        this.date = in.readString();
        this.recBy = in.readString();
        this.customerName = in.readString();
        this.mobile = in.readString();
        this.address = in.readString();
        this.pc = in.readString();
        this.laptop = in.readString();
        this.miniNotebook = in.readString();
        this.allInPcTablate = in.readString();
        this.printerAiopsc = in.readString();
        this.brandName = in.readString();
        this.snPn = in.readString();
        this.adapter = in.readString();
        this.powerCord = in.readString();
        this.battery = in.readString();
        this.drive = in.readString();
        this.bag = in.readString();
        this.osDisk = in.readString();
        this.driveDisk = in.readString();
        this.printerDriveDisk = in.readString();
        this.printerAdapter = in.readString();
        this.status = in.readString();
        this.advancePay = in.readString();
        this.estCharge = in.readString();
        this.estTime = in.readString();
        this.issue = in.readString();
        this.srNo = in.readString();
        this.deadNoPower = in.readString();
        this.dimDisplay = in.readString();
        this.bodyDamage = in.readString();
        this.noBootup = in.readString();
        this.jobNumber = in.readString();
        this.remaining_amount = in.readString();
        this.total_amount = in.readString();
        this.ready_status = in.readString();
        this.total_amount_status = in.readString();
        this.os_reload = in.readString();
        this.servicing = in.readString();
        this.display_fliker = in.readString();
        this.body_repair = in.readString();
    }

    public static final Creator<Customermaster> CREATOR = new Creator<Customermaster>() {
        @Override
        public Customermaster createFromParcel(Parcel source) {
            return new Customermaster(source);
        }

        @Override
        public Customermaster[] newArray(int size) {
            return new Customermaster[size];
        }
    };
}