package nl.stokpop.afterburner.config;

import com.github.gavlyukovskiy.boot.jdbc.decorator.p6spy.P6SpyDataSourceDecorator;
import lombok.Value;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.DatabaseStartupValidator;

import javax.sql.DataSource;

@Value
@ConfigurationProperties(prefix="afterburner.mybatis.datasource")
@ConstructorBinding
public class AfterburnerMyBatisConfig {

    String url;
    String username;
    String password;
    String driverClassName;

    //@Bean(name = "dataSourceMyBatis")
    // There is an issue "cannot find symbol method value()" when using Qualifier injection in sqlSessionFactory()
    public DataSource dataSourceMyBatis(P6SpyDataSourceDecorator p6SpyDecorator) {
        DataSource datasource = DataSourceBuilder.create()
            .driverClassName(driverClassName)
            .url(url)
            .username(username)
            .password(password)
            .build();
        return p6SpyDecorator.decorate("dataSourceMyBatis", datasource);
    }

    @Bean(name = "sqlSessionMyBatis")
    public SqlSessionFactory sqlSessionFactory(P6SpyDataSourceDecorator p6SpyDecorator) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceMyBatis(p6SpyDecorator));
        return factoryBean.getObject();
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.setDefaultFetchSize(10);
    }

    @Bean
    public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
        DatabaseStartupValidator dsv = new DatabaseStartupValidator();
        dsv.setDataSource(dataSource);
        dsv.setValidationQuery(DatabaseDriver.MARIADB.getValidationQuery());
        return dsv;
    }
}
