package com.example.springbootauthentication.springbootauthenticationimplementation.controller;

import com.example.springbootauthentication.springbootauthenticationimplementation.entity.AuthRequest;
import com.example.springbootauthentication.springbootauthenticationimplementation.entity.UserInfo;
import com.example.springbootauthentication.springbootauthenticationimplementation.service.JwtService;
import com.example.springbootauthentication.springbootauthenticationimplementation.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        System.out.println("1111111111111111111111111111111");
        if (authentication.isAuthenticated()) {
            System.out.println("222222222222222222222222222222");
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            System.out.println("333333333333333333333333333333333333333333333333");
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
