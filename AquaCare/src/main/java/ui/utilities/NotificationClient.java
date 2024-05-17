package ui.utilities;

import java.awt.*;

public class NotificationClient {

    public static void displayTray(String alertTitle, String alertBody) {

        try {
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        //Set the tray icon image and tooltip
        TrayIcon trayIcon = new TrayIcon(image, "Alert");

        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);

        tray.add(trayIcon);

        trayIcon.displayMessage(alertTitle, alertBody, TrayIcon.MessageType.INFO);
    }
    catch (AWTException e) {
        System.err.println("TrayIcon could not be added.");
    }

    }




}
