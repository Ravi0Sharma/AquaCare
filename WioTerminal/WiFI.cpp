#include "WiFi.h"
#include "Screen_draw.h"

const char* ssid = "iPhone";
const char* password =  "aquacare";

void WiFi_setup() {
    
     // Source : https://wiki.seeedstudio.comWio-Terminal-Wi-Fi/
    WiFi.mode(WIFI_STA); // Switches Wi-Fi mode to station mode
    WiFi.disconnect(); // disconnects from any connected network

    Serial.println("Connecting to WiFi.."); 
   
    WiFi.begin(ssid, password); // Initiates connection to Wi-Fi network using provided SSID and password
    tft.fillScreen(TFT_WHITE);
    //Continuously attempts Wi-Fi connection, updating status on serial monitor and screen.
    while (WiFi.status() != WL_CONNECTED) {
        WiFi.begin(ssid, password);
        delay(1500);
        
        Serial.println("Connecting to WiFi..");
        Screen_logo(); 
      
        Screen_connectingWiFi();
    }
    tft.fillScreen(TFT_WHITE);
    Serial.println("Connected to the WiFi network");
    Screen_connected();
    
  delay(2000);
  tft.fillScreen(TFT_WHITE);
  
    }