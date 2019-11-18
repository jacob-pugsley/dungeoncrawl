package client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;



import jig.Entity;
import jig.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.Vector;

public class Level1 extends BasicGameState {
    private Boolean paused;
    Character knight;

    int currentOX;
    int currentOY;

    Vector currentOrigin;

    private Random rand;
    
    private int[][] rotatedMap;

    Socket socket;
    ObjectInputStream dis;
    ObjectOutputStream dos;
    String serverMessage;

    private StateBasedGame game;
    
    private final int messageTimer = 2000;

    
    private ArrayList<Item> itemsToRender;
    
    //whether to display player inventory/codex on the screen
    private boolean displayInventory = false;
    private boolean displayCodex = false;
    private int itemx = 0;
    private int itemy = 0; //which item is currently selected in the inventory
    private int selectedEquippedItem = 0; //item selected in the hotbar
    
    private class Message{
    	protected int timer = messageTimer;
    	protected String text;
    	protected Message(String text){
    		this.text = text;
    	}
    }
    
    //array of messages to print to the screen
    private Message[] messagebox;
    private int messages = 4; //number of messages printed at one time

    @Override
    public int getID() {
        return Main.LEVEL1;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        serverMessage = "";
        Main dc = (Main) game;
        if(dc.socket == null){
            System.out.println("ERROR: Make sure you start the server before starting the client!");
            System.exit(1);
        }
        paused = false;
        dc.tilesWide = dc.ScreenWidth/dc.tilesize;
        dc.tilesHigh = dc.ScreenHeight/dc.tilesize;

        messagebox = new Message[messages]; //display four messages at a time

            // TODO This section is the original map generator.
//            dc.map = client.RenderMap.getDebugMap(dc);
//            try {
//                dc.map = client.RenderMap.getRandomMap();        // grab a randomly generated map
//            } catch (IOException e) {
//                e.printStackTrace();
//            // server.Server sockets for reading/writing to server.

        this.socket = dc.socket;
        this.dis = dc.dis;
        this.dos = dc.dos;


        //dc.map = RenderMap.getDebugMap(dc);
        ///*


        // TODO this section requires that you run the server prior to client.Main.
        // Grab the map from the server.Server

        try {
            Integer[][] mapData = (Integer[][]) this.dis.readObject();
            // Convert it into an 2d int array
            dc.map = new int[mapData.length][mapData[0].length];
            for (int i = 0; i < mapData.length; i++) {
                for (int j = 0; j < mapData[i].length; j++) {
                    dc.map[i][j] = mapData[i][j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //*/
        dc.mapTiles = new Entity[dc.map.length][dc.map[0].length];      // initialize the mapTiles
        System.out.printf("Map Size: %s, %s\n", dc.map.length, dc.map[0].length);



        

        
  
        
        //rotated map verified correct
        rotatedMap = new int[dc.map[0].length][dc.map.length];
        for( int i = 0; i < dc.map.length; i++ ){
        	for( int j = 0; j < dc.map[i].length; j++ ){
        		//g.drawString(""+dc.map[i][j], j*dc.tilesize, i*dc.tilesize);
        		//i is y, j is x
        		rotatedMap[j][i] = dc.map[i][j];
        	}
        }
        
        try {
			Main.im.plant(5, rotatedMap);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        itemsToRender = Main.im.itemsInRegion(new Vector(0, 0), new Vector(100, 100));
        
        //find a tile with no walls in its horizontal adjacencies
        rand = new Random();
        rand.setSeed(System.nanoTime());
        
        int row = rand.nextInt(dc.ScreenHeight/dc.tilesize);
		int col = rand.nextInt(dc.ScreenWidth/dc.tilesize);

		while( dc.map[row][col] == 1 || wallAdjacent(row, col, dc.map) ){
			//spawn on a floor tile with 4 adjacent floor tiles
	        row = rand.nextInt(dc.ScreenHeight/dc.tilesize);
			col = rand.nextInt(dc.ScreenWidth/dc.tilesize);
		}
        
		/*
        float wx = (dc.tilesize * col) - dc.offset;// - dc.xOffset;
        float wy = (dc.tilesize * row) - dc.tilesize - dc.doubleOffset;// - dc.doubleOffset;// - dc.yOffset;
        
        knight = new Character(dc, wx, wy, "knight_iron", 1);
        dc.characters.add(knight);
        */
        // map variables
    	//Main dc = (Main) game;
        dc.mapWidth = dc.map[0].length;
        dc.mapHeight = dc.map.length;

        // setup the knight character
        float wx = (dc.tilesize * 20) - dc.offset;
        float wy = (dc.tilesize * 18) - dc.tilesize - dc.doubleOffset;
        System.out.printf("setting character at %s, %s\n", wx, wy);
        knight = new Character(dc, wx, wy, "knight_iron", 1);
        dc.characters.add(knight);
        currentOX = knight.ox;
        currentOY = knight.oy;

        // render map
        RenderMap.setMap(dc, knight);

        String coord = wx + " " + wy;
        try {
            dos.writeUTF(coord);
            dos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }

        itemsToRender = Main.im.itemsInRegion(new Vector(0, 0), new Vector(100, 100));

        // test items can be deleted
        /*
        dc.testItems.add(new DisplayItem((dc.tilesize * 4)- dc.offset, (dc.tilesize * 4)- dc.offset, "armor_gold"));
        dc.testItems.add(new DisplayItem((dc.tilesize * 5)- dc.offset, (dc.tilesize * 4)- dc.offset, "armor_iron"));
        dc.testItems.add(new DisplayItem((dc.tilesize * 6)- dc.offset, (dc.tilesize * 4)- dc.offset, "sword_iron"));
        dc.testItems.add(new DisplayItem((dc.tilesize * 7)- dc.offset, (dc.tilesize * 4)- dc.offset, "sword_wood"));
        dc.testItems.add(new DisplayItem((dc.tilesize * 8)- dc.offset, (dc.tilesize * 4)- dc.offset, "sword_gold"));
        */
        
        /*
        currentOrigin = knight.origin;
        RenderMap.setMap(dc, knight.origin);                   // renders the map Tiles
        */
    }
    
    private boolean wallAdjacent(int row, int col, int[][] map){
    	//check if there is a wall or map edge in a horizontally adjacent tile
    	try{
	    	if( map[row-1][col] == 1 ){
	    		return true;
	    	}else if( map[row+1][col] == 1 ){
	    		return true;
	    	}else if( map[row][col-1] == 1 ){
	    		return true;
	    	}else if( map[row][col+1] == 1 ){
	    		return true;
	    	}else if( map[row][col] == 1 ){
	    		return true;
	    	}
    	}catch(ArrayIndexOutOfBoundsException ex){
    		return true;
    	}
    	return false;


    }


    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    	this.game = game;
    	messagebox = new Message[messages];

    	//TODO: make the restoration boundary cover only the screen area + a buffer
    	itemsToRender = Main.im.itemsInRegion(new Vector(0, 0), new Vector(100, 100));
    }


    public void addMessage(String message){
    	//add a message to the first index of the message box
    	//  and shift everything else down
    	for( int i = messagebox.length-1; i > 0; i-- ){
    		messagebox[i] = messagebox[i-1];
    	}
    	messagebox[0] = new Message(message);
    }
    

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Main dc = (Main) game;
        // render tiles
        for (int i = 0; i < dc.map.length; i++) {
            for (int j = 0; j < dc.map[i].length; j++) {
                if (dc.mapTiles[i][j] == null)
                    continue;
                dc.mapTiles[i][j].render(g);
            }
        }
        
        //render all visible items
        g.setColor(Color.red);
        for( Item i : itemsToRender){
        	//System.out.println("Drawing item at "+i.getWorldCoordinates().getX()+", "+i.getWorldCoordinates().getY());
        	//TODO: draw item images
        	//for now, use ovals
        	//g.drawOval((i.getWorldCoordinates().getX()*dc.tilesize)+(dc.tilesize/2), (i.getWorldCoordinates().getY()*dc.tilesize)+(dc.tilesize/2), 4, 4);
        	
        	i.setPosition((i.getWorldCoordinates().getX()*dc.tilesize)+(dc.tilesize/2), (i.getWorldCoordinates().getY()*dc.tilesize)+(dc.tilesize/2));
        	i.render(g);
        	
        }

        // draw test items
        for (DisplayItem i : dc.testItems) {
            i.render(g);
        }

        knight.animate.render(g);

        
        
        //render messages
        for( Message m : messagebox ){
        	if( m != null ){
                g.setColor(new Color(0, 0, 0, 0.5f));
                g.fillRoundRect(25, dc.ScreenHeight-(20 * messagebox.length), 400, 20 * messagebox.length, 0);
                g.setColor(Color.red);
                break;
        	}
        }

        for( int i = 0; i < messagebox.length; i++ ){
        	if( messagebox[i] == null || messagebox[i].text == "" ){
        		break;
        	}
        	Color tmp = g.getColor();
        	//make the messages fade away based on their timers
        	g.setColor(new Color(255, 0, 0, (float) (messagebox[i].timer*2)/messageTimer));
        	g.drawString(messagebox[i].text, 30, dc.ScreenHeight-(20 * (messagebox.length - i)));
        	g.setColor(tmp);
        }

        
        //display player inventory
        //use the knight for now
        if( displayInventory ){
        	renderItemBox(dc, g, "Inventory", dc.tilesize, dc.tilesize, dc.tilesize*4, dc.tilesize*8);
        	ArrayList<Item> items = knight.getInventory();
        	if( items.size() != 0 ){
            	int row = 2;
            	int col = 1;
            	for( int i = 0; i < items.size(); i++ ){
            		g.drawImage(items.get(i).getImage(), col*dc.tilesize, row*dc.tilesize);
            		col++;
            		if( i > 4 && i % 4 == 0 ){
            			row++;
            			col = 1;
            		}
            	}
            	//draw a square around the selected item
            	Color tmp = g.getColor();
            	g.setColor(Color.white);
            	g.drawRect(
            			(itemx + 1)*dc.tilesize,
            			(itemy + 2)*dc.tilesize,
            			dc.tilesize,
            			dc.tilesize
            			);
            	g.setColor(tmp);
        	}
        }
        if( displayCodex ){
        	//display this box next to the item box
        	//  if it is open
        	int x = dc.tilesize;
        	if( displayInventory ){
        		x *= 4;
        	}
        	
        	renderItemBox(dc, g, "Codex", x, dc.tilesize, dc.tilesize * 10, dc.tilesize*8);
        	
        	ArrayList<Item> items = knight.getCodex();
        	int row = 2;
        	for( Item i : items ){
        		g.drawImage(i.getImage(), dc.tilesize, row*dc.tilesize);
        		Color tmp = g.getColor();
        		g.setColor(Color.white);
        		g.drawString(i.getMaterial()+" "+i.getType()+" of "+i.getEffect(), dc.tilesize*2, dc.tilesize*row + dc.tilesize*0.25f);
        		g.setColor(tmp);
        		row++;
        	}
        }
        
        //draw the player's equipped items
        Color tmp = g.getColor();
        g.setColor(new Color(0, 0, 0, 0.5f));
        g.fillRoundRect(dc.ScreenWidth-(dc.tilesize*5), dc.ScreenHeight-(dc.tilesize*2), dc.tilesize*4, dc.tilesize, 0);
        int x = 0;
        int y = 0;
        for( int i = 0; i < knight.getEquipped().length; i++ ){
        	if( knight.getEquipped()[i] != null ){
	        	x = dc.ScreenWidth-(dc.tilesize*(knight.getEquipped().length+1));
	        	y = dc.ScreenHeight-(dc.tilesize*2);
	        	g.drawImage(knight.getEquipped()[i].getImage(), x+(dc.tilesize*i), y);
        	}
        }
        g.setColor(Color.white);
        g.drawRect(x+(dc.tilesize*selectedEquippedItem), y, dc.tilesize, dc.tilesize);
        g.setColor(tmp);
        
        
        
        
        //print the tile values on the screen
        /*
        for( int i = 0; i < dc.map.length; i++ ){
        	for( int j = 0; j < dc.map[i].length; j++ ){
        		g.drawString(""+dc.map[i][j], j*dc.tilesize, i*dc.tilesize);
        		//i is y, j is x
        	}
        }
        //*/
        
        /*
        if( rotatedMap != null ){
        	for( int i = 0; i < rotatedMap.length; i++ ){
        		for( int j = 0; j < rotatedMap[i].length; j++ ){
        			g.drawString(""+rotatedMap[i][j], i*dc.tilesize, j*dc.tilesize);
        		}
        	}
        }
        //*/
        
    }
    
    private void renderItemBox(Main dc, Graphics g, String title, int x, int y, int width, int height){
    	Color tmp = g.getColor();
    	g.setColor(new Color(0, 0, 0, 0.5f));
    	
    	//create a rectangle that can fit all the player's items with 4 items per row
    	g.fillRoundRect(dc.tilesize, dc.tilesize, width, height, 0);
    	
    	g.setColor(Color.white);
    	g.drawString(title, dc.tilesize + 10, dc.tilesize + 10);
    	

    	g.setColor(tmp);

    }



    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        Input input = container.getInput();
        Main dc = (Main) game;
        Cheats.enableCheats(dc, input);
        pause(input);
        if (paused) {
            return;
        }
        knight.move(getKeystroke(input));
        /*
        if (currentOrigin.getX() != knight.origin.getX() && currentOrigin.getY() != knight.origin.getY()) {
            RenderMap.setMap(dc, knight.origin);
            currentOrigin = knight.origin;
        }
        */
        
        if( input.isKeyPressed(Input.KEY_I) ){
        	displayInventory = !displayInventory;
        	displayCodex = false;
        }
        if( input.isKeyPressed(Input.KEY_O) ){
        	displayCodex = !displayCodex;
        	displayInventory = false;
        }
        
        if( displayInventory ){
        	if( input.isKeyPressed(Input.KEY_UP) ){
        		//selectedItem.setY(selectedItem.getY()-1);
        		itemy--;
        	}else if( input.isKeyPressed(Input.KEY_DOWN) ){
        		//selectedItem.setY(selectedItem.getY()+1);
        		itemy++;
        	}else if( input.isKeyPressed(Input.KEY_LEFT) ){
        		//selectedItem.setX(selectedItem.getX()-1);
        		itemx--;
        	}else if( input.isKeyPressed(Input.KEY_RIGHT) ){
        		//selectedItem.setX(selectedItem.getX()+1);
        		itemx++;
        	}else if( input.isKeyPressed(Input.KEY_ENTER) ){
        		System.out.println("Equipping "+((itemy*4)+itemx)+"...");
        		//knight.equipItem( knight.getInventory().get((itemy*4)+itemx).getID(), 0);
        		String m = knight.equipItem((itemy*4)+itemx);
        		if( m != null ){
        			addMessage(m);
        		}
        	}
        	
        	
        	if( itemx < 0 ){
        		itemx = 0;
        	}
        	if( itemy < 0 ){
        		itemy = 0;
        	}
        	if( itemx > 3 ){
        		itemx = 3;
        	}
        	if( itemy > knight.getInventory().size()/4){
        		itemy = knight.getInventory().size()/4;
        	}
        	
        	//System.out.println(itemx+", "+itemy);
        }else{
        	if( input.isKeyPressed(Input.KEY_LEFT) ){
        		selectedEquippedItem--;
        	}else if( input.isKeyPressed(Input.KEY_RIGHT) ){
        		selectedEquippedItem++;
        	}
        	
        	if( selectedEquippedItem < 0 ){
        		selectedEquippedItem = 0;
        	}
        	if( selectedEquippedItem > knight.getEquipped().length-1 ){
        		selectedEquippedItem = knight.getEquipped().length-1;
        	}
        }
        

        //check if a character has hit an item
        for( Character ch : dc.characters ){
        	float x = (ch.animate.getX()/dc.tilesize);
        	float y = (ch.animate.getY()/dc.tilesize);
        	Vector aniPos = new Vector((int)x, (int)y);
        	
	        Item i = Main.im.getItemAt(aniPos);
	        if( i != null ){
	        	if( i.isIdentified() ){
	        		addMessage("Picked up " + i.getMaterial() + " " +i.getType() + " of " + i.getEffect() + ".");
	        	}else{
	        		addMessage("Picked up unidentified "+i.getMaterial().toLowerCase()+" "+i.getType().toLowerCase()+".");
	        	}
	        	//give removes item from the world's inventory
	        	//  and adds it to the player's inventory
	        	Main.im.give(i.getID(), ch.getPid());
	        	
	        	//stop rendering the item
	        	itemsToRender.remove(i);
	        }
        }
        
        //update message timers
        for( int i = 0; i < messagebox.length; i++ ){
        	if( messagebox[i] == null ){
        		break;
        	}
        	if( messagebox[i].timer <= 0 ){
        		messagebox[i] = null;
        	}else{
        		messagebox[i].timer -= delta;
        	}
        }
    }


    // pause the game
    public void pause(Input input) {
        if (input.isKeyPressed(Input.KEY_SPACE)) {
            paused = !paused;
        }
    }


    /*
    get the key being pressed, returns a string
     */
    public String getKeystroke(Input input) {
        String ks = "";
        if (input.isKeyDown(Input.KEY_W)) {
            ks = "w";
        }
        else if (input.isKeyDown(Input.KEY_S)) {
            ks = "s";
        }
        else if (input.isKeyDown(Input.KEY_A)) {
            ks = "a";
        }
        else if (input.isKeyDown(Input.KEY_D)) {
            ks = "d";
        }
        // cheat speed up
        else if (input.isKeyPressed(Input.KEY_4)) {
            ks = "4";
        }
        // cheat slow down
        else if (input.isKeyPressed(Input.KEY_5)) {
            ks = "5";
        }
        try{
            dos.writeUTF(ks);
            dos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
        return ks;
    }

    /**
     * Reads the new player coordinates and walks the player accordingly.
//     */
//    public void getNewPlayerCoord(){
//        try {
//            serverMessage = dis.readUTF();
//            if (!serverMessage.equals("")) {
////                System.out.println("server.Server says: Move valid.  New coordinates: "+ serverMessage);
//                knight.moveSmoothTranslationHelper();
//            } else{
////                System.out.println("server.Server says: Move invalid/No Button Pressed.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}