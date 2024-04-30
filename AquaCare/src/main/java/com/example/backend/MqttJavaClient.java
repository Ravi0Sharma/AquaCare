package backend;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttJavaClient implements MqttCallback {
    //Signleton class
    private static MqttJavaClient mqttJavaClient = null;

    DatabaseInputParser databaseInputParser;

    // private variables
    private String broker ;
    private String clientId;
    private String userName;
    private String password;

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
        //Mayb reconnection code

    }

    /**
     * 
     * deliveryComplete
     * This callback is invoked when a message published by this client
     * is successfully received by the broker.
     * 
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Publish complete");
        //System.out.println("Publish complete" + new String(token.getMessage().getPayload()));
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


    // constructor
     private MqttJavaClient(){
        // Do initialization here 
        databaseInputParser = new DatabaseInputParser();

        //!Will be changed so credidential are not hardcoded
        broker = "tcp://broker.hivemq.com:1883";
        clientId = "AquaCareApp";
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

        Subscribe("AquaCare/#",1); //Was for testing purposes
     }
    
     
    // connect to broker

    private void Connect(){
        try {
            connOpts.setCleanSession(true);
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

    private void Subscribe(String topic, int qos){
        try {
            mqttAsyncJavaClient.subscribe(topic, qos);
            System.out.println("Subscribed");
        } catch (Exception e){
            System.out.println("sub error: " +e);
        }
        
    }

    // disconnect from a broker

    private void Disconnect(){
        try {
            mqttAsyncJavaClient.disconnect();
            System.out.println("Disconnected");

        } catch (Exception e){
            System.out.println("disconnect error" + e);
        }
    }
}