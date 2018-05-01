import java.util.Random;
/**
 * Class TransporterRoom - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "TransporterRoom" represents one location in the scenery of the game.  It is 
 * connected to other rooms via a random generator.  For each existing exit, the transporter room
 * stores a number to generate one room to be transported to.
 * 
 * @author Ahmad Chaudhry  
 * @version November 11, 201
 *
 */
public class TransporterRoom extends Room
{
    Random r = new Random();
    
    /**
     * Constructor for objects of class transporterRoom
     */
    public TransporterRoom(String description)
    {
        super(description);
    }
    
    /**
     * Returns a random room, independent of the direction parameter.
     *
     * @param direction Ignored.
     * @return A randomly selected room.
     */
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }

    /**
     * Choose a random room.
     *
     * @return The room we end up in upon leaving this one.
     */
    private Room findRandomRoom()
    {
        //decides a random room to go to based on room size
       int i = r.nextInt(rooms.size());
       return rooms.get(i);
    }
}
