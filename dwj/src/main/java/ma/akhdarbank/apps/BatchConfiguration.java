/**
 * 
 */
package ma.akhdarbank.apps;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author a.bouabidi
 *
 */

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration extends DefaultBatchConfigurer  {
	@Override
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource batchDataSource) {
        super.setDataSource(batchDataSource);
    }

    @Bean
    public BatchDataSourceInitializer batchDataSourceInitializer(@Qualifier("dataSource") DataSource batchDataSource,
            ResourceLoader resourceLoader) {
        return new BatchDataSourceInitializer(batchDataSource, resourceLoader, new BatchProperties());
    }
    
    
    
    
}
