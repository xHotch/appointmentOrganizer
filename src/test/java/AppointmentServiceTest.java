import com.hotch.entities.Appointment;
import com.hotch.entities.User;
import com.hotch.service.AppointmentService;
import com.hotch.service.UserService;
import com.hotch.service.exceptions.OverlappingDateException;
import com.hotch.service.exceptions.UserNotFoundException;
import com.hotch.service.implementation.SimpleAppointmentService;
import com.hotch.service.implementation.SimpleUserService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;


public class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private UserService userService;
    private User testUser;
    private Appointment testAppointment;
    private Appointment testAppointment2;

    @Before
    public void setUp() {

        userService = new SimpleUserService();
        appointmentService = new SimpleAppointmentService(userService);

        testAppointment = new Appointment();
        testAppointment.setTitle("Test Appointment");
        testAppointment.setStartDate(LocalDateTime.MIN);
        testAppointment.setEndDate(LocalDateTime.now());


        testAppointment2 = new Appointment();
        testAppointment2.setTitle("Test Appointment");
        testAppointment2.setStartDate(LocalDateTime.MIN);
        testAppointment2.setEndDate(LocalDateTime.MAX);


        testUser = new User();
        testUser.setName("Peter");
        userService.add(testUser);

    }

    @Test
    public void testAddAppointment() throws OverlappingDateException, UserNotFoundException {
        testAppointment.getUserList().add(testUser);

        appointmentService.add(testAppointment);
    }

    @Test(expected = OverlappingDateException.class)
    public void testAddAppointmentThrowsOverlapException() throws OverlappingDateException, UserNotFoundException {
        testAppointment.getUserList().add(testUser);
        appointmentService.add(testAppointment);

        testAppointment2.getUserList().add(testUser);
        appointmentService.add(testAppointment2);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAddAppointmentThrowsUserNotFoundException() throws OverlappingDateException, UserNotFoundException {
        User newUser = new User();
        try {
            userService.delete(newUser.getId());
        } catch (UserNotFoundException e) {

        }

        testAppointment.getUserList().add(newUser);
        appointmentService.add(testAppointment);

    }


}
