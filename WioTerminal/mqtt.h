#include <MQTT.h>
#include "PubSubClient.h"

extern PubSubClient client; 
extern const char* mqtt_server;

extern PubSubClient client; 
extern const char* TOPIC_SUB;
extern void MQTT_connect(); 

extern void callback(char* topic, byte* payload, unsigned int length);

extern void reconnect();

// topics for sending sensor data

extern const char* TOPIC_PUB_TEMP;
extern const char* TOPIC_PUB_PH;
extern const char* TOPIC_PUB_LIGHT;
extern const char* TOPIC_PUB_FOOD; 

// topics for receiving user sensor ranges
extern const char* TOPIC_SUB_TEMP;
extern const char* TOPIC_SUB_PH;
extern const char* TOPIC_SUB_LIGHT;
extern const char* TOPIC_SUB_FOOD;

