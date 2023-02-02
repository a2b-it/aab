/*
 *
 *   Copyright (c) 1999 - 2011 my-Channels Ltd
 *   Copyright (c) 2012 - 2017 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 *   Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 *
 */

package gsimt.mom.samples;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class JMSClient implements ExceptionListener {

  public static final String ENV_USERNAME = "umdemo_username";
  public static final String ENV_PASSWORD = "umdemo_password";
  public static final String ENV_ALTJMSCREDS = "umdemo_altjms";

  public static final String AMQP_CONNECTION_FACTORY_PREFIX = "connectionfactory.";
  public static final String AMQP_TOPIC_PREFIX = "topic.";
  public static final String AMQP_QUEUE_PREFIX = "queue.";
  public static final String AMQP_PROTOCOL_PREFIX = "amqp";

  public static final String NIRVANA_CONTEXT_FACTORY = "com.pcbsys.nirvana.nSpace.NirvanaContextFactory";
  public static final String NIRVANA_DEFAULT_URL = "nsp://127.0.0.1:9000";
  public static final String SIMPLE = "simple";

  public static final String PROPERTY_CONTEXT_FACTORY = "CONTEXT_FACTORY";
  public static final String PROPERTY_PROVIDER_URL = "PROVIDER_URL";
  public static final String PROPERTY_RNAME = "RNAME";
  public static final String PROPERTY_PRINCIPAL = "PRINCIPAL";
  public static final String PROPERTY_PASSWORD = "PASSWORD";

  protected final String factoryName;
  protected final String destinationName;

  public JMSClient(String factoryName, String destinationName) {
    this.factoryName = factoryName;
    this.destinationName = destinationName;
  }

  protected static void UsageEnv() {
    System.out.println("\n\n(Environment Variables) \n");

    System.out.println("(RNAME) - One or more RNAME entries in the form protocol://host:port");
    System.out.println("   protocol - Can be one of nsp, nhp, nsps, or nhps, where:");
    System.out.println("   nsp - Specifies Nirvana Socket Protocol (nsp)");
    System.out.println("   nhp - Specifies Nirvana HTTP Protocol (nhp)");
    System.out.println("   nsps - Specifies Nirvana Socket Protocol Secure (nsps), i.e. using SSL/TLS");
    System.out.println("   nhps - Specifies Nirvana HTTP Protocol Secure (nhps), i.e. using SSL/TLS");
    System.out.println("   port - The port number of the server");
    System.out.println(
        "\nHint: - For multiple RNAME entries, use comma separated values which will be attempted in connection weight order\n");

    System.out.println(
        "(LOGLEVEL) - This determines how much information the nirvana api will output 0 = verbose 7 = quiet\n");

    System.out.println("(CKEYSTORE) - If using SSL, the location of the keystore containing the client cert\n");
    System.out.println("(CKEYSTOREPASSWD) - If using SSL, the password for the keystore containing the client cert\n");
    System.out.println("(CAKEYSTORE) - If using SSL, the location of the ca truststore\n");
    System.out.println("(CAKEYSTOREPASSWD) - If using SSL, the password for the ca truststore\n");

    System.out.println("(HPROXY) - HTTP Proxy details in the form proxyhost:proxyport, where:");
    System.out.println("   proxyhost - The HTTP proxy host");
    System.out.println("   proxyport - The HTTP proxy port\n");
    System.out.println("(HAUTH) - HTTP Proxy authentication details in the form user:pass, where:");
    System.out.println("   user - The HTTP proxy authentication username");
    System.out.println("   pass - The HTTP proxy authentication password\n\n");

    System.out.println(
        "(CONTEXT_FACTORY) - The name of the ContextFactory class to use (default: com.pcbsys.nirvana.nSpace.NirvanaContextFactory)");
    System.out.println("   When using an AMPQ connection this should be changed to:");
    System.out
        .println("   org.apache.qpid.jms.jndi.JmsInitialContextFactory - to use the QPID Proton JMS Client libraries");
    System.out.println(
        "   org.apache.qpid.amqp_1_0.jms.jndi.PropertiesFileInitialContextFactory - to use the QPID Legacy JMS Client libraries");
    System.out
        .println("Hint: AMPQ connections work only over nsp and nsps interfaces, they won't work over nhp or nhps\n");

    System.out.println(
        "(PROVIDER_URL) - The URL of your local nirvana realm from which JNDI entries will be looked up(default: nsp://localhost:9000)");
    System.out.println("   When using an AMPQ connection this should be changed to:");
    System.out.println(
        "   amqp://localhost:9000 - when using a plain AMPQ connection, connecting to a standard nsp interface");
    System.out.println(
        "   amqps://localhost:9000 - when using a plain AMPQ connection over a secure socket connection, connecting to a nsps interface");
    System.out
        .println("Hint: AMPQ connections work only over nsp and nsps interfaces, they won't work over nhp or nhps\n");

  }

  protected static void printFactoryNameUsage() {
    System.out.println(
        "<factoryname> - JMS Factory (Must exist in target realm). This argument is ignored when using AMPQ, but it is still required.");
  }

  protected static void printDestinationNameUsage() {
    System.out.println(
        "<destinationName> - JMS Destination to subscribe to. When using AMPQ this should be in the format topic.<topicName> or queue.<queueName>");
  }

  protected static void printPublishTopicNameUsage() {
    System.out.println(
        "<topicName> - JMS Topic to publish on. When using AMPQ this should be in the format topic.<topicName>");
  }

  /**
   * Get the initial context used to locate the factories and destinations
   *
   * @return the initial context based on the supplied PROVIDER_URL
   * @throws Exception if contex cannot be created
   */
  protected Context getInitialContext() throws Exception {
    Hashtable<String, String> env = new Hashtable<String, String>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, System.getProperty(PROPERTY_CONTEXT_FACTORY, NIRVANA_CONTEXT_FACTORY));

    // check for AMPQ usage
    String providerUrl = System.getProperty(PROPERTY_PROVIDER_URL, NIRVANA_DEFAULT_URL);
    if (providerUrl.startsWith(AMQP_PROTOCOL_PREFIX)) {
      env.put(AMQP_CONNECTION_FACTORY_PREFIX + factoryName, providerUrl);
      if (destinationName.startsWith(AMQP_TOPIC_PREFIX)) {
        String topicName = destinationName.substring(AMQP_TOPIC_PREFIX.length());
        env.put(AMQP_TOPIC_PREFIX + destinationName, topicName);
      } else if (destinationName.startsWith(AMQP_QUEUE_PREFIX)) {
        String queueName = destinationName.substring(AMQP_QUEUE_PREFIX.length());
        env.put(AMQP_QUEUE_PREFIX + destinationName, queueName);
      } else {
        System.out.println("When using AMQP, the destination name should be prefixed by \"topic.\" or \"queue.\"");
      }
    } else {
      String rname = System.getProperty(PROPERTY_RNAME);

      if (rname == null) {
        env.put(Context.PROVIDER_URL, System.getProperty(PROPERTY_PROVIDER_URL, NIRVANA_DEFAULT_URL));
      } else {
        env.put(Context.PROVIDER_URL, System.getProperty(PROPERTY_PROVIDER_URL, rname));
      }
    }

    String username = null;
    String password = null;
    if (!"Y".equalsIgnoreCase(System.getProperty(ENV_ALTJMSCREDS))) {
      username = System.getenv(ENV_USERNAME);
      password = System.getenv(ENV_PASSWORD);
    }
    if (username == null) {
      username = System.getProperty(PROPERTY_PRINCIPAL, SIMPLE);
    }
    if (password == null) {
      password = System.getProperty(PROPERTY_PASSWORD, SIMPLE);
    }
    env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
    env.put(Context.SECURITY_PRINCIPAL, username);
    env.put(Context.SECURITY_CREDENTIALS, password);
    System.out.println("JNDI credentials = " + username);

    return new InitialContext(env);
  }

  /**
   * This method is the callback for any exception generated by the JMS provider
   *
   * @param ex the JMSException thrown
   */
  public void onException(JMSException ex) {
    ex.printStackTrace();
  }
}
