package nl.stokpop.afterburner.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorPage implements ErrorController {
	  private static final String PATH = "/error";

	  @RequestMapping(value = PATH)
	  public ResponseEntity<String> error() {
		  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>Afterburner failed.</h2><p>Please try another url...</p>");
	  }

	  @Override
	  public String getErrorPath() {
		  return PATH;
	  }
}
