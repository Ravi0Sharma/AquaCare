#include <math.h>
#include "pin.h"
//#include <Servo.h>
#include <Arduino.h>

#define MAX_READING 1023           
#define MIN_READING 0
#define Vref 4.95

//extern Servo dispenser;

extern const int serial_Begin_Rate;
extern int mapToPercentage(int signal);
extern int convertTemp(int temp);
extern int convertPh(int ph);
//extern void servo();
