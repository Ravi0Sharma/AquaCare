plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    //Project dependecy: Java-MQTT client
    implementation("com.influxdb:influxdb-client-java:7.0.0")

    //Project dependecy: Java-InfluxDB client
    implementation("io.joynr.java.messaging.mqtt:paho-mqtt-client:1.14.2")
}

application {
    // Define the main class for the application.
    mainClass.set("backend.Main")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "backend.Main"
    }
}