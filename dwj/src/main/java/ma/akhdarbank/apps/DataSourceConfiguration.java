/**
 * 
 */
package ma.akhdarbank.apps;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author a.bouabidi
 *
 */
@Configuration
@PropertySource({"file:./config/persistence-multiple-db-${spring.profiles.active}.properties"})
@EnableJpaRepositories(
	    basePackages = "ma.akhdarbank.apps.dao",
	    entityManagerFactoryRef = "domainEntityManager", 
	    transactionManagerRef = "domainTransactionManager"
	)
public class DataSourceConfiguration {	
	@Autowired
    private Environment env;

    @Bean(name = "domainDataSource")   
    @ConfigurationProperties("spring.domain.datasource")
    public DataSource domainDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean
    @Primary
    @ConfigurationProperties("spring.domain.datasource")
    public LocalContainerEntityManagerFactoryBean domainEntityManager() {
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(domainDataSource());
        em.setPackagesToScan(
          new String[] { "ma.akhdarbank.apps.dao","ma.akhdarbank.apps.model" });

        HibernateJpaVendorAdapter vendorAdapter
          = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
          env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
          env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        
        return em;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("domainDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("domainDataSource")DataSource ds) {
		return new NamedParameterJdbcTemplate(ds);
	}
}
