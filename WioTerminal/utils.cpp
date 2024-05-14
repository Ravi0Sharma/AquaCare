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

// Map a light to a percentage scale (0-100)
int mapToPercentage(int signal) {
return map(signal, MIN_READING, MAX_READING, 0, 100);
}             

// Controls a servo motor to dispense food.
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


