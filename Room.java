import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList; // or java.util.*; and replace the above

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * 
 * @author Ahmad Chaudhry  
 * @version November 11, 2017
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.

    // the items in this room
    private ArrayList<Item> items;
    //the ArrayList used to store the rooms for transporter room
    protected static ArrayList<Room> rooms = new ArrayList<Room>();
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new ArrayList<Item>();
        //each new room gets added to the rooms ArrayList
        rooms.add(this);
    }
    
    /**
     * Add an item to the room, best to check that it's not null.
     * 
     * @param item The item to add to the room
     */
    public void addItem(Item item) 
    {
        if (item!=null) { // not required, but good practice
            items.add(item);
        }
    }

    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit
     * @param neighbour The room to which the exit leads
     */
    public void setExit(String direction, Room neighbour) 
    {
        exits.put(direction, neighbour);
    }

    /**
     * Returns a short description of the room, i.e. the one that
     * was defined in the constructor
     * 
     * @return The short description of the room
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     Items: 
     *        a chair weighing 5 kgs.
     *        a table weighing 10 kgs.
     *     
     * @return A long description of this room
     */
    public String getLongDescription(Item carriedItem)
    {
        String longdescription;
        
        longdescription = "You are " + description + ".\n" + getExitString()
        + "\nItems:" + getItems();
        //you are not holding any item
        if (carriedItem == null)
        {
           longdescription += "\nCarried Item: " + "\n    Empty"; 
        }
        //you are holding a item
        else 
        {
             longdescription += "\nCarried Item: " + "\n    " + carriedItem.getName();
        }
        return longdescription;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction The exit's direction
     * @return The room in the given direction
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Return a String representing the items in the room, one per line.
     * 
     * @return A String of the items, one per line
     */
    public String getItems() 
    {
        // let's use a StringBuilder (not required)
        StringBuilder s = new StringBuilder();
        for (Item i : items) {
            s.append("\n    " + i.getDescription());
        }
        return s.toString(); 
    }
    
    /**
     * Return a item that is removed from the Item list.
     * 
     * @param The name of the item being removed
     * @return A item that has been removed
     */
    public Item removeItem(String nameitem) 
    {
        for (int i = 0; i < items.size(); i++)
        {
            if (nameitem.equals(items.get(i).getName()))
            {
                return items.remove(i);
            }
        }
        return null;
    }
    
    /**
     * Return a item that is stored in the Item list.
     * 
     * @param The name of the item being removed
     * @return A item that is trying to be found
     */
    public Item getItem(String nameitem) 
    {
        for (int i = 0; i < items.size(); i++)
        {
            if (nameitem.equals(items.get(i).getName()))
            {
                return items.get(i);
            }
        }
        return null;
    }
    
    /**
     * Return true if an item exists in the Item list.
     * 
     * @param The name of the item being removed
     * @return True if item exists else false
     */
    public boolean hasItem (String nameitem)
    {
        for (int i = 0; i < items.size(); i++)
        {
            if (nameitem.equals(items.get(i).getName()))
            {
                return true;
            }
        }
            return false;
    }
}

