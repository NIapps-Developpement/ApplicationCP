package be.cerclepolytechnique.applicationcp;

public class ItemCal {
    private String name;
    private String date;
    private String message;
    private String photonbr;

    public ItemCal(String name, String date, String message, String photonbr) {
        this.name = name;
        this.date = date;
        this.message = message;
        this.photonbr = photonbr;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getPhotonbr() {
        return photonbr;
    }

    public void setPhotonbr(String photonbr) {
        this.photonbr = photonbr;
    }
}
