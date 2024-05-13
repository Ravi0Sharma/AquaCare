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



void Screen_result(int tempResult, int lightResult, int phResult, int dispenserUsageCount){
 tft.fillScreen(TFT_WHITE);
  tft.drawNumber(tempResult,50,95); 
  tft.drawNumber(lightResult,50,190); 
  tft.drawNumber(pinPhSensor,210,95); 
  tft.drawNumber(dispenserUsageCount,225,190);
}
