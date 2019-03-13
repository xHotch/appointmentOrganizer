package com.hotch.service.implementation;

import com.hotch.entities.Appointment;
import com.hotch.entities.User;
import com.hotch.service.AppointmentService;
import com.hotch.service.UserService;
import com.hotch.service.exceptions.AppointmentNotFoundException;
import com.hotch.service.exceptions.OverlappingDateException;
import com.hotch.service.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Simple Appointment service, does not call a persistence layer to persist data, only stores it in a simple HashMap
 */
@Service
public class SimpleAppointmentService implements AppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private UserService userService;

    public SimpleAppointmentService(UserService userService) {
        this.userService = userService;
    }

    private HashMap<Integer, Appointment> appointments = new HashMap<>();
    private int ids = 0;


    @Override
    public Appointment add(Appointment appointment) throws OverlappingDateException, UserNotFoundException {

        while (appointments.containsKey(ids)) {
            ids++;
        }
        return saveAppointment(appointment, ids);
    }

    /**
     * Method to save a new Appointment
     *
     * @param appointment The appointment that is tested saved
     * @param id          The appointments identifier
     * @throws OverlappingDateException if an overlap occurs
     * @throws UserNotFoundException    if there was an invalid user
     */
    private Appointment saveAppointment(Appointment appointment, int id) throws OverlappingDateException, UserNotFoundException {
        checkDateCollision(appointment);
        appointment.setId(id);
        appointments.put(appointment.getId(), appointment);

        for (User user : appointment.getUserList()) {
            user.getAppointmentList().add(appointment);
        }
        return appointment;
    }

    @Override
    public List<Appointment> list() {
        return new ArrayList<>(appointments.values());
    }

    @Override
    public List<Appointment> show(int userId) throws UserNotFoundException {
        return userService.show(userId).getAppointmentList();
    }


    @Override
    public void delete(int id) throws AppointmentNotFoundException {
        Appointment appointment = appointments.get(id);
        if (appointment == null) {
            throw new AppointmentNotFoundException("No Appointment found with id" + id);
        }
        for (User user : appointment.getUserList()) {
            user.getAppointmentList().remove(appointment);
        }
        appointments.remove(id);
    }

    @Override
    public Appointment edit(int id, Appointment editedAppointment) throws OverlappingDateException, UserNotFoundException {
        Appointment lookedUpAppointment = appointments.get(id);

        //create new Appointment if no Appointment with the id is found
        if (lookedUpAppointment == null) {
            return saveAppointment(editedAppointment, id);
        }

        try {
            delete(lookedUpAppointment.getId());
            saveAppointment(editedAppointment, id);
        } catch (OverlappingDateException e) {
            //Overlap in new Appointment, restore old appointment
            saveAppointment(lookedUpAppointment, id);
        } catch (AppointmentNotFoundException e) {
            //should not appear
            LOG.error("Caught AppointmentNotFoundException ", e);
        }

        return lookedUpAppointment;
    }


    /**
     * Method to determine if any user has an overlap with the given appointment
     *
     * @param appointment The appointment that is tested
     * @throws OverlappingDateException if an overlap occurs
     * @throws UserNotFoundException    if there was an invalid user
     */
    private void checkDateCollision(Appointment appointment) throws OverlappingDateException, UserNotFoundException {

        LocalDateTime appointmentStart = appointment.getStartDate();
        LocalDateTime appointmentEnd = appointment.getEndDate();

        for (User user : appointment.getUserList()) {
            userService.show(user.getId());
            for (Appointment userAppointment : user.getAppointmentList()) {
                if (overlap(appointmentStart, appointmentEnd, userAppointment.getStartDate(), userAppointment.getEndDate())) {
                    throw new OverlappingDateException(String.format("Appointment %s is overlapping with Appointment %s", appointment.getId(), userAppointment.getId()));
                }
            }
        }

    }

    private boolean overlap(LocalDateTime startDate1, LocalDateTime endDate1, LocalDateTime startDate2, LocalDateTime endDate2) {
        return startDate1.isBefore(endDate2) && startDate2.isBefore(endDate1);
    }
}
