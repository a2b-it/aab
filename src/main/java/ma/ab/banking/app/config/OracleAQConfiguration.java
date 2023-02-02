/**
 * 
 */
package ma.ab.banking.app.config;

import java.sql.SQLException;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import ma.ab.banking.app.jms.EthixTranListener;
import oracle.jdbc.datasource.impl.OracleDataSource;
import oracle.jms.AQjmsFactory;

/**
 * @author a.bouabidi
 *
 */
@Configuration
@EnableTransactionManagement
@Slf4j
@Getter
@Setter
public class OracleAQConfiguration {

	public static String QUEUE_NAME = "ETHIX_T_Q";

	
	  @Value("${aqapi.datasource.user}") private String user;
	  
	  @Value("${aqapi.datasource.password}") private String password;
	  
	  @Value("${aqapi.datasource.url}") private String url;
	 
	@Autowired
	SampleJmsErrorHandler sampleJmsErrorHandler;

	@Bean(name = "aqDataSource")
	@ConfigurationProperties("spring.oracle.aq.datasource")
	public DataSource aqDataSource() {
		OracleDataSource dataSource = null;
		try {
			dataSource = new OracleDataSource();
			dataSource.setUser(user);
			dataSource.setPassword(password);
			dataSource.setURL(url);
			// dataSource.setFastConnectionFailoverEnabled(true);
			dataSource.setImplicitCachingEnabled(true);
			// dataSource.setConnectionCachingEnabled(true);
		} catch (SQLException e) {

		}
		return dataSource;
	}

	 @Bean
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(connectionFactory);
        return jmsTransactionManager;
    }
	 
	@Bean
	public MessageListenerContainer listenerContainer(ConnectionFactory connectionFactory, EthixTranListener ethixTranListener) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName(QUEUE_NAME);
		container.setMessageListener(ethixTranListener);
		return container;
	}

	
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setConcurrency("1-1");
		factory.setPubSubDomain(true);
		factory.setMessageConverter(jacksonJmsMessageConverter());
		return factory;
	}
	 
	

	@Bean
	public ConnectionFactory connectionFactory(DataSource aqDataSource) {
		QueueConnectionFactory qf = null;
		/*
		 * log.debug("============================================= user {}",
		 * getUser());
		 * log.debug("============================================= pass {}",
		 * getPassword());
		 * log.debug("============================================= url {}", getUrl());
		 * Properties info = new Properties(); info.put("driver-name",
		 * "oracle.jdbc.OracleDriver"); info.put("user", user); info.put("password",
		 * password);
		 */
		try {
			log.debug("============================================= ");
			qf = AQjmsFactory.getQueueConnectionFactory(aqDataSource);

			/*
			 * qf = AQjmsFactory.getQueueConnectionFactory("localhost", "xe", 49161,
			 * "thin");
			 */

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qf;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	@Bean
	public JmsTemplate jmsAqApiTemplate(ConnectionFactory connectionFactory) throws Exception {
		JmsTemplate jmsTemplate = new JmsTemplate();

		jmsTemplate.setConnectionFactory(connectionFactory);
		
		// jmsTemplate.get
		return jmsTemplate;
	}
}
