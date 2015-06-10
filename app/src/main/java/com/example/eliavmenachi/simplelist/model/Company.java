package com.example.eliavmenachi.simplelist.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Company {
    String id;
    String name;
//    location
    List<Employee> employees;

    public Company(String id, String name) {
        this.id = id;
        this.name = name;
        this.employees = new LinkedList<Employee>();
    }

    public Company(String id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public String getId() { return this.id; }

    public String getName() { return this.name; }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public List<Employee> getEmployees() { return employees; }
}
