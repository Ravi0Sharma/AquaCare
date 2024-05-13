#include "mqtt.h"
#include "WiFi.h" 
#include "utils.h"  
#include "Screen_draw.h"
#include <time.h>

                      
WiFiClient wioClient;
PubSubClient client(wioClient);

const char* mqtt_server = "broker.hivemq.com"; // MQTT server address

// Topics for sending sensor data
const char* TOPIC_PUB_TEMP  = "AquaCare/1/Temperature";
const char* TOPIC_PUB_LIGHT = "AquaCare/1/Light";
const char* TOPIC_PUB_PH  = "AquaCare/1/Ph";
const char* TOPIC_PUB_FOOD = "AquaCare/1/Dispenser";

const char* TOPIC_SUB_FOOD = "AquaCare/1/Feed";  // Topic for subscribing to food dispensing requests

// Connect to MQTT broker

void MQTT_connect() {

  //This code briefly displays "Connecting to MQTT" on both serial and screen.
  Serial.print("Connecting to MQTT"); 
  tft.fillScreen(TFT_BLACK);
  tft.setTextSize(2);
  tft.setCursor((320 - tft.textWidth("Connecting to MQTT")) / 2, 120);
  tft.print("Connecting to MQTT");
  delay(3000);
  
  while (!client.connected()) {
    String clientId = "WioTerminal/Aquacare";
    
    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      client.subscribe(TOPIC_SUB_FOOD);
      
    } else {
       // Attempts to connect to MQTT, updating the display and serial output until successful.
      Serial.print("failed, state=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
   
      tft.setCursor((320 - tft.textWidth("Connecting to MQTT")) / 2, 120);
      tft.print("Connecting to MQTT");


       //Loop attempts to connect to Wi-Fi, updating the display and serial output until successful.
       while (WiFi.status() != WL_CONNECTED){
       delay(500);
       tft.fillScreen(TFT_BLACK);
      delay(1000);
       tft.setCursor((320 - tft.textWidth("Connecting to Wi-Fi..")) / 2, 120);
       tft.print("Connecting to Wi-Fi..");
       Serial.println("Connecting to WiFi..");
       WiFi.begin(ssid, password);
      }
      delay(3000);
      
    }
  }
  // Display and serial output Connected.
  Serial.println();
  Serial.print("Connected");
  tft.fillScreen(TFT_BLACK);
  tft.setCursor((320 - tft.textWidth("Connected!")) / 2, 120);
  delay(2000);
  tft.print("Connected");
  delay(5000);
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

 // Triggers servo if topic matches TOPIC_SUB_FOOD, and publishes confirmation.
 if (strcmp(topic, TOPIC_SUB_FOOD) == 0) {

    unsigned long timeStamp = time(NULL);

    // Create publish message
    String dispenserMessage = "1" + ", " + String(timeStamp);

    servo();
    client.publish(TOPIC_PUB_FOOD, dispenserMessage.c_str());

}

}