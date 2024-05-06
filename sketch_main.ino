#include "Screen_draw.h"
#include "WiFi.h"
#include "pin.h"
#include "mqtt.h"   
#include "utils.h"

long lastMsg = 0;
long now = millis();
char msg[30];

TFT_eSPI tft;
Servo dispenser;

void setup() {
  
  tft.begin();
  tft.setRotation(3);

  Serial.begin(serial_Begin_Rate);  //start serial communication
  dispenser.attach(pinfoodDispenser); 

  WiFi_setup(); // Establishes a connection between the Wio Terminal and a WiFi network.
  client.setServer(mqtt_server, 1883); // Connect the MQTT Server

  client.setCallback(callback);
}

void loop() {

if (!client.connected()) {
     MQTT_connect();
}
  client.loop();
  
  Screen_draw();

  int valueTemp = analogRead(pinTempSensor);        
  int valueLight = analogRead(pinLightSensor); 
  int valuePh = analogRead(pinPhSensor);   

  int tempResult = convertTemp(valueTemp);
  int lightResult = mapToPercentage(valueLight);
  int phResult = convertPh(valuePh);

  //tft.drawNumber(tempResult,50,95); 
  //tft.drawNumber(lightResult,30,190);  
  //tft.drawNumber(phResult,200,95); 
 
  if (now - lastMsg > 2000) {
    lastMsg = now;
       Serial.print("Publish reading");
    Serial.println(msg);
    client.publish(TOPIC_PUB_TEMP, String(tempResult).c_str());
    client.publish(TOPIC_PUB_LIGHT, String(lightResult).c_str());
    client.publish(TOPIC_PUB_PH, String(phResult).c_str());
   
  }
  
}

