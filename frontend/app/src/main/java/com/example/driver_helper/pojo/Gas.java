package com.example.driver_helper.pojo;

public class Gas {

    private String Type;
    private String Price;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    @Override
    public String toString() {
        return "Gas{" +
                "Type='" + Type + '\'' +
                ", Price='" + Price + '\'' +
                '}';
    }
}
