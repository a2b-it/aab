/**
 * 
 */
package ma.aab.svi.app.config;

import java.sql.SQLException;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.sql.DataSource;
import ma.aab.svi.app.listeners.Listener;
import oracle.jdbc.datasource.impl.OracleDataSource;
import oracle.jms.AQjmsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */

@Configuration
@ConfigurationProperties("spring.oracle.aq.datasource")
@Getter
@Setter
public class OracleAQConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(OracleAQConfiguration.class);
	public static String QUEUE_NAME = "ETHIX_T_Q";
	private String username;
	private String password;
	private String jdbcUrl;

	@Bean(name = { "aqDataSource" })
	public DataSource aqDataSource() {
		OracleDataSource dataSource = null;
		try {
			log.debug("USER {}", this.username);
			log.debug("PASSWORD {}", this.password);
			log.debug("URL {}", this.jdbcUrl);
			dataSource = new OracleDataSource();
			dataSource.setUser(this.username);
			dataSource.setPassword(this.password);
			dataSource.setURL(this.jdbcUrl);

			dataSource.setImplicitCachingEnabled(true);
		} catch (SQLException localSQLException) {
		}
		return dataSource;
	}

	@Bean
	public MessageListenerContainer listenerContainer(ConnectionFactory connectionFactory) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName(QUEUE_NAME);
		container.setMessageListener(new Listener());
		return container;
	}

	@Bean
	public ConnectionFactory connectionFactory(DataSource aqDataSource) {
		QueueConnectionFactory qf = null;
		try {
			log.debug("============================================= ");
			qf = AQjmsFactory.getQueueConnectionFactory(aqDataSource);
		} catch (JMSException e) {
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

		return jmsTemplate;
	}

}
