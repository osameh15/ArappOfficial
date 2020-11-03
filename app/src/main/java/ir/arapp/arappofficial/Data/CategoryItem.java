package ir.arapp.arappofficial.Data;

public class CategoryItem
{
    //Variables
    private int id;
    private String text;
    private String url;

    //Constructor
    public CategoryItem(int id, String text, String url)
    {
        this.id = id;
        this.text = text;
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
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
}
