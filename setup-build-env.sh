# Credits to Pasha Klimenkov for writing esp32 CI guide: https://codeblog.dotsandbrackets.com/gitlab-ci-esp32-arduino/
#!/bin/bash

apt-get update
cd ~

# Install arduino-cli
apt-get install curl -y
curl -fsSL https://raw.githubusercontent.com/arduino/arduino-cli/master/install.sh | sh
export PATH=$PATH:/root/bin
arduino-cli -version

# Install Seeed Wio Terminal core
printf "board_manager:\n  additional_urls:\n    - https://files.seeedstudio.com/arduino/package_seeeduino_boards_index.json\n" > .arduino-cli.yaml
arduino-cli core update-index --config-file .arduino-cli.yaml
arduino-cli core install Seeeduino:samd --config-file .arduino-cli.yaml

# Install 'native' packages (libraries that do not come with the core)

# Define the libraries to be installed
libraries=(
    "PubSubClient@2.8"
    "Servo@1.2.1"
#    "Seeed Arduino FS@2.1.1"               # To write on SD card
    "Seeed Arduino rpcUnified@2.1.4"
    "Seeed Arduino rpcWiFi@1.0.7"
#    "Seeed Arduino RTC@2.0.0"              # Realtime clock component
    "Seeed Arduino SFUD@2.0.2"
    "Seeed_Arduino_mbedtls@3.0.1"
    "ArduinoSTL"
    "WiFiNINA"
#    "TFT_eSPI"
)
for lib in "${libraries[@]}"; do
    arduino-cli lib install "$lib"    # Install each library using arduino-cli
done

arduino-cli lib list

#cp math.h /usr/include/math.h

cd -
apt-get install git -y
cd arduino-cli config dump | grep sketchbook | sed 's/.*\ //'
ls -l
cd Arduino/libraries
#cp MQTT.h /root/Arduino/libraries/PubSubClient/src
git clone https://github.com/arduino-libraries/Servo.git
git clone https://gist.github.com/4033545.git

arduino-cli lib list

## Install 'third-party' packages / libraries: find proper location and 'git clone'
# apt-get install git -y
# cd `arduino-cli config dump | grep sketchbook | sed 's/.*\ //'`/libraries
# git clone https://github.com/ThingPulse/esp8266-oled-ssd1306.git
# git clone https://github.com/Seeed-Studio/Seeed_Arduino_RTC.git
