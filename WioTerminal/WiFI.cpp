#include "WiFi.h";
#include "Screen_draw.h"

const char* ssid = "iPhone";
const char* password =  "aquacare";


void WiFi_setup() {
    
    // Source : https://wiki.seeedstudio.com/Wio-Terminal-Wi-Fi/
    WiFi.mode(WIFI_STA); // Switches Wi-Fi mode to station mode
    WiFi.disconnect(); // disconnects from any connected network

    Serial.println("Connecting to WiFi.."); 
   
    WiFi.begin(ssid, password); // Initiates connection to Wi-Fi network using provided SSID and password
     
    //Continuously attempts Wi-Fi connection, updating status on serial monitor and screen.
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.println("Connecting to WiFi..");
        tft.fillScreen(TFT_BLACK);
        tft.setTextColor(TFT_BLUE);
         tft.setTextSize(2);
         tft.setCursor((320 - tft.textWidth("Connecting to Wi-Fi")) / 2, 120);
        tft.print("Connecting to Wi-Fi");
        WiFi.begin(ssid, password);
    }
    Serial.println("Connected to the WiFi network");
    tft.fillScreen(TFT_BLACK);
    tft.setCursor((320 - tft.textWidth("Connected!")) / 2, 120);
    tft.print("Connected!");
    delay(2000);
   
    }