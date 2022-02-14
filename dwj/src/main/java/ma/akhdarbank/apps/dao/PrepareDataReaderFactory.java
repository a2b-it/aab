/**
 * 
 */
package ma.akhdarbank.apps.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.akhdarbank.apps.model.Tiers;

/**
 * @author a.bouabidi
 *
 */

@Configuration
public class PrepareDataReaderFactory {
	
	@Autowired
	@Qualifier("domainDataSource")
	private DataSource dataSource;

	
	@Bean
	public JdbcPagingItemReader<Tiers> prepareDataReader() throws Exception {
		//
		
		Map<String, Object> parameterValues = new HashMap<>();
		//parameterValues.put("status", "NEW");
		//
		PagingQueryProvider queryProvider= (PagingQueryProvider) queryProvider();		
		JdbcPagingItemReader<Tiers> jdbcReader = new JdbcPagingItemReaderBuilder<Tiers>()
   				.name("tiersReader")
   				.dataSource(this.dataSource)
   				.queryProvider(queryProvider)
   				.parameterValues(parameterValues)
   				.rowMapper(new TierRowMapper ())
   				.pageSize(1000)
   				.build(); 
		return jdbcReader;
	}
	
	public PagingQueryProvider queryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
		provider.setSelectClause("select IDENTIFIANT,TYPEPERSONNE,NOM,PRENOM,RAISONSOCIALE,ETX_RIM ");
		provider.setFromClause("from tiers");
		//provider.setWhereClause("where status=:status");
		provider.setSortKey("IDENTIFIANT");
		provider.setDataSource(dataSource);
		return provider.getObject();
	}
}
