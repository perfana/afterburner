package nl.stokpop.afterburner.config;

import com.github.gavlyukovskiy.boot.jdbc.decorator.p6spy.P6SpyDataSourceDecorator;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "nl.stokpop.afterburner.mybatis", sqlSessionFactoryRef="sqlSessionMyBatis")
public class AfterburnerH2Config {

    @Bean
    @Primary
    @ConfigurationProperties("afterburner.basket")
    public DataSourceProperties memberDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("afterburner.basket.h2")
    public DataSource memberDataSource(P6SpyDataSourceDecorator p6SpyDecorator) {
        HikariDataSource hikariDataSource = memberDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
        return p6SpyDecorator.decorate("basketH2Db", hikariDataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

}
