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
import javax.naming.Context;
import javax.naming.NamingException;

public class Subscriber extends JMSClient implements MessageListener {

  // Sesson required for transacted operations
  protected Session s = null;

  protected int count = 0;

  // is the session transacted
  protected boolean transacted = false;

  public Subscriber(String factoryName, String destinationName) {
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
    Subscriber subscriber = new Subscriber(args[0], args[1]);

    boolean transacted = false;
    String selector = null;

    if (args.length > 2) {
      transacted = Boolean.parseBoolean(args[2]);
    }

    if (args.length > 3) {
      selector = args[3];
    }

    //Subscribe to the destination specified
    subscriber.doIt(args[0], args[1], transacted, selector);
  }

  /**
   * A callback is received by the API to this method each time a message is received from
   * the destination.
   *
   * @param msg A JMS Message object
   */
  @Override
  public void onMessage(Message msg) {
    try {
      //Process the jms Message
      System.out.println("JMS MSG ID : " + msg.getJMSMessageID());
      System.out.println("JMS DELIVERY MODE : " + msg.getJMSDeliveryMode());
      System.out.println("JMS TIME STAMP : " + msg.getJMSTimestamp());
      System.out.println("Received : " + msg.toString());

      if (transacted) {
        boolean committed = false;
        while (!committed) {
          try {
            s.commit();
            committed = true;
          } catch (TransactionRolledBackException e) {
            committed = true;
            System.out.println("Transaction rolled back " + e.getMessage());
          } catch (Exception e) {
            e.printStackTrace();
            Thread.sleep(1000);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    count++;
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
      } catch (JMSException e1) {
        e1.printStackTrace();
      } catch (NamingException e1) {
        e1.printStackTrace();
      }
    }
    return d;
  }

  /**
   * This method demonstrates the Nirvana JMS API calls necessary to subscribe to
   * a destination whether it is a topic or a queue.
   * It is called after all command line arguments have been received and
   * validated
   *
   * @param factoryName The name of the factory to find
   * @param destName the name of the destination
   * @param isTransacted whether the session is transactional
   * @param selector an optional mesage selector string
   */
  protected void doIt(String factoryName, String destName, boolean isTransacted, String selector) {
    try {
      this.transacted = isTransacted;

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

      // Create a Subscriber from the Session
      MessageConsumer consumer = s.createConsumer(d, selector);

      // Set the message listener to receive messages asynchronously
      consumer.setMessageListener(this);

      ctx.close();

      //Stay subscribed until the user presses any key
      System.in.read();

      System.out.println("Closing connection. Consumed a total of " + count);

      c.close();
      s.close();
      ctx.close();

      if (count <= 0) {
        System.out.println("No Messages are received");
        System.exit(2);
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

  /**
   * Prints the usage message for this class
   */
  protected static void Usage() {
    System.out.println("Usage ...\n\n");
    System.out.println("jmssub <factoryname> <destinationName> <transacted>\n");

    System.out.println("<Required Arguments> \n");
    JMSClient.printFactoryNameUsage();
    JMSClient.printDestinationNameUsage();
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
// End of Subscriber Class