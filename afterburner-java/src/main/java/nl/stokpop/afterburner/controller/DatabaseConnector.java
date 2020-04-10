package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseConnector {

    private final JdbcTemplate template;

    private final AfterburnerProperties props;

    @Autowired
    public DatabaseConnector(JdbcTemplate template, AfterburnerProperties props) {
        this.template = template;
        this.props = props;
    }

    @ApiOperation(value = "Execute trivial query on remote database and measure the response time.")
    @GetMapping("/db/connect")
    public BurnerMessage checkDatabaseConnection() {
        long startTime = System.currentTimeMillis();

        String query = props.getDatabaseConnectQuery();

        long nanoStartTime = System.nanoTime();
        template.execute(query);
        long estimatedQueryTime = System.nanoTime() - nanoStartTime;

        long durationMillis = System.currentTimeMillis() - startTime;

        String message = String.format("{ 'db-call':'success','query-duration-nanos':%d }", estimatedQueryTime);

        return new BurnerMessage(message, props.getAfterburnerName(), durationMillis);
    }

}
