package ui.utilities;

import java.awt.*;

public class NotificationClient {

    public void displayTray(String alertTitle, String alertBody) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        //Set the tray icon image and tooltip
        TrayIcon trayIcon = new TrayIcon(image, "Alert");

        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);

        //Set tooltip text for the tray icon
        //trayIcon.setToolTip("Alert");

        tray.add(trayIcon);

        trayIcon.displayMessage(alertTitle, alertBody, TrayIcon.MessageType.INFO);
    }




}
