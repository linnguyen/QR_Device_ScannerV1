package model;

/**
 * Created by ryne on 31/03/2017.
 */

public class InventoryLab {

    private String parentCode;
    private int numberOfDeviceLeft;
    private String noteDevice;

    public InventoryLab(String parentCode, int numberOfDeviceLeft, String noteDevice) {
        this.parentCode = parentCode;
        this.numberOfDeviceLeft = numberOfDeviceLeft;
        this.noteDevice = noteDevice;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getNumberOfDeviceLeft() {
        return numberOfDeviceLeft;
    }

    public void setNumberOfDeviceLeft(int numberOfDeviceLeft) {
        this.numberOfDeviceLeft = numberOfDeviceLeft;
    }

    public String getNoteDevice() {
        return noteDevice;
    }

    public void setNoteDevice(String noteDevice) {
        this.noteDevice = noteDevice;
    }

}
