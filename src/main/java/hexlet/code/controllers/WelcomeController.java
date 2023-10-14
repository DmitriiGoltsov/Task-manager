package hexlet.code.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class WelcomeController {

    @GetMapping(path = "/welcome")
    public String getGreeting() {
        return "Welcome to Spring";
    }
}
