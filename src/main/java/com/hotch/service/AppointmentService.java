package com.hotch.service;

import com.hotch.entities.Appointment;
import com.hotch.service.exceptions.AppointmentNotFoundException;
import com.hotch.service.exceptions.OverlappingDateException;
import com.hotch.service.exceptions.UserNotFoundException;

import java.util.List;

public interface AppointmentService {

    /**
     * Persists an Appointment.
     * Has to make sure an id is assigned to the Appointment
     *
     * @param appointment Entity holding the values for the new appointment
     * @return the saved Appointment
     */
    Appointment add(Appointment appointment) throws OverlappingDateException, UserNotFoundException;

    /**
     * @return A List containing all Appointments
     */
    List<Appointment> list();

    /**
     * Returns all Appointments with a given User id
     *
     * @param id the identifier
     * @return List of all Appointments from the user
     * @throws UserNotFoundException if no user with the id was found
     */
    List<Appointment> show(int id) throws UserNotFoundException;

    /**
     * Removes an Appointment with a given id
     * Has to make sure, all appointments are edited too.
     *
     * @param id the identifier
     * @throws AppointmentNotFoundException if no Appointment with the id was found
     */
    void delete(int id) throws AppointmentNotFoundException;

    /**
     * Edits a user if a appointment with given id is present, if not creates a new appointment instead
     *
     * @param id          the identifier of the old appointment
     * @param appointment Entity holding the updated values
     * @return the updated/created User
     */
    Appointment edit(int id, Appointment appointment) throws OverlappingDateException, UserNotFoundException;
}
