package com.example.rahul.myotpgeneration;

/**
 * Created by VNurtureTechnologies on 03/09/16.
 */
public class SMSModel {
    private String id;
    private String address;
    private String body;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
