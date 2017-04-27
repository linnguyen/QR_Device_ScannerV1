package model;

/**
 * Created by ryne on 27/04/2017.
 */

public class InventorySeason {
    private int id;
    private String name;

    public InventorySeason(){

    }
    public InventorySeason(int id, String name) {
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
        return this.name;
    }
}
