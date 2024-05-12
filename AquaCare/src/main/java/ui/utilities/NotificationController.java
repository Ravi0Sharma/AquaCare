package ui.utilities;

import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.influxdb.query.FluxRecord;

import com.influxdb.query.FluxTable;

public class NotificationController {

    private final ApplicationInterface appInterface;

    //Expected Structure: deviceID, <sensor, (lower threshold, upper threshold)>
    HashMap<String, HashMap<String, Threshold>> deviceMap;

    private int counter;

    public NotificationController() {

        appInterface = new ApplicationInterface();

    }

    //Returns true if the thresholds are not breached
    public static void checkThresholds(double meanTemp, double meanPh) {
        if (CurrentFish.getInstance().getSelectedFish() == null) {
            System.out.println("No fish selected.");
            return;
        }

        if (isWithinThreshold(CurrentFish.getInstance().getSelectedFish().getFishTemp(), meanTemp)
                && isWithinThreshold(CurrentFish.getInstance().getSelectedFish().getFishPh(), meanPh)) {

            System.out.println("Thresholds are not breached.");
            return;
        }
        System.out.println(CurrentFish.getInstance().getSelectedFish().getFishName());
        System.out.println(CurrentFish.getInstance().getSelectedFish().getFishSpecies());
        System.out.println(CurrentFish.getInstance().getSelectedFish().getFishPh());
        System.out.println(CurrentFish.getInstance().getSelectedFish().getFishLight());
        System.out.println(CurrentFish.getInstance().getSelectedFish().getFishTemp());


        NotificationClient.displayTray(
                "Fish " + CurrentFish.getInstance().getSelectedFish().getFishName(), "Threshold Breach with values of Temperature and pH: " + meanTemp + " and " + meanPh);
    }

    private static boolean isWithinThreshold(String thresholdString, double value) {
        // Split the string by the "-" character
        String[] parts = thresholdString.split("-");

        // Remove any non-numeric characters from the maximum value string
        String maxValueString = parts[1].replaceAll("[^\\d.]", "");

        // Parse the minimum and maximum value strings into doubles
        double minValue = Double.parseDouble(parts[0]);
        double maxValue = Double.parseDouble(maxValueString);

        System.out.println("Min: " + minValue + " Max: " + maxValue + " Value: " + value);

        // Compare the value with the minimum and maximum values
        return value >= minValue && value <= maxValue;
    }
}