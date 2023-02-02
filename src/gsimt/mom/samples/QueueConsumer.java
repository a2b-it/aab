/*
 *
 *   Copyright (c) 1999 - 2011 my-Channels Ltd
 *   Copyright (c) 2012 - 2017 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 *   Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 *
 */

package gsimt.mom.samples;

import com.pcbsys.foundation.utils.fEnvironment;

import com.pcbsys.nirvana.client.nSessionAttributes;
import javax.jms.*;

public class QueueConsumer {

  public QueueConsumer() {
  }

  public static void main(String[] args) {
    QueueConsumer queueConsumer = new QueueConsumer();
    if (args.length < 1) {
      Usage();
      System.exit(1);
    } else if ((args.length == 1) && (args[0].equals("-?"))) {
      Usage();
      UsageEnv();
      System.exit(1);
    }

    queueConsumer.doIt(args[0]);
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
    System.out.println("   pass - The HTTP proxy authentication password\n");
  }

  /**
   * This method demonstrates the Nirvana JMS API calls necessary to subscribe to
   * a queue.
   * It is called after all command line arguments have been received and
   * validated
   *
   * @param qname The name of the queue to subscribe to
   */
  private void doIt(String qname) {
    // process the environment variables for rname, proxy and ssl configurations
    processEnvironmentVariables();
    //Check the local realm details (can have a maximum of 4 Interfaces)
    int idx = 0;
    String RNAME = null;
    if (System.getProperty("RNAME") != null) {
      RNAME = System.getProperty("RNAME");
    } else {
      Usage();
      System.exit(1);
    }

    try {
      QueueConnectionFactory qcf = new com.pcbsys.nirvana.nJMS.QueueConnectionFactoryImpl(RNAME);
      QueueConnection qc = qcf.createQueueConnection();
      qc.setExceptionListener(new nJMSExceptionListener());
      qc.start();
      QueueSession qs = qc.createQueueSession(true, Session.SESSION_TRANSACTED);
      Queue q = qs.createQueue(qname);
      QueueReceiver qr = qs.createReceiver(q);
      while (true) {
        TextMessage tmsg = (TextMessage) qr.receive();
        System.out.println("JMS MSG ID : " + tmsg.getJMSMessageID());
        System.out.println("JMS DELIVERY MODE : " + tmsg.getJMSDeliveryMode());
        System.out.println("JMS TIME STAMP : " + tmsg.getJMSTimestamp());
        System.out.println("Request: " + tmsg.getText());
        boolean committed = false;
        while (!committed) {
          try {
            qs.commit();
            committed = true;
          } catch (TransactionRolledBackException e) {
            committed = true;
            System.out.println("Transaction rolled back " + e.getMessage());
          } catch (Exception e) {
            Thread.sleep(1000);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Prints the usage message for this class
   */
  private static void Usage() {
    System.out.println("Usage ...\n\n");
    System.out.println("jmsqueuesub <queueName>\n");

    System.out.println("<Required Arguments> \n");
    System.out.println("<queueName> - JMS Queue to subscribe to");

    System.out.println("\n\nNote: -? provides help on environment variables \n");
  }

  private static void processEnvironmentVariable(String variable) {
    String laxVAR = System.getProperty("lax.nl.env." + variable);
    if (laxVAR == null) {
      laxVAR = System.getenv(variable);
    }
    if (laxVAR != null) {
      if (fEnvironment.CKEYSTORE.equals(variable)) {
        variable = nSessionAttributes.KEYSTORE_PATH;
        System.clearProperty(fEnvironment.CKEYSTORE);
      } else if (fEnvironment.CKEYSTOREPASSWD.equals(variable)) {
        variable = nSessionAttributes.KEYSTORE_PASSWORD;
        System.clearProperty(fEnvironment.CKEYSTOREPASSWD);
      } else if (fEnvironment.CAKEYSTORE.equals(variable)) {
        variable = nSessionAttributes.TRUSTSTORE_PATH;
        System.clearProperty(fEnvironment.CAKEYSTORE);
      } else if (fEnvironment.CAKEYSTOREPASSWD.equals(variable)) {
        variable = nSessionAttributes.TRUSTSTORE_PASSWORD;
        System.clearProperty(fEnvironment.CAKEYSTOREPASSWD);
      }
      System.setProperty(variable, laxVAR);
    }
  }

  private void processEnvironmentVariables() {
    //Process Environment Variables
    processEnvironmentVariable("RNAME");
    processEnvironmentVariable("LOGLEVEL");
    processEnvironmentVariable("HPROXY");
    processEnvironmentVariable("HAUTH");
    processEnvironmentVariable("CKEYSTORE");
    processEnvironmentVariable("CKEYSTOREPASSWD");
    processEnvironmentVariable("CAKEYSTORE");
    processEnvironmentVariable("CAKEYSTOREPASSWD");
    // Install any proxy server settings
    fEnvironment.setProxyEnvironments();
    // Install any ssl settings
    fEnvironment.setSSLEnvironments();
  }
} // End of QueueConsumer Class
