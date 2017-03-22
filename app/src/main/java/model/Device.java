package model;

/**
 * Created by ryne on 22/03/2017.
 */

public class Device {
    private String name;
    private String origin;
    private String yearofProduce;

    public  Device(){

    }
    public Device(String name, String origin, String yearofProduce) {
        this.name = name;
        this.origin = origin;
        this.yearofProduce = yearofProduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getYearofProduce() {
        return yearofProduce;
    }

    public void setYearofProduce(String yearofProduce) {
        this.yearofProduce = yearofProduce;
    }
}
