package model;

/**
 * Created by ryne on 31/03/2017.
 */

public class InventoryLab {

    private String parentCode;
    private int numberOfDeviceLeft;
    private int numberOfNormalDevice;
    private int numberOfBrokenDevice;
    private int numberOfUnusedDevice;
    private String noteDevice;

    public InventoryLab(String parentCode, int numberOfDeviceLeft, int numberOfNormalDevice, int numberOfBrokenDevice, int numberOfUnusedDevice, String noteDevice) {
        this.parentCode = parentCode;
        this.numberOfDeviceLeft = numberOfDeviceLeft;
        this.numberOfNormalDevice = numberOfNormalDevice;
        this.numberOfBrokenDevice = numberOfBrokenDevice;
        this.numberOfUnusedDevice = numberOfUnusedDevice;
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

    public int getNumberOfNormalDevice() {
        return numberOfNormalDevice;
    }

    public void setNumberOfNormalDevice(int numberOfNormalDevice) {
        this.numberOfNormalDevice = numberOfNormalDevice;
    }

    public int getNumberOfBrokenDevice() {
        return numberOfBrokenDevice;
    }

    public void setNumberOfBrokenDevice(int numberOfBrokenDevice) {
        this.numberOfBrokenDevice = numberOfBrokenDevice;
    }

    public int getNumberOfUnusedDevice() {
        return numberOfUnusedDevice;
    }

    public void setNumberOfUnusedDevice(int numberOfUnusedDevice) {
        this.numberOfUnusedDevice = numberOfUnusedDevice;
    }

    public String getNoteDevice() {
        return noteDevice;
    }

    public void setNoteDevice(String noteDevice) {
        this.noteDevice = noteDevice;
    }
}
