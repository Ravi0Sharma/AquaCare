#include "WiFi.h"; 

const char* ssid = "iPhone";
const char* password =  "aquacare";

void WiFi_setup() {
    
     // Source : https://wiki.seeedstudio.com/Wio-Terminal-Wi-Fi/
    WiFi.mode(WIFI_STA);
    WiFi.disconnect();

    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.println("Connecting to WiFi..");
        WiFi.begin(ssid, password);
    }
    Serial.println("Connected to the WiFi network");
    }