package nl.stokpop.afterburner.config;

import com.github.gavlyukovskiy.boot.jdbc.decorator.p6spy.P6SpyDataSourceDecorator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "nl.stokpop.afterburner.mybatis", sqlSessionFactoryRef="sqlSessionMyBatis")
@ConfigurationProperties("afterburner.datasource.employee")
@Profile("employee-db")
public class AfterburnerMyBatisConfig extends HikariConfig {

    private HikariDataSource dbConnectionPoolMyBatis() {
        return new HikariDataSource(this);
    }

    @Bean(name = "dataSourceMyBatis")
    public DataSource dataSourceMyBatis(P6SpyDataSourceDecorator p6SpyDecorator) {
        return p6SpyDecorator.decorate("dataSourceMyBatis", dbConnectionPoolMyBatis());
    }

    @Bean(name = "sqlSessionMyBatis")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceMyBatis") DataSource dataSourceMyBatis) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceMyBatis);
        return factoryBean.getObject();
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.setDefaultFetchSize(10);
    }

}
