package dev.phenom.JournalApp.controller;

import dev.phenom.JournalApp.Entity.User;
import dev.phenom.JournalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> GetAllUsers()
    {
        List<User> All=userService.getAll();
        if (!All.isEmpty() && All != null)
        {
            return new ResponseEntity<>(All, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public void createUser(@RequestBody User user){
        userService.saveNewAdminUser(user);
    }
}
