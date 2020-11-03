package ir.arapp.arappofficial.Data;

public class HomeServicesData
{
    private int id;
    private int userID;
    private int currentUserID;
    private String email;
    private String phone;
    private String title;
    private String intro;
    private String name;
    private String text;
    private int image;
    private String date;
    private double rate;
    private String category;
    private String city;
    private String province;
    private int rate_count;
    private int special;
    private int allComment;
    private int allLikes;
    private String address;
    private String specialText;

    public HomeServicesData(int id, int userID, int currentUserID, String email, String phone, String title, String intro, String name,
                            String text, int image, String date, double rate, String category, String city, String province, int rate_count,
                            int special, int allComment, int allLikes, String address, String specialText)
    {
        this.id = id;
        this.userID = userID;
        this.currentUserID = currentUserID;
        this.email = email;
        this.phone = phone;
        this.title = title;
        this.intro = intro;
        this.name = name;
        this.text = text;
        this.image = image;
        this.date = date;
        this.rate = rate;
        this.category = category;
        this.city = city;
        this.province = province;
        this.rate_count = rate_count;
        this.special = special;
        this.allComment = allComment;
        this.allLikes = allLikes;
        this.address = address;
        this.specialText = specialText;
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

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getTitle()
    {
        return title;
    }

    public String getIntro()
    {
        return intro;
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

    public String getCategory()
    {
        return category;
    }

    public String getCity()
    {
        return city;
    }

    public String getProvince()
    {
        return province;
    }

    public int getRate_count()
    {
        return rate_count;
    }

    public int getSpecial()
    {
        return special;
    }

    public String getAllComment()
    {
        return String.valueOf(allComment);
    }

    public String getAllLikes()
    {
        return String.valueOf(allLikes);
    }

    public String getAddress()
    {
        return address;
    }

    public String getSpecialText()
    {
        return specialText;
    }
}
