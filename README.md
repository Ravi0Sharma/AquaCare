# **AquaCare**

[[_TOC_]]

## **Description**

AquaCare offers an aquarium monitoring system designed to assist in maintaining fish and plant life in a well-nurtured environment. The system collects readings using pH and temperature sensors to provide users with detailed historical data on temperature, pH and light levels.

AquaCare offers products tailored to owners with specific needs, including species highly sensitive to temperature, light and pH fluctuations. Additionally, it provides a solution for anyone who wants to be notified of an excessive levels which can be harmful to aquatic life.

To save time and ensure proper care, AquaCare notifies users when monitored levels (such as temperature, light or pH) exceed set thresholds, indicating the need for adjustments to protect aquatic life from hazardous conditions. AquaCare also includes an automated food dispenser for customers who prefers to feed their fish remotely.

AquaCare's historical data can be used to offer potential buyers detailed insights into how fish and plants have been cared for, displaying their habitat conditions during ownership.

Our application serves as a central hub for aquarium monitoring, offering real-time sensor readings and a comprehensive overview of the health and conditions of aquatic life.

<!--- 
## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.
-->

## **Installation**

## Installation of the Application

### Prerequisites
Before installing the application, ensure you have the following prerequisites installed on your system:

Java Development Kit (JDK) version 19 or higher: [Download JDK](https://www.oracle.com/java/technologies/downloads/#java19)

### Downloading the application
To download the application, check [Relases](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/releases) tab. There you can download the most recent relase. 

After downloading the .zip file, extract the contents using [7zip](https://www.7-zip.org/)

Running the .jar or .exe file should start the application.

### Downloading the source code

To download the source code, check [Relases](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/releases) tab. There, under the assets menu, you can find and download the most recent source code relase. 

After downloading the .zip file, extract the contents using [7zip](https://www.7-zip.org/)

## Installation of Wio Terminal Script

### Dependencies

1. [Wio Terminal](https://wiki.seeedstudio.com/Wio-Terminal-Getting-Started/)
2. [Arduino IDE](https://www.arduino.cc/en/software) or [Arduino CLI](https://arduino.github.io/arduino-cli/0.35/installation/) (We recommend using IDE since it is more user friendly)
3. [Wio Terminal Board Library](https://wiki.seeedstudio.com/Wio-Terminal-Getting-Started/#software)
4. Required Libraries:
    1. [PubSubClient@2.8](https://www.arduino.cc/reference/en/libraries/pubsubclient/)
    2. [Seeed Arduino rpcUnified@2.1.4](https://www.arduino.cc/reference/en/libraries/seeed-arduino-rpcunified/)
    3. [Seeed Arduino rpcWiFi@1.0.7](https://www.arduino.cc/reference/en/libraries/seeed-arduino-rpcwifi/)
    4. [Seeed Arduino SFUD@2.0.2](https://www.arduino.cc/reference/en/libraries/seeed-arduino-sfud/)
    5. [Seeed_Arduino_mbedtls@3.0.1](https://www.arduino.cc/reference/en/libraries/seeed_arduino_mbedtls/)
    6. [ArduinoSTL](https://www.arduino.cc/reference/en/libraries/arduinostl/)
    7. [WiFiNINA](https://www.arduino.cc/reference/en/libraries/wifinina/)
    8. [Seeed Arduino rpcBLE@1.0.0](https://www.arduino.cc/reference/en/libraries/seeed-arduino-rpcble/)
    9. [Master-Servo]() (A magical file with unknown origin)
5. Sensors Used In Our Project (Exact same product may not be required):
    1. [Temperature sensor](https://wiki.seeedstudio.com/Grove-Temperature_Sensor_V1.2/)
    2. [Garsent Digital pH Sensor](https://www.amazon.se/-/en/Garsent-Digital-Composite-ElectrodeAquaculture/dp/B07QKK1XB6) 
    3. [Grove Light Sensor](https://wiki.seeedstudio.com/Grove-Light_Sensor/)
    4. [MMOBIEL Servo Motor](https://www.amazon.se/-/en/Micro-Servo-Motor-Kit-Radio-Controlled/dp/B097RD8RB7/?th=1)

## **Roadmap**
For a detailed view of expected future releases and upcoming features, please refer to [Milestones](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/milestones).

## **Contributing**

In the [Contributions.md](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/blob/main/CONTRIBUTING.md) file, you'll find general guidelines for participating in this project. This includes steps on how to propose changes to the project, how to submit a pull request, and the process for reviewing and merging that request. For more detailed information on each contribution type and specific instructions, please refer to the [contributions.md](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/blob/main/CONTRIBUTING.md) file.

## **Authors and acknowledgment**
- Ahmet Yavuz Kalkan([@ahmety](https://git.chalmers.se/ahmety)): Made substantial contributions to backend utilities for the application.

- Süeda Nalan Tahtaci([@sueda](https://git.chalmers.se/sueda)): Made substantial contributions to the front-end.

- Ravi Sharma([@ravisha](https://git.chalmers.se/ravisha)): Led the team as project manager and made substantial contributions to the backend utilities for the Wio Terminal and application.

<!---
## **Support**
[Buy us a coffee](https://www.coop.se/handla/varor/dryck/kaffe/bryggkaffe/bryggkaffe-mellanrost-8711000530085)
-->

## **License**
This project is under MIT license, to read more refer to [License](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/blob/main/LICENSE.MD)
