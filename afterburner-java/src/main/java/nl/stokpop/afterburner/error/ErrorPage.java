package nl.stokpop.afterburner.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorPage implements ErrorController {
	  static final String PATH = "/error";

	  @GetMapping(value = PATH)
	  public ResponseEntity<String> error() {
		  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>Not a valid Afterburner url</h2><p>Please try another url... or check the <a href = '/swagger-ui.html'>API documentation</a></p>");
	  }

}
