/**
 * 
 */
package com.apiweather.app.flow;

import org.springframework.context.annotation.Configuration;

/**
 * @author a.bouabidi
 *
 */
@Configuration
public class FtpConfiguration {
	/*
	@ServiceActivator(inputChannel = "ftpLS")
	@Bean
	public FtpOutboundGateway getGW() {
		FtpOutboundGateway gateway = new FtpOutboundGateway(sf(), "ls", "payload");
		gateway.setOption(Option.NAME_ONLY);
		gateway.setOutputChannelName("results");
		return gateway;
	}

	@Bean
	public MessageChannel results() {
		DirectChannel channel = new DirectChannel();
		channel.addInterceptor(tap());
		return channel;
	}

	@Bean
	public WireTap tap() {
		return new WireTap("logging");
	}

	@ServiceActivator(inputChannel = "logging")
	@Bean
	public LoggingHandler logger() {
		LoggingHandler logger = new LoggingHandler(Level.INFO);
		logger.setLogExpressionString("'Files:' + payload");
		return logger;
	}

	@Bean
	public DefaultFtpSessionFactory sf() {
		DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
		sf.setHost("localhost");
		sf.setPort(2121);
		sf.setUsername("admin");
		sf.setPassword("admin");
		return sf;
	}

	
	/*
	public MessageHandler handler() {
		
	}*/
	/*@Bean
	FtpRemoteFileTemplate ftpRemoteFileTemplate(DefaultFtpSessionFactory sf_1) {
		return new FtpRemoteFileTemplate(dsf);
	}
	
	
    @Bean(name="sf_1")    
	public DefaultFtpSessionFactory sf_1(@Value("${ftp1.username}") String username, @Value("${ftp1.password}") String pw,
			@Value("${ftp1.host}") String host, @Value("${ftp1.port}") int port) {
    	DefaultFtpSessionFactory sf = createSessionFactory(username, pw,host,port);
		return sf;
	}
    
    @Bean(name="sf_2")    
	public DefaultFtpSessionFactory sf_2(@Value("${ftp2.username}") String username, @Value("${ftp2.password}") String pw,
			@Value("${ftp2.host}") String host, @Value("${ftp2.port}") int port) {
		DefaultFtpSessionFactory sf = createSessionFactory(username, pw,host,port);
		return sf;
	}
    
    private DefaultFtpSessionFactory createSessionFactory(String username, String pw, String host, int port) {
		var defaultFtpSessionFactory = new DefaultFtpSessionFactory();
		defaultFtpSessionFactory.setPassword(pw);
		defaultFtpSessionFactory.setUsername(username);
		defaultFtpSessionFactory.setHost(host);
		defaultFtpSessionFactory.setPort(port);
		return defaultFtpSessionFactory;
    }
     */  
}
