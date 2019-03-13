package com.hotch.entities;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String name;
    private List<Appointment> appointmentList;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Appointment> getAppointmentList() {
        if (appointmentList==null){
            appointmentList = new ArrayList<>();
        }
        return appointmentList;
    }

}
