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
import com.pcbsys.nirvana.nJMS.QueueConnectionFactoryImpl;
import com.pcbsys.nirvana.*;
import java.io.*;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

public class QueuePublisher {

  private boolean isTransacted = true;

  public QueuePublisher() {
  }

  public static void main(String[] args) {
    QueuePublisher queuePublisher = new QueuePublisher();
    if (args.length < 1) {
      Usage();
      System.exit(1);
    } else if ((args.length == 1) && (args[0].equals("-?"))) {
      Usage();
      UsageEnv();
      System.exit(1);
    }

    queuePublisher.doIt("A");
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

    System.out.println("(ISTRANSACTED) - This determines if publishing will be transacted (default: false)\n");
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
   * This method demonstrates the Nirvana JMS API calls necessary to publish to
   * a queue.
   * It is called after all command line arguments have been received and
   * validated
   *
   * @param qname The name of the topic to publish to
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
      String isTrans = null;
      isTrans = System.getProperty("ISTRANSACTED");
      QueueSession qs = null;
      if (isTrans != null) {
        if (isTrans.equalsIgnoreCase("true")) {
          qs = qc.createQueueSession(true, 1);
          isTransacted = true;
        }

        if (isTrans.equalsIgnoreCase("false")) {
          qs = qc.createQueueSession(false, 1);
        }
      } else {
        qs = qc.createQueueSession(false, 1);
      }

      Queue q = qs.createQueue(qname);
      QueueSender qp = qs.createSender(q);
      System.out.println("Enter Q to finish publishing : ");
      while (true) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in), 1);
        System.out.print("Enter a Message to be published : ");
        String str = br.readLine();
        if (str.equalsIgnoreCase("Q")) {
          System.out.println("Closing queue session and queue connection");
          qs.close();
          qc.close();
          break;
        }
        TextMessage tmsg = qs.createTextMessage();
        tmsg.setText(str);
        qp.send(tmsg);
        if (isTransacted) {
          qs.commit();
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
    System.out.println("jmsqueuepub <queueName>\n");

    System.out.println("<Required Arguments> \n");
    System.out.println("<queueName> - JMS Queue to publish on");

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
    processEnvironmentVariable("ISTRANSACTED");
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
} // End of QueuePublisher Class
