package com.codefuelindia.mudraitsolution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileModel {


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @SerializedName("file_name")
    @Expose
    private String fileName;

    @SerializedName("file_url")
    @Expose
    private String fileUrl;

}
