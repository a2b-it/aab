/**
 * 
 */
package ma.akhdarbank.apps;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * @author a.bouabidi
 *
 */
@Configuration
public class BatchConfiguration extends DefaultBatchConfigurer  {
	@Override
    @Autowired
    public void setDataSource(@Qualifier("batchDataSource") DataSource batchDataSource) {
        super.setDataSource(batchDataSource);
    }

    @Bean
    public BatchDataSourceInitializer batchDataSourceInitializer(@Qualifier("batchDataSource") DataSource batchDataSource,
            ResourceLoader resourceLoader) {
        return new BatchDataSourceInitializer(batchDataSource, resourceLoader, new BatchProperties());
    }
}
