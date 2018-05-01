
/**
 * This class represents an beamer which may be put
 * in a room in the game of Zuul.
 * 
 * @author Ahmad Chaudhry
 * @version November 11, 2017 
 */

public class Beamer extends Item
{
    //the room that will store a beamers charge
    protected Room room;
    
    /**
     * Constructor for objects of class Beamer
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Beamer(String description, double weight, String name)
    {
        //uses superclass contructor
        super(description, weight, name);
        //beamers charged room is null 
        room = null;
    }
    
    /**
     * Charges the beamer in the current room
     * 
     * @param room The room you are going to charge in
     */
    public void charge (Room room)
    {
        this.room = room;
    }
    
    /**
     * Fires the beamer to send player to charged room
     * 
     * @return The room the beamer was charged in
     */
    public Room fire()
    {
        //the new room player gets sent to
        Room newLocation = room;
        //resets the beamer
        room = null;
        return newLocation;
    }
}
