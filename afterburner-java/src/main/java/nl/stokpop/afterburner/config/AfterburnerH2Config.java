package nl.stokpop.afterburner.config;

import com.github.gavlyukovskiy.boot.jdbc.decorator.p6spy.P6SpyDataSourceDecorator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties("afterburner.datasource.basket")
public class AfterburnerH2Config extends HikariConfig {

    public HikariDataSource basketHikariDataSource() {
        return new HikariDataSource(this);
    }

    @Bean
    @Primary
    public DataSource dataSource(P6SpyDataSourceDecorator p6SpyDecorator) {
        return p6SpyDecorator.decorate("basketDataSource", basketHikariDataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
