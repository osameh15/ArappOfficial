package ir.arapp.arappofficial.Data;

public class SliderItem
{
    //Variables
    private int id;
    private int show;
    private String text;
    private String url;
    private String createdAt;
    private String updatedAt;

    //Constructor
    public SliderItem(int id, int show, String text, String url, String createdAt, String updatedAt)
    {
        this.id = id;
        this.show = show;
        this.text = text;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getShow()
    {
        return show;
    }

    public void setShow(int show)
    {
        this.show = show;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
