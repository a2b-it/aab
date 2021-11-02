/**
 * 
 */
package ma.akhdarbank.apps;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author a.bouabidi
 *
 */
@Configuration
public class DataSourceConfiguration {
	@Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource domainDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("batchDataSource")
    @ConfigurationProperties("spring.batch.datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }
}
