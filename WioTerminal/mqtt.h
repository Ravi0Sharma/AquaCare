#include "PubSubClient.h"   

extern PubSubClient client;  // Declaration of MQTT client object
extern const char* mqtt_server; // MQTT server address
extern const char* TOPIC_SUB;  // Topic for subscribing to MQTT messages

extern void MQTT_connect(); 

extern void callback(char* topic, byte* payload, unsigned int length); // Declaration of MQTT callback function

// topics for sending sensor data
extern const char* TOPIC_PUB_TEMP;
extern const char* TOPIC_PUB_PH;
extern const char* TOPIC_PUB_LIGHT;
extern const char* TOPIC_PUB_FOOD; 


extern const char* TOPIC_SUB_FOOD; // Topic for receiving user requests to dispense food


