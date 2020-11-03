package ir.arapp.arappofficial.Data;

public class NotificationItem
{
    //Variables
    private int id;
    private String title;
    private String subtitle;
    private String text;
    private String image;
    private int visible;
    private String createdAt;

    //Constructor

    public NotificationItem(int id, String title, String subtitle, String text, String image, int visible, String createdAt)
    {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.text = text;
        this.image = image;
        this.visible = visible;
        this.createdAt = createdAt;
    }


    //Getter functions

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public String getText()
    {
        return text;
    }

    public String getImage()
    {
        return image;
    }

    public int getVisible()
    {
        return visible;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }
}
