#include "Screen_draw.h"
#include "utils.h"

void Screen_draw(){ 
//tft.fillScreen(TFT_WHITE);
tft.fillRect(0,0,320,50,TFT_BLUE); 
tft.setTextSize(2); 
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
tft.drawString("Light",25,160);
tft.setTextSize(3);
 
//pH display
tft.setTextSize(2);
tft.drawString("pH",200,65);
tft.setTextSize(3);


/*
spr.setTextSize(2);
spr.drawString(""",200,160);
spr.setTextSize(3);
spr.drawNumber(,205,190);   
spr.drawString("",245,190);
*/

//style 
tft.setTextColor(TFT_BLUE);
tft.drawString("C",90,95);
tft.drawString("%",70,190);
tft.drawString("pH",240,95);
}