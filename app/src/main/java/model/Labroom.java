package model;

import java.io.Serializable;

/**
 * Created by ryne on 29/03/2017.
 */

public class Labroom implements Serializable {
    private String id;
    private String name;

    public Labroom(String id, String name) {
        this.id = id;
        this.name = name;
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

    public String toString(){
        return  this.name;
    }
}
