package nl.stokpop.afterburner.stub;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping(value = "/delay", produces = MediaType.APPLICATION_JSON_VALUE)
    public String delay() {
        return "{ \"hello\":\"world\" }";
    }

}
