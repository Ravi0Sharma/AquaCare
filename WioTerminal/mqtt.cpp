#include "mqtt.h"
#include "WiFi.h" 
#include "utils.h"  
#include "Screen_draw.h"
                      
WiFiClient wioClient;
PubSubClient client(wioClient);

const char* mqtt_server = "broker.hivemq.com"; // MQTT server address

// Topics for sending sensor data
const char* TOPIC_PUB_TEMP  = "AquaCare/1/Temperature";
const char* TOPIC_PUB_LIGHT = "AquaCare/1/Light";
const char* TOPIC_PUB_PH  = "AquaCare/1/Ph";
const char* TOPIC_PUB_FOOD = "AquaCare/1/Dispenser";

const char* TOPIC_SUB_FOOD = "AquaCare/1/Feed"; // Topic for subscribing to food dispensing requests

int dispenserUsageCount = 0; 


// Connect to MQTT broker
void MQTT_connect() {

  //This code briefly displays "Connecting to MQTT" on both serial and screen
  tft.fillScreen(TFT_WHITE);
  Screen_logo();
  Screen_connectingMQTT();
  delay(2000);
  
  
  while (!client.connected()) {
    String clientId = "WioTerminal/Aquacare";
    
    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      client.subscribe(TOPIC_SUB_FOOD);
      
    } else {
      // Attempts to connect to MQTT, updating the display and serial output until successful
      Serial.print("failed, state=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");

      //Loop attempts to connect to Wi-Fi, updating the display and serial output until successful
       while (WiFi.status() != WL_CONNECTED){
       delay(500);
       tft.fillScreen(TFT_WHITE);
      delay(1000);
      WiFi_setup();
      }
      delay(3000);
      
    }
  }
  // Display and serial output Connected
  tft.fillScreen(TFT_WHITE);
  Screen_connected();
  delay(4000);
  tft.fillScreen(TFT_WHITE);
}

//Handles incoming MQTT messages
void callback(char* topic, byte* payload, unsigned int length) {
 
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  char buff_p[length];
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
    buff_p[i] = (char)payload[i];
  }

 // Triggers servo if topic matches TOPIC_SUB_FOOD, and publishes confirmation
 if (strcmp(topic, TOPIC_SUB_FOOD) == 0) {
    servo();
    client.publish(TOPIC_PUB_FOOD, "1");

    dispenserUsageCount++; 
}

}