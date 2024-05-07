#include "Screen_draw.h"
#include "WiFi.h"
#include "pin.h"
#include "mqtt.h"   
#include "utils.h"


char msg[50];
const long interval = 5000;
unsigned long previousMillis = 0;  

TFT_eSPI tft;
TFT_eSprite spr = TFT_eSprite(&tft);
Servo dispenser;

void setup() {
  
  tft.begin();
  tft.setRotation(3);
  tft.fillScreen(TFT_WHITE);

  Serial.begin(serial_Begin_Rate);  //start serial communication

  WiFi_setup(); // Establishes a connection between the Wio Terminal and a WiFi network.
  delay (3000);
  client.setServer(mqtt_server, 1883); // Connect the MQTT Server

  client.setCallback(callback);
  dispenser.attach(pinfoodDispenser); 
  
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
      client.publish(TOPIC_PUB_PH, String(phResult).c_str());

      tft.fillRect(0, 50, 320, 200, TFT_WHITE);
      tft.drawNumber(valueTemp,50,95); 
      tft.drawNumber(valueLight,50,190); 
      tft.drawNumber(valuePh,210,95); 
      tft.drawNumber(1,225,190); 
     
    }
  }