package com.example.eliavmenachi.simplelist.model;

public class Employee {
    String id;
    String name;
    String address;
    String imageName;
    String phone;
    String department;
    boolean isAtWork;

    public Employee(String id,
                    String name,
                    String address,
                    String imageName,
                    String phone,
                    String department,
                    boolean isAtWork) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imageName = imageName;
        this.phone = phone;
        this.department = department;
        this.isAtWork = isAtWork;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getImageName() {
        return imageName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public boolean isAtWork() {
        return isAtWork;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setAtWork(boolean isAtWork) {
        this.isAtWork = isAtWork;
    }
}
