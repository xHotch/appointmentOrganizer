package com.hotch.service.implementation;

import com.hotch.entities.Appointment;
import com.hotch.entities.User;
import com.hotch.service.exceptions.UserNotFoundException;
import com.hotch.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Simple User service, does not call a persistence layer to persist data, only stores it in a simple HashMap
 */
@Service
public class SimpleUserService implements UserService {

    private HashMap<Integer,User> userList = new HashMap<>();
    private int ids = 0;

    @Override
    public User add(User user) {
        while (userList.containsKey(ids)){
            ids++;
        }
        user.setId(ids);
        userList.put(user.getId(),user);
        return user;
    }

    @Override
    public User show(int id) throws UserNotFoundException {
        User lookedUpUser = userList.get(id);
        if (lookedUpUser == null) {
            throw new UserNotFoundException("No user found with id" + id);
        }
        return lookedUpUser;
    }

    @Override
    public List<User> list() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public void delete(int id) throws UserNotFoundException{
        User lookedUpUser = userList.get(id);
        if (lookedUpUser == null) {
            throw new UserNotFoundException("No user found with id" + id);
        }
        for (Appointment appointment: lookedUpUser.getAppointmentList()){
            appointment.getUserList().remove(lookedUpUser);
        }
        userList.remove(id);
    }


    @Override
    public User edit(int id, User editedUser) {
        User lookedUpUser = userList.get(id);
        if (lookedUpUser == null) {
            editedUser.setId(id);
            userList.put(id,editedUser);
            return editedUser;
        }

        lookedUpUser.setName(editedUser.getName());
        return lookedUpUser;
    }
}
