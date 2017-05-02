package com.jayavijayjayavelu.knowyourgovernment;

/**
 * Created by jayavijayjayavelu on 4/3/17.
 */

public class CivicDetails {
    String name;
    String address;
    String city;
    String state;
    String zip;
    String party;
    String phone;
    String url;
    String email;
    String photo;
    String channels[][];

    public CivicDetails(String name, String address, String city, String state, String zip, String party, String phone, String url, String email, String photo, String[][] channels) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.party = party;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photo = photo;
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String[][] getChannels() {
        return channels;
    }

    public void setChannels(String[][] channels) {
        this.channels = channels;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
