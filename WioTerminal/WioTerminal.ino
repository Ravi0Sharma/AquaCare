#include"Screen_draw.h"
#include"WiFi.h"
#include"pin.h"
#include"mqtt.h"
#include"utils.h"

char msg[50];const
long interval = 5000;
unsigned long previousMillis = 0;

TFT_eSPI tft;
Servo dispenser;

void setup() {

    tft.begin();
    tft.setRotation(3);
    tft.fillScreen(TFT_WHITE); // Fill Wio Terminal screen white.

    Serial.begin(serial_Begin_Rate); // Start serial communication

    WiFi_setup(); // Establishes a connection between the Wio Terminal and a WiFi network.
    delay(3000);
    client.setServer(mqtt_server, 1883); // Connect the MQTT Server

    client.setCallback(callback); // Define behavior when message recvided from mqtt broker
    dispenser.attach(pinfoodDispenser); // Set up servo motor

}

void loop() {

unsigned long currentMillis = millis(); //Store the current time in milliseconds since the program started

if (!client.connected()) { // Connect to Mqtt if not connected 
     MQTT_connect();
}
  client.loop();

  Screen_draw();

  int valueTemp = analogRead(pinTempSensor);    // read temperature sensor signal
  int valueLight = analogRead(pinLightSensor);  // read light sensor signal
  int valuePh = analogRead(pinPhSensor);        // read ph sensor signal

  int tempResult = convertTemp(valueTemp); 
  int lightResult = mapToPercentage(valueLight);
  
  // Publish sensor readings and update display if interval has elapsed
  if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      Serial.print("Publish reading");
      Serial.println(msg);
      client.publish(TOPIC_PUB_TEMP, String(tempResult).c_str());
      client.publish(TOPIC_PUB_LIGHT, String(lightResult).c_str());
      client.publish(TOPIC_PUB_PH, String(pinPhSensor).c_str());

      delay(1000);
      Screen_result(tempResult, lightResult, pinPhSensor, dispenserUsageCount); 
     
    }
  }