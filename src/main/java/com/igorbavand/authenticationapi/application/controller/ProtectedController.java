package com.igorbavand.authenticationapi.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/protected")
public class ProtectedController {

    @GetMapping
    public String protectedMethod() {
        return "Protected works";
    }
}
