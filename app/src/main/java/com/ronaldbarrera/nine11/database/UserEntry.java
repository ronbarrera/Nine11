package com.ronaldbarrera.nine11.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class UserEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String personalName;
    private String personalPhone;
    private String personalAddress;
    private String dob;
    private String bloodtype;
    private String contactName;
    private String contactPhone;
    private String pictureUri;

    @Ignore
    public UserEntry(String personalName, String personalPhone, String personalAddress, String dob, String bloodtype, String contactName, String contactPhone) {
        this.personalName = personalName;
        this.personalPhone = personalPhone;
        this.personalAddress = personalAddress;
        this.dob = dob;
        this.bloodtype = bloodtype;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    public UserEntry(int id, String personalName, String personalPhone, String personalAddress, String dob, String bloodtype, String contactName, String contactPhone) {
        this.id = id;
        this.personalName = personalName;
        this.personalPhone = personalPhone;
        this.personalAddress = personalAddress;
        this.dob = dob;
        this.bloodtype = bloodtype;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public String getPersonalAddress() {
        return personalAddress;
    }

    public void setPersonalAddress(String personalAddress) {
        this.personalAddress = personalAddress;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }
}
