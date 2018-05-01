
/**
 * This class represents an item which may be put
 * in a room in the game of Zuul.
 * 
 * 
 * @author Ahmad Chaudhry
 * @version November 11, 2017 
 */
public class Item
{
    // description of the item
    private String description;
    
    // weight of the item in kilgrams 
    private double weight;
    
    //name of the item
    private String name; 

    /**
     * Constructor for objects of class Item.
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Item(String description, double weight, String name)
    {
        this.description = description;
        this.weight = weight;
        this.name = name;
    }

    /**
     * Returns a description of the item, including its
     * description and weight.
     * 
     * @return  A description of the item
     */
    public String getDescription()
    {
        return description + " that weighs " + weight + "kg.";
    }
    
    /**
     * Returns the name of the item.
     * 
     * @return  The name of the item
     */
    public String getName()
    {
        return name;
    }
    
}
