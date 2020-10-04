package ru.abtank.springsecurity.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.abtank.springsecurity.entities.User;
import ru.abtank.springsecurity.servises.UserService;

import java.security.Principal;

@RestController
public class MainController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String homePage(Principal principal) {
        if (principal != null) {
            System.out.println(((Authentication) principal).getAuthorities());
        }
        return "home";
    }

    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers(Principal principal) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(principal.getName());
        return "secured path of web service: " + user.getUsername() + " " + user.getEmail();
    }

    @GetMapping("/read_profile")
    public String pageForReadProfile() {
        return "read_profile page";
    }

    @GetMapping("/only_for_admins")
    public String pageOnlyForAdmins() {
        return "only_for_admins page";
    }
}
