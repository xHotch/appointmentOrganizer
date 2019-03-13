package com.hotch.rest;

import com.hotch.entities.Appointment;
import com.hotch.service.AppointmentService;
import com.hotch.service.exceptions.AppointmentNotFoundException;
import com.hotch.service.exceptions.OverlappingDateException;
import com.hotch.service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AppointmentController {
    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments")
    @ResponseBody
    public Appointment add(@RequestBody Appointment newAppointment) {
        try {
            return appointmentService.add(newAppointment);
        } catch (OverlappingDateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add overlapping Appointments", e);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }

    }

    @PutMapping("appointments/{id}")
    @ResponseBody
    public Appointment replace(@RequestBody Appointment newUser, @PathVariable int id) {
        try {
            return appointmentService.edit(id, newUser);
        } catch (OverlappingDateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add overlapping Appointments", e);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @GetMapping("/appointments/{id}")
    @ResponseBody
    public List<Appointment> show(@PathVariable int id) {
        try {
            return appointmentService.show(id);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @GetMapping("/appointments")
    @ResponseBody
    public List<Appointment> list() {
        return appointmentService.list();
    }

    @DeleteMapping("/appointments/{id}")
    @ResponseBody
    public void delete(@PathVariable int id) {
        try {
            appointmentService.delete(id);
        } catch (AppointmentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find appointment to delete", e);
        }
    }
}
