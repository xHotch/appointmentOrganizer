import com.hotch.entities.User;
import com.hotch.service.UserService;
import com.hotch.service.exceptions.UserNotFoundException;
import com.hotch.service.implementation.SimpleUserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserServiceTest {

    private UserService userService;
    private User testUser;
    private User testUser2;

    @Before
    public void setUp(){
        userService= new SimpleUserService();
        testUser = new User();
        testUser.setName("Peter");
        testUser2 = new User();
        testUser2.setName("Ines");
    }

    @Test
    public void testAddUser(){
        User user1 = userService.add(testUser);
        User user2 = userService.add(testUser2);

        Assert.assertTrue(user2.getId()>user1.getId());
    }

    @Test
    public void testListUsers(){
        int lengthBefore = userService.list().size();
        userService.add(testUser);
        userService.add(testUser2);

        int lengthAfter = userService.list().size();

        Assert.assertEquals(lengthBefore+2,lengthAfter);
    }

    @Test(expected = UserNotFoundException.class)
    public void showThrowsException() throws UserNotFoundException {
        int id = 3;
        try {
            userService.delete(id);
        } catch (UserNotFoundException e){
            //Catch Exception if it occurs on delete
        }
        userService.show(id);
    }

}
