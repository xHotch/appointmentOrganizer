package com.hotch.service;

import com.hotch.entities.User;
import com.hotch.service.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {


    /**
     * Persists a user.
     * Has to make sure an id is assigned to the user
     *
     * @param user Entity holding the values for the new user
     * @return the saved user
     */
    User add(User user);

    /**
     * Returns a user with a given id
     *
     * @param id the identifier
     * @return the User
     * @throws UserNotFoundException if no user with the id was found
     */
    User show(int id) throws UserNotFoundException;

    /**
     *
     * @return A List containing all users
     */
    List<User> list();

    /**
     * Removes a user with a given id
     * Has to make sure, all appointments are edited too.
     *
     * @param id the identifier
     * @throws UserNotFoundException if no user with the id was found
     */
    void delete(int id) throws UserNotFoundException;

    /**
     * Edits a user if a user with given id is present, if not creates a new user instead
     * @param id the identifier
     * @param user Entity holding the updated values
     * @return the updated/created User
     */
    User edit(int id, User user);
}
