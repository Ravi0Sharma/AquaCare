#include <math.h>
#include "TFT_eSPI.h"
#include "rpcWiFi.h"
#include "Adafruit_MQTT.h"
#include "Adafruit_MQTT_Client.h"
#include <DNSServer.h>
#include <WebServer.h>
#include <WiFiManager.h> 

#define AIO_SERVER      "io.adafruit.com"
#define AIO_SERVERPORT  1883                  
#define AIO_USERNAME    "gusravish"
#define AIO_KEY         "aio_lWLq92MIFw7EAUrF0fnDpcfvj6YS"

TFT_eSPI tft;
TFT_eSprite spr = TFT_eSprite(&tft);

const int pinTempSensor = A0;
const int pinLightSensor = A2;

const char* serverName = "io.adafruit.com";
String AIOKey = "aio_lWLq92MIFw7EAUrF0fnDpcfvj6YS"; //api key 

WiFiClient client; //WiFiClientSecure client;

// Setup the MQTT client class by passing in the WiFi client and MQTT server and login details.
Adafruit_MQTT_Client mqtt(&client, AIO_SERVER, AIO_SERVERPORT, AIO_USERNAME, AIO_KEY);

// Setup feed called for temperature and light for publishing.
Adafruit_MQTT_Publish temperature = Adafruit_MQTT_Publish(&mqtt, AIO_USERNAME "/feeds/temperature");
Adafruit_MQTT_Publish light = Adafruit_MQTT_Publish(&mqtt, AIO_USERNAME "/feeds/light");

// publish for ph  

void setup(){

 Serial.begin(115200); //start serial communication
 while(!Serial);  
 delay(100);

 WiFiManager wifiManager; 

wifiManager.resetSettings(); // reset if you want to connect to new network
 
wifiManager.autoConnect("AutoConnectAP");// start acceses point

WiFi.begin(); // connect to wifi

// attempt to connect to wifi
   while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(1000);
   }
   Serial.print("Connected to WiFi");
   Serial.println(WiFi.localIP());
   Serial.println("");
 
tft.begin(); //Start TFT LCD
tft.setRotation(3); //Set TFT LCD rotation
spr.createSprite(TFT_HEIGHT,TFT_WIDTH); //Create buffer

}

void loop(){

MQTT_connect();

uint32_t valueTemp = analogRead(pinTempSensor); // Read analog value from temperature sensor
uint32_t valueLight = analogRead(pinLightSensor); // Read analog value from ligth sensor

// convert the analog value to celcius 
float R = (1023.0 / valueTemp - 1.0 ) * 100000 ;

int32_t temp = 1.0/(log(R/100000)/4275+1/298.15)-273.15;

if (! temperature.publish(temp)) { // publish temp 
   Serial.println(F("Failed"));
 } else {
   Serial.println(F("Temperture sent"));
 }

Serial.print("temperature = ");
Serial.println(temp);


// convert the analog value to K  (have to change to lux )
int32_t lux = (int32_t)(1023 - pinLightSensor) * 10 / pinLightSensor; 

if (! light.publish(lux)) { // publish light
   Serial.println(F("Failed"));
 } else {
   Serial.println(F("light sent"));
 }

Serial.print("Lux = ");
Serial.println(lux);

delay(3000); // Delay before next temperature reading

// header 
spr.fillSprite(TFT_WHITE); 
spr.fillRect(0,0,320,50,TFT_BLUE); 
spr.setTextSize(2); 
spr.setTextColor(TFT_WHITE); 
spr.drawString("Aquarium",65,15);   

// verticle and horizontal line
spr.drawFastVLine(150,50,190,TFT_BLUE); //Drawing verticle line
spr.drawFastHLine(0,140,320,TFT_BLUE); //Drawing horizontal line

// temperature display
spr.setTextColor(TFT_BLACK);
spr.setTextSize(2);
spr.drawString("Temperature",10,65);
spr.setTextSize(3);
spr.drawNumber(temp,50,95); 

//light display
spr.setTextSize(2);
spr.drawString("Light",25,160);
spr.setTextSize(3);
spr.drawNumber(lux,30,190);  
 
//pH display
spr.setTextSize(2);
spr.drawString("pH",200,65);
spr.setTextSize(3);
spr.drawNumber(temp,200,95); 

/*
spr.setTextSize(2);
spr.drawString(""",200,160);
spr.setTextSize(3);
spr.drawNumber(,205,190);   
spr.drawString("",245,190);
*/

//style 
spr.setTextColor(TFT_BLUE);
spr.drawString("C",90,95);
spr.drawString("lux",70,190);
spr.drawString("pH",240,95);


spr.pushSprite(0,0); 
 delay(50);
}

// Function to connect to the MQTT server.
void MQTT_connect() {
 int8_t ret;

 // Stops if already connected.
 if (mqtt.connected()) {
   return;
 }

 Serial.print("Connecting to MQTT... ");

 uint8_t retries = 3;
 while ((ret = mqtt.connect()) != 0) { // connect will return 0 for connected
      Serial.println(mqtt.connectErrorString(ret));
      Serial.println("Retrying MQTT connection in 5 seconds...");
      mqtt.disconnect();
      delay(5000);  
      retries--;
      if (retries == 0) {
        // basically die and wait for WDT to reset me
        while (1);
      }
 }
 Serial.println("MQTT Connected!");
}
