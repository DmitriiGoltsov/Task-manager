package hexlet.code.app.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Welcome controller")
@RequestMapping(path = "/welcome")
public class WelcomeController {

    @ApiResponse(responseCode = "200", description = "Welcome page has been rendered")
    @GetMapping(path = "")
    public String getGreeting() {
        return "Welcome to Spring";
    }
}
