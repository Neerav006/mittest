package com.codefuelindia.mudraitsolution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyRes {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("m_id")
    @Expose
    private String mem_id;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("ssy")
    @Expose
    private boolean is_ssy;
    @SerializedName("type")
    @Expose
    private boolean type;
    @SerializedName("not_count")
    @Expose
    private int count;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("member_no")
    @Expose
    private String member_no;
    @SerializedName("premium")
    @Expose
    private String isPremium;

    public String getJobcard() {
        return jobcard;
    }

    public void setJobcard(String jobcard) {
        this.jobcard = jobcard;
    }

    @SerializedName("jobcard")
    @Expose
    private String jobcard;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @SerializedName("addr")
    @Expose
    private String addr;

    public String getGor() {
        return gor;
    }

    public void setGor(String gor) {
        this.gor = gor;
    }

    @SerializedName("gor")
    @Expose
    private String gor;

    public String getFamily_count() {
        return family_count;
    }

    public void setFamily_count(String family_count) {
        this.family_count = family_count;
    }

    @SerializedName("f_count")
    @Expose
    private String family_count;

    public String getGroup_count() {
        return group_count;
    }

    public void setGroup_count(String group_count) {
        this.group_count = group_count;
    }

    @SerializedName("group_count")
    @Expose
    private String group_count;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMember_no() {
        return member_no;
    }

    public void setMember_no(String member_no) {
        this.member_no = member_no;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIs_ssy() {
        return is_ssy;
    }

    public void setIs_ssy(boolean is_ssy) {
        this.is_ssy = is_ssy;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String isPremium() {
        return isPremium;
    }

    public void setPremium(String premium) {
        isPremium = premium;
    }

    public String getPdf_id() {
        return pdf_id;
    }

    public void setPdf_id(String pdf_id) {
        this.pdf_id = pdf_id;
    }

    @SerializedName("pdf_id")
    private String pdf_id;

}
