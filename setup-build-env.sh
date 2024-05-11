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
arduino-cli lib install "PubSubClient@2.8"
arduino-cli lib install "Seeed Arduino rpcUnified@2.1.4"
arduino-cli lib install "Seeed Arduino rpcWiFi@1.0.7"
arduino-cli lib install "Seeed Arduino SFUD@2.0.2"
arduino-cli lib install "Seeed_Arduino_mbedtls@3.0.1"
arduino-cli lib install "ArduinoSTL"
arduino-cli lib install "WiFiNINA"
arduino-cli lib install "Seeed Arduino rpcBLE@1.0.0"
#arduino-cli lib install "Servo@1.2.1"
#arduino-cli lib install "Seeed Arduino FS@2.1.1"   # For SD card

apt-get install git -y
#mkdir -p /root/Arduino/libraries
git clone https://github.com/PaintYourDragon/Servo.git /root/Arduino/libraries/Servo

#cd -
#git clone https://gist.github.com/4033545.git      # Install math.h

