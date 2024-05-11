#include "Screen_draw.h"
#include "WiFi.h"
#include "pin.h"
#include "utils.h"
#include "mqtt.h"


char msg[50];
const long interval = 5000;
unsigned long previousMillis = 0;  

TFT_eSPI tft;
Servo dispenser;

void setup() {
  
  tft.begin();
  tft.setRotation(3);
  tft.fillScreen(TFT_WHITE); // Fill Wio Terminal screen white.

  Serial.begin(serial_Begin_Rate);  //start serial communication

  WiFi_setup(); // Establishes a connection between the Wio Terminal and a WiFi network.
  delay (3000);
  client.setServer(mqtt_server, 1883); // Connect the MQTT Server

  client.setCallback(callback); // Define behavior when message recvided from mqtt broker
  dispenser.attach(pinfoodDispenser); // Set up servo motor 
  
}

void loop() {

unsigned long currentMillis = millis();

Screen_draw();

if (!client.connected()) {
     MQTT_connect();
}
  client.loop();
  
  int valueTemp = analogRead(pinTempSensor);        
  int valueLight = analogRead(pinLightSensor); 
  int valuePh = analogRead(pinPhSensor);   

  int tempResult = convertTemp(valueTemp);
  int lightResult = mapToPercentage(valueLight);
  int phResult = convertPh(valuePh);


  if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      Serial.print("Publish reading");
      Serial.println(msg);
      client.publish(TOPIC_PUB_TEMP, String(tempResult).c_str());
      client.publish(TOPIC_PUB_LIGHT, String(lightResult).c_str());
      client.publish(TOPIC_PUB_PH, String(pinPhSensor).c_str());
      
      delay(1000);
      tft.fillScreen(TFT_WHITE);
      tft.drawNumber(tempResult,50,95); 
      tft.drawNumber(lightResult,50,190); 
      tft.drawNumber(pinPhSensor,210,95); 
      tft.drawNumber(1,225,190); 
     
    }
  }