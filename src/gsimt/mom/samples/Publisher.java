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
import com.pcbsys.nirvana.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * This class demonstrates how to construct a Publisher that can send messages on either a topic or queue,
 * based on the type of the connection factory provided as parameters
 */
public class Publisher extends JMSClient {

  private final Object mutex = new Object();
  protected MessageProducer p;
  protected Session s;
  protected int publishCount = 0;

  public Publisher(String factoryName, String destinationName) {
    super(factoryName, destinationName);
  }

  public static void main(String[] args) {
    //Check to see if args were specified
    if ((args.length == 1) && args[0].equals("-?")) {
      UsageEnv();
    }

    if (args.length < 2) {
      Usage();
      System.exit(1);
    }

    //Process Environment Variables
    processEnvironmentVariable("RNAME");
    processEnvironmentVariable("PRINCIPAL");
    processEnvironmentVariable("PASSWORD");
    processEnvironmentVariable("CONTEXT_FACTORY");
    processEnvironmentVariable("PROVIDER_URL");
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

    //Create an instance for this class
    Publisher publisher = new Publisher(args[0], args[1]);

    int count = 10000;
    boolean transacted = false;

    if (args.length > 2) {
      count = Integer.parseInt(args[2]);
    }

    if (args.length > 3) {
      transacted = Boolean.parseBoolean(args[3]);
    }

    //Publish to the topic specified
    publisher.doIt(args[0], args[1], count, transacted);
  }

  protected static void UsageEnv() {
    JMSClient.UsageEnv();
    System.out.println("(ISTRANSACTED) - This determines if publishing will be transacted (default: false)\n");
  }

  /**
   * Get the destination from the context, if not bound, then bind it
   *
   * @param ctx the context to use
   * @param s the session used to create the destination if it is not bound
   * @param destName the name of the destination
   * @return the desired topic
   */
  protected Destination getDestination(Context ctx, Session s, String destName) {
    Destination d = null;

    try {
      d = (Destination) ctx.lookup(destName);
    } catch (NamingException e) {
      try {
        if (s instanceof TopicSession) {
          d = s.createTopic(destName);
        } else {
          d = s.createQueue(destName);
        }
        ctx.bind(destName, d);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    return d;
  }

  public void doTXPublish(Message msg) {
    boolean success = false;
    while (!success) {
      try {
        p.send(msg);
        success = true;
      } catch (Throwable t) {
        System.out.println("JMS-Producer send failed - " + t);
        synchronized (mutex) {
          try {
            mutex.wait(5);
          } catch (InterruptedException ignored) {
          }
        }
      }
    }
    success = false;
    while (!success) {
      try {
        s.commit();
        success = true;
        publishCount++;
        synchronized (mutex) {
          mutex.wait(10); //NB: Modify or comment this out, to change the throttling level
        }
      } catch (Throwable t) {
        System.out.println("JMS-Producer commit failed - " + t);
        try {
          synchronized (mutex) {
            mutex.wait(500);
          }
        } catch (InterruptedException ignored) {
        }
      }
    }
  }

  /**
   * This method demonstrates the Nirvana JMS API calls necessary to publish to
   * a destination whether it is a topic or a queue.
   * It is called after all command line arguments have been received and
   * validated
   *
   * @param factoryName The name of the factory to find
   * @param destName the name of the destination
   * @param count number of message to publish
   * @param transacted whether the session is transactional
   */
  private void doIt(String factoryName, String destName, int count, boolean transacted) {
    try {
      // get the initial context
      Context ctx = getInitialContext();
      ConnectionFactory cf = (ConnectionFactory) ctx.lookup(factoryName);

      // Create a Connection from the Connection Factory
      Connection c = cf.createConnection();

      // set the exceptionlistener
      c.setExceptionListener(this);

      // Start the connection
      c.start();

      // Create a Sesson from the Connection
      s = c.createSession(transacted, 1);

      // create the destination, and bind if necessary
      Destination d = getDestination(ctx, s, destName);

      //Create a Producer from the Session
      p = s.createProducer(d);

      //Prompt for a message
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in), 1);
      System.out.print("Enter a message to be published : ");

      String str = br.readLine();

      // create a bytes message
      TextMessage bmsg = s.createTextMessage();
      bmsg.setText(str);

      //Loop for count
      for (int x = 0; x < count; x++) {
        if (transacted) {
          doTXPublish(bmsg);
        } else {
          doPublish(bmsg);
        }
      }

      //Print a message to the console saying we are about to exit
      System.out.println("Closing session and connection. Published a total of " + publishCount + " messages");
      Thread.sleep(1000);
      c.close();
      s.close();

      //close the context
      ctx.close();

      if (publishCount < count) {
        System.exit(1);
      }

    } catch (NamingException ex) {
      System.out.println("\nNaming Exception : Please ensure you have created the connection factory " + factoryName);
      System.out.println(
          "Naming Exception : This can be done using the Enterprise Manager JNDI panel or the jmsadmin command line application");
      ex.printStackTrace();
      System.exit(1);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public void doPublish(Message msg) {
    try {
      //Publish the message to the topic
      p.send(msg);
      publishCount++;
    } catch (JMSException e) {
      e.printStackTrace();
      boolean committed = false;

      while (!committed) {
        try {
          p.send(msg);
          committed = true;
        } catch (JMSException ex) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ignored) {
          }
        }
      }
    }
  }

  /**
   * Prints the usage message for this class
   */
  private static void Usage() {
    System.out.println("Usage ...\n\n");
    System.out.println("jmspub <factoryname> <destinationName> <count> <transacted>\n");

    System.out.println("<Required Arguments> \n");
    JMSClient.printFactoryNameUsage();
    System.out.println("<destinationName> - JMS Destination to publish on");
    System.out.println("<count> - Number of events to publish");
    System.out.println("<transacted> - Whether the session is transacted");
    System.out.println("\n\nNote: -? provides help on environment variables \n");
  }

  protected static void processEnvironmentVariable(String variable) {
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
}
// End of Publisher Class
