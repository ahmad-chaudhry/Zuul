import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * 
 * @author Ahmad Chaudhry
 * @version November 11, 2017
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    //item that the user carries
    private Item carriedItem;
    // amount of energy user has 
    private int energy;
    //beamer 1 and 2 found in rooms of the game
    private Beamer beamer1;
    private Beamer beamer2;
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        //user starts with no item
        carriedItem = null;
        //user starts with 5 energy 
        energy = 5;
        
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        //random Room initalized with other rooms
        Room outside, theatre, pub, lab, office, random;
        //cookie initalized with other items
        Item chair, bar, computer, computer2, tree, cookie;
        
        // create some items
        chair = new Item("a wooden chair",5, "chair");
        bar = new Item("a long bar with stools",95.67, "bar");
        computer = new Item("a PC",10, "PC");
        computer2 = new Item("a Mac",5, "Mac");
        tree = new Item("a fir tree",500.5, "tree");
        //cookie created
        cookie = new Item("a chocolate chip cookie", 0.5, "cookie");
        
        //create beamer items
        beamer1 = new Beamer("a beamer1", 5, "beamer1");
        beamer2 = new Beamer("a beamer2", 5, "beamer2");

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        //create random room
        random = new TransporterRoom("in a room that will transport you to a random room");
        
        // put items in the rooms
        outside.addItem(tree);
        outside.addItem(tree);
        theatre.addItem(chair);
        theatre.addItem(bar);
        pub.addItem(bar);
        //cookies added to pub
        pub.addItem(cookie);
        pub.addItem(cookie);
        pub.addItem(cookie);
        lab.addItem(chair);
        lab.addItem(computer);
        lab.addItem(chair);
        lab.addItem(computer2);
        office.addItem(chair);
        office.addItem(computer);
        //cookie added to lab
        lab.addItem(cookie);
        //placed beamers in rooms
        outside.addItem(beamer1);
        lab.addItem(beamer2);
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        //set exits for random room
        outside.setExit("north", random);
        random.setExit("south", outside);
        
        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription(carriedItem));
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        else if (commandWord.equals("fire")) {
            fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription(carriedItem));
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            System.out.println(currentRoom.getLongDescription(carriedItem));
        }
    }
    
    /** 
     * "eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Eat what?");
        }
        //you are not holding an item
        else if(carriedItem == null)
        {
            System.out.println("You have no food to eat");
        }
        //if you're not holding a cookie as your carried item
        else if (!"cookie".equals(carriedItem.getName())) 
        {
            System.out.println("You have no food to eat");
        }
        else
        {
            //you tell the game to eat the cookie
            if (command.getSecondWord().equals("cookie"))
            {
                //you gain 5 energy
                energy += 5;
                System.out.println("You have eaten the cookie and are no longer hungry.");
                //your cookie is eaten
                carriedItem = null;
            }
            // you ask to eat something else
            else 
            {
                System.out.println("You have no food to eat");
            }
        
        }
    }
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(currentRoom.getLongDescription(carriedItem));
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(currentRoom.getLongDescription(carriedItem));
            }
        }
    }
    
    /** 
     * "take" was entered. Check the rest of the command to see
     *  whether we really want to take an item.
     * 
     * @param command The command to be processed
     */
    private void take(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("take what?");
        }
        //you are already holding an item
        else if (carriedItem != null)
        {
            System.out.println("you are already holding an item");
        }
        //the item you are asking to carry does not exist in your current room
        else if (!currentRoom.hasItem(command.getSecondWord()))
        {
            System.out.println("that item is not in the room");
        }
        //you have no energy and want to pick up a cookie
        else if (energy <= 0 && currentRoom.hasItem(command.getSecondWord()) && 
        command.getSecondWord().equals("cookie")) 
        {
            carriedItem = currentRoom.removeItem(command.getSecondWord());
            System.out.println("You picked up " + carriedItem.getName());
        }
        //you have no energy and want to pick up a item besides a cookie
        else if (energy <= 0 )
        {
            System.out.println("you have no energy left, find a cookie");
        }
        //you pick up the item in the room that you are requesting
        else if (currentRoom.hasItem(command.getSecondWord())) 
        {
           //item gets stored with player and removed from the room
           carriedItem = currentRoom.removeItem(command.getSecondWord());
           System.out.println("You picked up " + carriedItem.getName());
           //you lose one energy for picking up a item
           energy--;
        }
    }
    
    /** 
     * "drop" was entered. Check the rest of the command to see
     *  whether we really want to drop our current item.
     * 
     * @param command The command to be processed
     */
    private void drop(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("drop what?");
        }
        //you are carrying no item
        else if (carriedItem == null)
        {
            System.out.println("You have nothing to drop");
        }
        //you are carrying a item you want to drop
        else if (carriedItem != null) 
        {
            //you add the carried item to the room you're currently in
            currentRoom.addItem(carriedItem);
            System.out.println("You dropped " + carriedItem.getName());
            //the item you were holding gets removed
            carriedItem = null;
        }
    }
    
    /** 
     * "charge" was entered. Check the rest of the command to see
     *  whether we really want to charge a beamer.
     * 
     * @param command The command to be processed
     */
    private void charge(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("charge what?");
            return;
        }
        //if you are holding beamer1
        if (carriedItem == beamer1)
        {
            //charges beamer1
            beamer1.charge(currentRoom);
            System.out.println("Beamer charged");
        }
        //if you are holding beamer2
        else if (carriedItem == beamer2)
        {
            //charges beamer2
            beamer2.charge(currentRoom);
            System.out.println("Beamer charged");
        }
    }
    
    /** 
     * "fire" was entered. Check the rest of the command to see
     *  whether we really want to fire a beamer.
     * 
     * @param command The command to be processed
     */
    private void fire(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("fire what?");
            return;
        }
        //if you are holding beamer1
        if (carriedItem == beamer1)
        {
            //you have not charged beamer1 
            if (beamer1.room == null)
            {
                System.out.println("You tried to fire a discharged Beamer");
            }
            //you will be sent to the room beamer1 was charged in
            else
            {
                //new room becomes the charged room
                currentRoom = beamer1.room;
                //beamer gets reset
                beamer1.room = null;
                System.out.println(currentRoom.getLongDescription(carriedItem));
            }
        }
        //you are holding beamer2
        else if (carriedItem == beamer2)
        {
            //you have not charged beamer2
            if (beamer2.room == null)
            {
                System.out.println("You tried to fire a discharged Beamer");
            }
            //you will be sent to the room beamer2 was charged in
            else
            {
                //new room becomes the charged room
                currentRoom = beamer2.room;
                //beamer gets reset
                beamer2.room = null;
                System.out.println(currentRoom.getLongDescription(carriedItem));
            }
        }
        
    }
    
}
