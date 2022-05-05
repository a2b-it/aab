/**
 * 
 */
package ma.ab.banking.app.config;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import oracle.jms.AQjmsFactory;

/**
 * @author a.bouabidi
 *
 */
@Configuration
@Slf4j
@Getter
@Setter
public class OracleAQConfiguration {
	@Autowired
	SqlSessionFactory sessionFactory;

	@Value("${aqapi.datasource.user}")
	private String user;

	@Value("${aqapi.datasource.password}")
	private String password;

	@Value("${aqapi.datasource.url}")
	private String url;

	@Autowired
	SampleJmsErrorHandler sampleJmsErrorHandler;
	
	@Bean
	public DefaultJmsListenerContainerFactory aqApiFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message
		// converter
		log.debug("============================================= aqApiFactory");
		factory.setErrorHandler(sampleJmsErrorHandler);
		configurer.configure(factory, connectionFactory);
		
		// You could still override some of Boot's default if necessary.
		return factory;
	}

	@Bean
	public QueueConnectionFactory connectionFactory() throws Exception {
		
		log.debug("============================================= user {}" , getUser());
		log.debug("============================================= pass {}", getPassword());
		log.debug("============================================= url {}", getUrl());
		Properties info = new Properties();
		info.put("driver-name", "oracle.jdbc.OracleDriver");
		info.put("user", user);
		info.put("password", password);
		return AQjmsFactory.getQueueConnectionFactory(url, info);
	}

	@Bean
	public JmsTemplate jmsTemplate() throws Exception {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory());
		return jmsTemplate;
	}
}
