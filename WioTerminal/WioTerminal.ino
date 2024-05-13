#include "Screen_draw.h"
#include "WiFi.h"
#include "pin.h"
#include "utils.h"
#include "mqtt.h"
#include <time.h>


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

unsigned long currentMillis = millis(); //Store the current time in milliseconds since the program started

Screen_draw();

if (!client.connected()) { // Connect to Mqtt if not connected 
     MQTT_connect();
}
  client.loop();
  
  int valueTemp = analogRead(pinTempSensor);   // read temperature sensor signal     
  int valueLight = analogRead(pinLightSensor); // read light sensor signal
  int valuePh = analogRead(pinPhSensor);       // read ph sensor signal

  int tempResult = convertTemp(valueTemp);  // read temperature sensor signal
  int lightResult = mapToPercentage(valueLight);
  int phResult = convertPh(valuePh);


  // Publish sensor readings and update display if interval has elapsed
  if (currentMillis - previousMillis >= interval) {

      // Get current time in unix epoch seconds
      unsigned long timeStamp = time(NULL);

      // Create publish message
      String temperatureMessage = String(tempResult) + ", " + String(timeStamp);
      String lightMessage = String(lightResult) + ", " + String(timeStamp)
      String phMessage = String(phResult) + ", " + String(timeStamp);


      previousMillis = currentMillis;
      Serial.print("Publish reading");
      Serial.println(msg);
      client.publish(TOPIC_PUB_TEMP, temperatureMessage.c_str());
      client.publish(TOPIC_PUB_LIGHT, lightMessage.c_str());
      client.publish(TOPIC_PUB_PH, phMessage.c_str());
      
      delay(1000);
      tft.fillScreen(TFT_WHITE);
      tft.drawNumber(tempResult,50,95); 
      tft.drawNumber(lightResult,50,190); 
      tft.drawNumber(pinPhSensor,210,95); 
      tft.drawNumber(1,225,190); 
     
    }
  }