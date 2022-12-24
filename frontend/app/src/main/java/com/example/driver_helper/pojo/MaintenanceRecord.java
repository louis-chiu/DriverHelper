package com.example.driver_helper.pojo;

public class MaintenanceRecord {

    private Long mid;
    private String mdate;
    private String items;
    private Long price;
    private String notes;



    private Long vid;

    public MaintenanceRecord(Long mid, String mdate, String items, Long price, String notes, Long vid) {
        this.mid = mid;
        this.mdate = mdate;
        this.items = items;
        this.price = price;
        this.notes = notes;
        this.vid = vid;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }


    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "mid=" + mid +
                ", mdate='" + mdate + '\'' +
                ", items='" + items + '\'' +
                ", price=" + price +
                ", notes='" + notes + '\'' +
                ", vid=" + vid +
                '}';
    }
}
