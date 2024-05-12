package ui.aquacare;

public class FishModel {

    //  declare variables that represents the fields in fish.json
    private String fishName;
    private String fishSpecies;
    private String favoriteFood;
    private String fishURL;
    private String fishPh;
    private String fishTemp;
    private String fishLight;

    public FishModel(String fishName, String fishSpecies, String favoriteFood, String fishURL, String fishPh, String fishTemp, String fishLight) {
        this.fishName = fishName;
        this.fishSpecies = fishSpecies;
        this.favoriteFood = favoriteFood;
        this.fishURL = fishURL;
        this.fishPh = fishPh;
        this.fishTemp = fishTemp;
        this.fishLight = fishLight;
    }

    public String getFishName() {
        return fishName;
    }
    public void setFishName(String fishName) {
        this.fishName = fishName;
    }
    public String getFishSpecies() {
        return fishSpecies;
    }
    public void setFishSpecies(String fishSpecies) {
        this.fishSpecies = fishSpecies;
    }
    public String getFavoriteFood() {
        return favoriteFood;
    }
    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }
    public String getFishURL() {
        return fishURL;
    }
    public void setFishURL(String fishURL) {
        this.fishURL = fishURL;
    }
    public String getFishPh() {
        return fishPh;
    }
    public void setFishPh(String fishPh) {
        this.fishPh = fishPh;
    }
    public String getFishTemp() {
        return fishTemp;
    }
    public void setFishTemp(String fishTemp) {
        this.fishTemp = fishTemp;
    }
    public String getFishLight() {
        return fishLight;
    }
    public void setFishLight(String fishLight) {
        this.fishLight = fishLight;
    }

}
