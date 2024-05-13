#include <math.h>
#include "pin.h"
#include <Servo.h>
#include <Arduino.h>

#define MAX_READING 1023          // Maximum analog reading
#define MIN_READING 0             // Minimum analog reading


extern Servo dispenser;           // External declaration for the servo motor

extern const int serial_Begin_Rate;  // External declaration for the serial begin rate

extern int mapToPercentage(int signal);  // External declaration for mapping signal to percentage
extern int convertTemp(int temp);        // External declaration for converting temperature
extern int convertPh(int ph);            // External declaration for converting pH
extern void servo();              