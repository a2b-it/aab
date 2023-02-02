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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.StreamMessage;

public class StreamMessageSubscriber extends Subscriber {

  public StreamMessageSubscriber(String factoryName, String destinationName) {
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
    StreamMessageSubscriber subscriber = new StreamMessageSubscriber(args[0], args[1]);

    boolean transacted = false;
    String selector = null;

    if (args.length > 2) {
      transacted = new Boolean(args[2]).booleanValue();
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
  public void onMessage(Message msg) {
    try {
      if (msg instanceof StreamMessage) {
        StreamMessage smsg = (StreamMessage) msg;
        System.out.println("JMS MSG ID : " + smsg.getJMSMessageID());
        System.out.println("JMS DELIVERY MODE : " + smsg.getJMSDeliveryMode());
        System.out.println("JMS TIME STAMP : " + smsg.getJMSTimestamp());
        System.out.println("Data : " + smsg.readString());
        System.out.println("Count : " + smsg.readInt());
        System.out.println("Transacted : " + smsg.readBoolean());
      } else {
        super.onMessage(msg);
      }

      if (transacted) {
        s.commit();
      }
    } catch (JMSException e) {
      e.printStackTrace();
    }
    count++;
  }

  /**
   * Prints the usage message for this class
   */
  protected static void Usage() {
    System.out.println("Usage ...\n\n");
    System.out.println("jmsstreamsub <factoryname> <destinationName> <transacted> <selector>\n");

    System.out.println("<Required Arguments> \n");
    JMSClient.printFactoryNameUsage();
    JMSClient.printDestinationNameUsage();
    System.out.println("<transacted> - Whether the session is transacted");
    System.out.println("<selector> - An optional message selector");
    System.out.println("\n\nNote: -? provides help on environment variables \n");
  }
}
// End of StreamMessageSubscriber Class
