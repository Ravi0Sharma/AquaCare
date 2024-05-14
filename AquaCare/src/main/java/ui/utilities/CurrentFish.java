package ui.utilities;

import ui.aquacare.FishModel;

public class CurrentFish {
    //Singleton class to store the currently selected fish
    private static CurrentFish instance = null;
    private FishModel selectedFish;

    private CurrentFish() {
        // Private constructor to prevent instantiation of the singleton
    }

    public static CurrentFish getInstance() {
        if (instance == null) {
            instance = new CurrentFish();
        }
        return instance;
    }

    public FishModel getSelectedFish() {
        return selectedFish;
    }

    public void setSelectedFish(FishModel selectedFish) {
        this.selectedFish = selectedFish;
    }
}