package dev.phenom.JournalApp.controller;

import dev.phenom.JournalApp.Entity.User;
import dev.phenom.JournalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @GetMapping("/health-check")
    public String Healthcheck() {
        return "Ok";
    }
    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        userService.saveNewUser(user);
    }
}
