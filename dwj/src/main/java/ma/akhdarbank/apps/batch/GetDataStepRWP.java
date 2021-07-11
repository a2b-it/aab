package ma.akhdarbank.apps.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ma.akhdarbank.apps.model.TierBatch;


@Component
public class GetDataStepRWP {

	
	@Bean
    public ItemReader<TierBatch> getDataReader() {
		// from rest
		return new RESTTierBatchResponseReader();
 
    }
	
	@Bean
    public ItemWriter<TierBatch> getDatawriter() {
		// to database
		return null;
 
    }
	
	@Bean
    public ItemProcessor<TierBatch,Long> getDataProcessor() {
		// 
		return null;
 
    }
}
