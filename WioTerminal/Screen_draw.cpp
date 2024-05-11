#include "Screen_draw.h"
#include "utils.h"

void Screen_draw(){ 

//Fills a blue rectangle at the top of the Wio Terminal screen.
tft.fillRect(0,0,320,50,TFT_BLUE); 

// vertical and horizontal line
tft.drawFastVLine(150,50,190,TFT_BLUE); //Drawing vertical line
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

//Dispenser
tft.setTextSize(2);
tft.drawString("Dispensed",180,160);
tft.setTextSize(3);

//Sets text color to blue 
tft.setTextColor(TFT_BLUE);
tft.drawString("C",90,95);
tft.drawString("%",95,190);
tft.drawString("pH",240,95);
}