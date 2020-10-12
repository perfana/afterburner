package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {
    @ApiOperation(value = "Welcome to afterburner.")
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String welcome() {
        return "<p>Welcome to afterburner, check the swagger docs: <a href = 'swagger-ui/'>swagger-ui/</a>";
    }
}
