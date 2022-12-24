package com.example.driver_helper.pojo;

public class RefuelingRecord {

    private Long rid;
    private String rdate;
    private String gastype;
    private Double liter;
    private Long price;
    private String notes;
    private Long vid;

    public RefuelingRecord(Long rid, String rdate, String gastype, Double liter, Long price, String notes, Long vid) {
        this.rid = rid;
        this.rdate = rdate;
        this.gastype = gastype;
        this.liter = liter;
        this.price = price;
        this.notes = notes;
        this.vid = vid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getGastype() {
        return gastype;
    }

    public void setGastype(String gastype) {
        this.gastype = gastype;
    }

    public Double getLiter() {
        return liter;
    }

    public void setLiter(Double liter) {
        this.liter = liter;
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
        return "RefuelingRecord{" +
                "rid=" + rid +
                ", rdate='" + rdate + '\'' +
                ", gastype='" + gastype + '\'' +
                ", liter=" + liter +
                ", price=" + price +
                ", notes='" + notes + '\'' +
                ", vid=" + vid +
                '}';
    }
}
