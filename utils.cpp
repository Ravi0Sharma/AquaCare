#include "pin.h"
#include "utils.h"


const int serial_Begin_Rate = 9600;

// variable to store the servo position
int pos = 0;    


// convert the analog value to celcius 
int convertTemp(int temp){
float R = (1023.0 / temp - 1.0 ) * 100000 ;
return temp = 1.0/(log(R/100000)/4275+1/298.15)-273.15;
}


int mapToPercentage(int signal) {
return map(signal, MIN_READING, MAX_READING, 0, 100);
}                                

int convertPh(int valuePh){
    long sensorSum;
     for(int m=0; m < 50;m++){
        sensorSum += valuePh;
    }
    valuePh =   sensorSum/50;
    
    return valuePh = (7-1000*(valuePh-372)*Vref/59.16/1023);
} 


void servo(){
  for (pos = 0; pos <= 90; pos += 1) { 
    dispenser.write(pos);              
    delay(4);                      
  }
  for (pos = 90; pos >= 0; pos -= 1) { 
    dispenser.write(pos);              
    delay(4);          
  }
}


