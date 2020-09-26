package nl.stokpop.afterburner.config;

import lombok.Value;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@ConfigurationProperties(prefix="afterburner.mybatis.datasource")
@ConstructorBinding
@MapperScan(value = "nl.stokpop.afterburner.mybatis", sqlSessionFactoryRef="sqlSessionMyBatis")
@Value
public class AfterburnerMyBatisConfig {

    String url;
    String username;
    String password;
    String driverClassName;

    //@Bean(name = "dataSourceMyBatis")
    // There is an issue "cannot find symbol method value()" when using Qualifier injection in sqlSessionFactory()
    public DataSource dataSourceMyBatis() {
        return DataSourceBuilder.create()
            .driverClassName(driverClassName)
            .url(url)
            .username(username)
            .password(password)
            .build();
    }

    @Bean(name = "sqlSessionMyBatis")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceMyBatis());
        return factoryBean.getObject();
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.setDefaultFetchSize(10);
    }
}
