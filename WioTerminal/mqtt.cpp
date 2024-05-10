#include "mqtt.h"
#include "WiFi.h" 
#include "utils.h"  
                      
WiFiClient wioClient;
PubSubClient client(wioClient);

const char* mqtt_server = "broker.hivemq.com";

const char* TOPIC_PUB_TEMP  = "AquaCare/deviceID/Sensors/Temperature";
const char* TOPIC_PUB_LIGHT = "AquaCare/deviceID/Sensors/Light";
const char* TOPIC_PUB_PH  = "AquaCare/deviceID/Sensors/pH";
//const char* TOPIC_PUB_FOOD = "AquaCare/deviceID/motor/Food";

const char* TOPIC_SUB_TEMP =  "AquaCare/deviceID/Range/Temperature";
const char* TOPIC_SUB_LIGHT = "AquaCare/deviceID/Range/Light";
const char* TOPIC_SUB_PH = "AquaCare/deviceID/Range/pH";
//const char* TOPIC_SUB_FOOD = "AquaCare/deviceID/Request/Food";

void MQTT_connect() {

 // Loop until we're reconnected
  Serial.print("Connecting to MQTT");  
  // Loop until we're reconnected
  while (!client.connected()) {
    String clientId = "WioTerminal/Aquacare";
    
    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      client.subscribe(TOPIC_SUB_TEMP);
      
      client.subscribe(TOPIC_SUB_LIGHT);
      client.subscribe(TOPIC_SUB_PH);
//      client.subscribe(TOPIC_SUB_FOOD);
      
    } else {
      Serial.print("failed, state=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      
      delay(5000);
    }
  }
  Serial.println();
  Serial.print("Connected");
}

void callback(char* topic, byte* payload, unsigned int length) {
 
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  char buff_p[length];
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
    buff_p[i] = (char)payload[i];
  }

// if (strcmp(topic, TOPIC_SUB_FOOD) == 0) {
//    servo();
//    client.publish(TOPIC_PUB_FOOD, "1");
//
//}


}