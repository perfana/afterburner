package nl.stokpop.afterburner.config;

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
    @ConfigurationProperties("app.datasource.member")
    public DataSourceProperties memberDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.member.configuration")
    public DataSource memberDataSource() {
        return memberDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

//    @Bean(name = "dataSourceH2")
//    public DataSource dataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("org.mariadb.jdbc.Driver");
//        dataSourceBuilder.url("jdbc:mariadb://localhost:3306/employees");
//        dataSourceBuilder.username("root");
//        dataSourceBuilder.password("mypass");
//        return dataSourceBuilder.build();
//    }

}
