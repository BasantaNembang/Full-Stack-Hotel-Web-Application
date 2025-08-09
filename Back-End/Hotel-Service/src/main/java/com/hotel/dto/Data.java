package com.hotel.dto;



public class Data {

    private String roomtype;
    private Double roomprice;

    public Double getRoomprice() {
        return roomprice;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public void setRoomprice(Double roomprice) {
        this.roomprice = roomprice;
    }

    public String getRoomtype() {
        return roomtype;
    }

    @Override
    public String toString() {
        return "Data{" +
                "roomtype='" + roomtype + '\'' +
                ", roomprice=" + roomprice +
                '}';
    }


}

