package com.hotel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Hotel {

    @Id
    private String hid;
    private String roomtype;
    private String imageurl;
    private int roomprice;
    private String imageid;


    public String getHid() {
        return hid;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public String getImageurl() {
        return imageurl;
    }

    public int getRoomprice() {
        return roomprice;
    }

    public String getImageid() {
        return imageid;
    }


    public void setHid(String hid) {
        this.hid = hid;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setRoomprice(int roomprice) {
        this.roomprice = roomprice;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }


    @Override
    public String toString() {
        return "Hotel{" +
                "hid='" + hid + '\'' +
                ", roomtype='" + roomtype + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", roomprice=" + roomprice +
                ", imageid='" + imageid + '\'' +
                '}';
    }
}
