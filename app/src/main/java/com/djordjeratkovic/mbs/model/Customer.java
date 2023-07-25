package com.djordjeratkovic.mbs.model;

public class Customer {
    private String name;
    private long PIB;
    private String docRef;
    private String employeeDocRef;

    public Customer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPIB() {
        return PIB;
    }

    public void setPIB(long PIB) {
        this.PIB = PIB;
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public String getEmployeeDocRef() {
        return employeeDocRef;
    }

    public void setEmployeeDocRef(String employeeDocRef) {
        this.employeeDocRef = employeeDocRef;
    }
}
