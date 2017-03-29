package model;

import java.io.Serializable;

/**
 * Created by ryne on 29/03/2017.
 */

public class Labroom implements Serializable {
    private int id;
    private String name;

    public Labroom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.getName();
    }
}
