package dev.phenom.JournalApp.Service;

import dev.phenom.JournalApp.Entity.User;
import dev.phenom.JournalApp.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password");
        userService.saveUser(user);
        assertNotNull(userRepository.findByUserName("testUser"));
    }

    @Test
    public void testSaveNewUser() {
        User user = new User();
        user.setUserName("newUser");
        user.setPassword("password");
        userService.saveNewUser(user);
        assertNotNull(userRepository.findByUserName("newUser"));
        assertTrue(userRepository.findByUserName("newUser").getRoles().contains("USER"));
    }

    @Test
    public void testSaveNewAdminUser() {
        User user = new User();
        user.setUserName("adminUser");
        user.setPassword("password");
        userService.saveNewAdminUser(user);
        assertNotNull(userRepository.findByUserName("adminUser"));
        assertTrue(userRepository.findByUserName("adminUser").getRoles().contains("ADMIN"));
    }

    @Test
    public void testGetAll() {
        List<User> users = userService.getAll();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testGetByUserName() {
        assertNotNull(userRepository.findByUserName("devansh"));
        assertFalse(userRepository.findByUserName("devansh").getJournalEntryList().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1,3,4",
            "3,3,6",
            "3,3,6"
    })
    public void test(int a, int b, int c) {
        assertEquals(c, a+b);
    }
}
