package nl.stokpop.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {
    @Operation(summary = "Welcome to afterburner.")
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String welcome() {
        return "<p>Welcome to Afterburner! <a href = '/swagger-ui.html'>API documentation</a>";
    }
}
