package ui.utilities;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttJavaClient implements MqttCallbackExtended {
    //Signleton class
    private static MqttJavaClient mqttJavaClient = null;

    DatabaseInputParser databaseInputParser;

    // private variables
    private final String broker ;
    private final String clientId;
    private final String userName;
    private final String password;

    // private instance variable

    private  MemoryPersistence persistence;
    private  MqttConnectOptions connOpts;
    private  MqttAsyncClient mqttAsyncJavaClient;


    //Singleton initiator
    public static MqttJavaClient getInstance()
    {
        // To ensure only one instance is created
        if (mqttJavaClient == null) {
            mqttJavaClient = new MqttJavaClient();
        }
        return mqttJavaClient;
    }

    // Interface MqttCallback Implementation
    /**
     * 
     * connectionLost
     * This callback is invoked upon losing the MQTT connection.
     * 
     */
    @Override
    public void connectionLost(Throwable arg0) {
        System.err.println("connection lost");
    }

    /**
     * 
     * messageArrived
     * This callback is invoked when a message is received on a subscribed topic.
     * 
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic: " + topic);
        System.out.println("message: " + new String(message.getPayload()));
        databaseInputParser.parseMqttData(topic, new String(message.getPayload()));

    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        SubscribeRelatedTopics();
    }

    // constructor
     private MqttJavaClient(){
        // Do initialization here
        databaseInputParser = new DatabaseInputParser();

        //!Will be changed so credidential are not hardcoded
        broker = "tcp://broker.hivemq.com:1883";

        //I have discovered that Client ID may cause improper connection, such as constant disconnecting and re-connecting
        clientId = "AquaCareApplication";

        userName = "username";
        password = "password";


        try {
            persistence = new MemoryPersistence();
            connOpts = new MqttConnectOptions();
            mqttAsyncJavaClient = new MqttAsyncClient(broker, clientId, persistence);

        } catch (Exception e){
            System.out.println(e);

        }

        Connect();

     }


    // connect to broker

    private void Connect(){
        try {
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            mqttAsyncJavaClient.setCallback(this);
            System.out.println("Connecting to broker: " + broker);
            //connOpts.setUserName("username");
            //connOpts.setPassword("password".toCharArray());
            mqttAsyncJavaClient.connect(connOpts);
            System.out.println("Connected");
            Thread.sleep(500); // wait until connection is complete

        } catch (Exception e){
            System.out.println("conn error" +e);
        }
    }

    // publish a message to a topic with a qos

    public void Publish(String topic, String message, int qos){
        try {
            System.out.println("Publishing message: " + message);
             IMqttDeliveryToken token = null;
             MqttMessage Mqttmsg = new MqttMessage(message.getBytes());
             Mqttmsg.setQos(qos);
             Mqttmsg.setRetained(false);
             token = mqttAsyncJavaClient.publish(topic, Mqttmsg);
             // Wait until the message has been delivered to the broker
             token.waitForCompletion();
             Thread.sleep(100);
             System.out.println("Message published");

        } catch (Exception e) {
            System.out.println("pub error :"+ e);
        }
    }

    // subscribe multiple topics with Qos

    private void Subscribe(String[] topics, int[] Qos){
        try {
            mqttAsyncJavaClient.subscribe(topics, Qos);
            System.out.println("Subscribed");
        } catch (Exception e){
            System.out.println("sub error :"+e);
        }

    }

    // subscribe a topic with qos

    public void Subscribe(String topic, int qos){
        try {
            mqttAsyncJavaClient.subscribe(topic, qos);
            System.out.println("Subscribed");
        } catch (Exception e){
            System.out.println("sub error: " +e);
        }

    }

    // subscribe to related topics

    private void SubscribeRelatedTopics(){
        Subscribe("AquaCare/+/Temperature",1);
        Subscribe("AquaCare/+/Light",1);
        Subscribe("AquaCare/+/Ph",1);
        Subscribe("AquaCare/+/Dispenser",1);

    }

    // disconnect from a broker

    public void Disconnect(){
        try {
            mqttAsyncJavaClient.disconnect();
            System.out.println("Disconnected");

        } catch (Exception e){
            System.out.println("disconnect error" + e);
        }
    }

}