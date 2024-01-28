package com.danillkucheruk.notes.controllers.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class MainController {
    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}
