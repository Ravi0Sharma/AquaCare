#include "Screen_draw.h"
#include "utils.h"
#include "WiFi.h"

void Screen_draw(){ 

tft.fillRect(0,0,320,50,TFT_BLUE); //Fills a blue rectangle at the top of the Wio Terminal screen.

// verticle and horizontal line
tft.drawFastVLine(150,50,190,TFT_BLUE); //Drawing verticle line
tft.drawFastHLine(0,140,320,TFT_BLUE); //Drawing horizontal line

// temperature display
tft.setTextColor(TFT_BLACK);
tft.setTextSize(2);
tft.drawString("Temperature",10,65);
tft.setTextSize(3);

//light display
tft.setTextSize(2);
tft.drawString("Light",45,160);
tft.setTextSize(3);
 
//pH display
tft.setTextSize(2);
tft.drawString("PH",225,65);
tft.setTextSize(3);

//Dispensor
tft.setTextSize(2);
tft.drawString("Dispensed",180,160);
tft.setTextSize(3);

//Sets text color to blue 
tft.setTextColor(TFT_BLUE);
tft.drawString("C",90,95);
tft.drawString("%",95,190);
tft.drawString("pH",240,95);
}

Display the AquaCare logo on the Wio Terminal screen
void Screen_logo(){
tft.fillRect(0,65,320,120,TFT_BLUE);  //Fills a blue rectangle at the top of the Wio Terminal screen
tft.setTextSize(4);
tft.setTextColor(TFT_WHITE);
tft.setCursor((320 - tft.textWidth("AquaCare")) / 2, 110);
tft.print("AQUACARE");
tft.drawFastHLine(0,100,320,TFT_GREEN); //Drawing horizontal line
tft.drawFastHLine(0,150,320,TFT_GREEN); //Drawing horizontal line
}

Display a message indicating the device is connecting to Wi-Fi
void Screen_connectingWiFi(){
tft.setTextSize(3);
tft.setTextColor(TFT_BLACK);
tft.setTextSize(2);
tft.setCursor((320 - tft.textWidth("Connecting to Wi-Fi..")) / 2, 25);
tft.print("Connecting to Wi-Fi..");
}
Display a message indicating when the device is connected
void Screen_connected(){
  tft.setTextColor(TFT_BLUE);
  tft.setTextSize(3);
  tft.setCursor((320 - tft.textWidth("Connected!")) / 2, 115);
  tft.print("Connected!");
}

Display a message indicating the device is connecting to broker
void Screen_connectingMQTT(){
tft.setTextSize(3);
tft.setTextColor(TFT_BLACK);
tft.setTextSize(2);
tft.setCursor((320 - tft.textWidth("Connecting to MQTT")) / 2, 25);
tft.print("Connecting to MQTT");
}

// Function to display the results on the screen.
void Screen_result(int tempResult, int lightResult, int phResult, int dispenserUsageCount){
 tft.fillScreen(TFT_WHITE);
  tft.drawNumber(tempResult,50,95); 
  tft.drawNumber(lightResult,50,190); 
  tft.drawNumber(pinPhSensor,210,95); 
  tft.drawNumber(dispenserUsageCount,225,190);
}
