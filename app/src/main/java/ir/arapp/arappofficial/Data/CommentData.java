package ir.arapp.arappofficial.Data;

public class CommentData
{
    private int id;
    private int userID;
    private int currentUserID;
    private int adsUserID;
    private String email;
    private String phone;
    private String name;
    private String text;
    private int image;
    private String date;
    private double rate;

    public CommentData(int id, int userID, int currentUserID, int adsUserID, String email, String phone, String name, String text, int image, String date, double rate)
    {
        this.id = id;
        this.userID = userID;
        this.currentUserID = currentUserID;
        this.adsUserID = adsUserID;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.text = text;
        this.image = image;
        this.date = date;
        this.rate = rate;
    }

    public String getId()
    {
        return String.valueOf(id);
    }

    public String getUserID()
    {
        return String.valueOf(userID);
    }

    public String getCurrentUserID()
    {
        return String.valueOf(currentUserID);
    }

    public int getAdsUserID()
    {
        return adsUserID;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getName()
    {
        return name;
    }

    public String getText()
    {
        return text;
    }

    public int getImage()
    {
        return image;
    }

    public String getDate()
    {
        return date;
    }

    public double getRate()
    {
        return rate;
    }
}
