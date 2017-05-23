package model;

import java.io.Serializable;

/**
 * Created by ryne on 22/03/2017.
 */

public class Device implements  Serializable{
    private String name;
    private String parentCode;
    private String producer;
    private String country;
    private String dateofProduce;
    private String digital;
    private String staff;
    private String room;
    private String timeofWarranty;
    private String description;

    public  Device(){

    }
    public Device(String name, String parentCode){
        this.name = name;
        this.parentCode = parentCode;
    }
    public Device(String name, String parentcode, String producer, String country, String dateofProduce, String digital, String staff, String room, String timeofWarranty, String description) {
        this.name = name;
        this.parentCode = parentcode;
        this.producer = producer;
        this.country = country;
        this.dateofProduce = dateofProduce;
        this.digital = digital;
        this.staff = staff;
        this.room = room;
        this.timeofWarranty = timeofWarranty;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentcode() {
        return parentCode;
    }

    public void setParentcode(String parentcode) {
        this.parentCode = parentcode;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateofProduce() {
        return dateofProduce;
    }

    public void setDateofProduce(String dateofProduce) {
        this.dateofProduce = dateofProduce;
    }

    public String getDigital() {
        return digital;
    }

    public void setDigital(String digital) {
        this.digital = digital;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTimeofWarranty() {
        return timeofWarranty;
    }

    public void setTimeofWarranty(String timeofWarranty) {
        this.timeofWarranty = timeofWarranty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString(){
        return this.getName()+" "+this.getParentcode();
    }
}
