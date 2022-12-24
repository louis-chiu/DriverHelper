package com.example.driver_helper.pojo;

public class Vehicle {

    private Long id;
    private String name;
    private String mfd;
    private Long mileage;
    private String type;
    private String brand;
    private String model;
    private int img;

    public Vehicle(Long id, String name, String mfd, Long mileage, String type, String brand, String model) {
        this.id = id;
        this.name = name;
        this.mfd = mfd;
        this.mileage = mileage;
        this.type = type;
        this.brand = brand;
        this.model = model;
    }

    public Vehicle(String name, String type) {
        this.name = name;
        this.type = type;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMfd() {
        return mfd;
    }

    public void setMfd(String mfd) {
        this.mfd = mfd;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mfd='" + mfd + '\'' +
                ", mileage=" + mileage +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                '}';
    }


}

