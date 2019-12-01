package client;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SplashScreen extends BasicGameState {
    ArrayList<Element> characterTypes;
    ArrayList<SubMenu> menus;
    String selectedPlayer;
    String ip = "";

    int menuOption = 0;
    int characterOption = 0;
    boolean selectCharacter = true;
    boolean connect = false;
    boolean enterAddress = false;

    
    @Override
    public int getID() {
        return Main.STARTUPSTATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Main dc = (Main) game;
        characterTypes = new ArrayList<>(4);
        initCharacterTypes();
        menus = new ArrayList<>(3);
        initSubMenus();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        Main dc = (Main) game;
        System.out.println("Welcome to the Splashscreen");

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Main dc = (Main) game;
        renderName(dc, g);
        renderCharacters(dc, g);
        renderIP(dc, g);
        renderLaunch(dc, g);
        if (selectCharacter) {
            g.drawRect(160, 160, 500, 75);
        }
        else if (enterAddress) {
            g.drawRect(160, 235, 500, 75);
        }
        else if (connect) {
            g.drawRect(160, 310, 500, 75);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Main dc = (Main) game;
        Input input = container.getInput();

        selectNextSubMenu(input);

        if (selectCharacter) {
            selectCharacter(input);
        }
        if (enterAddress) {
            enterIP(input);
        }
        if (connect) {

            if (input.isKeyPressed(Input.KEY_ENTER)) {
                connectToSever(dc);
            }
        }
    }


    private void enterIP(Input input) {
        if (input.isKeyPressed(Input.KEY_0)) {
            ip += "0";
        }
        else if (input.isKeyPressed(Input.KEY_1)) {
            ip += "1";
        }
        else if (input.isKeyPressed(Input.KEY_2)) {
            ip += "2";
        }
        else if (input.isKeyPressed(Input.KEY_3)) {
            ip += "3";
        }
        else if (input.isKeyPressed(Input.KEY_4)) {
            ip += "4";
        }
        else if (input.isKeyPressed(Input.KEY_5)) {
            ip += "5";
        }
        else if (input.isKeyPressed(Input.KEY_6)) {
            ip += "6";
        }
        else if (input.isKeyPressed(Input.KEY_7)) {
            ip += "7";
        }
        else if (input.isKeyPressed(Input.KEY_8)) {
            ip += "8";
        }
        else if (input.isKeyPressed(Input.KEY_9)) {
            ip += "9";
        }
        else if (input.isKeyPressed(Input.KEY_PERIOD)) {
            ip += ",";
        }
        else if (input.isKeyPressed(Input.KEY_COMMA)) {
            ip += ",";
        }
        else if (input.isKeyPressed(Input.KEY_DELETE) || input.isKeyPressed(Input.KEY_BACK)) {
            if (ip.length() > 0) {
                ip = ip.substring(0, ip.length() - 1);
            }
        }
    }



    /*
    Allows player to choose their character, currently selected character is in red
     */
    private void selectCharacter(Input input) {
        if (input.isKeyPressed(Input.KEY_LEFT)) {
            if (characterOption <= 0) {
                characterOption = 0;
                return;
            }
            characterOption--;
        }
        else if (input.isKeyPressed(Input.KEY_RIGHT)) {
            if (characterOption >= 3) {
                characterOption = 3;
                return;
            }
            characterOption++;
        }
        else if (input.isKeyPressed(Input.KEY_ENTER)) {
            selectedPlayer = characterTypes.get(characterOption).message;
        }

        Element ch = characterTypes.get(characterOption);
        ch.color = new Color(Color.red);
        for (Element e : characterTypes) {
            if (e.equals(ch)) {
                continue;
            }
            e.color = new Color(Color.white);
        }

    }

    /*
    Selects the sub menu, outlines it in a white box
     */
    private void selectNextSubMenu(Input input) {

        if (input.isKeyPressed(Input.KEY_DOWN)) {
            if (menuOption >= 2) {
                menuOption = 2;
            }
            else {
                menuOption++;
            }
        }
        else if (input.isKeyPressed(Input.KEY_UP)) {
            if (menuOption <= 0) {
                menuOption = 0;
            }
            else {
                menuOption--;
            }
        }

        switch (menuOption) {
            case 0:
                selectCharacter = true;
                enterAddress = false;
                connect = false;
                break;
            case 1:
                selectCharacter = false;
                enterAddress = true;
                connect = false;
                break;
            case 2:
                selectCharacter = false;
                enterAddress = false;
                connect = true;
                break;
            default:
                selectCharacter = true;
                enterAddress = false;
                connect = false;
                break;

        }
    }


    private void renderLaunch(Main dc, Graphics g) {
        Color tmp = g.getColor();
        g.setColor(new Color(255, 255, 255, 1f));
        g.drawString("Press ", 200, 325);
        g.setColor(new Color(0, 255, 0, 1f));
        g.drawString("<enter>", 255, 325);
        g.setColor(new Color(255, 255, 255, 1f));
        g.drawString(" to start the game.", 320, 325);
        g.setColor(tmp);
    }

    /**
     * Shows the names of the characters
     * @param dc
     * @param g
     */
    private void renderIP(Main dc, Graphics g) {
        Color tmp = g.getColor();
        g.setColor(new Color(255, 255, 255, 1f));
        g.drawString("Enter Server IP Address:", 200, 250);
        g.drawString(ip, 200, 275);
        g.setColor(tmp);
    }

    /**
     * Shows the names of the characters
     * @param dc
     * @param g
     */
    private void renderCharacters(Main dc, Graphics g) {
        Color tmp = g.getColor();
        g.setColor(new Color(255, 255, 255, 1f));
        g.drawString("Select Character:", 200, 175);
        for (Element e : characterTypes) {
            g.setColor(e.color);
            g.drawString(e.message, e.x, e.y);
        }
        g.setColor(tmp);
    }

    private void initCharacterTypes() {
        Color color = new Color(255, 255, 255, 1f);
        characterTypes.add(new Element("Knight", color, 200, 200));
        characterTypes.add(new Element("Tank", color, 300, 200));
        characterTypes.add(new Element("Archer", color, 400, 200));
        characterTypes.add(new Element("Mage", color, 500, 200));
    }

    private void initSubMenus() {
        menus.add(new SubMenu(true, "selectCharacters"));
        menus.add(new SubMenu(false, "connectToServer"));
    }
    
    /*
    Renders the title of the game
     */
    private void renderName(Main dc, Graphics g) {
        Color tmp = g.getColor();
        g.setColor(new Color(0, 0, 0, .3f));
        g.setColor(new Color(255, 255, 255, 1f));
        g.drawString("Dungeon Crawl", (float) dc.ScreenWidth/2 - 12, 50);
        g.setColor(tmp);
    }


    /**
     * Connects the client to the server
     * @param dc
     */
    private void connectToSever(Main dc) {
        // Setting up the connection to the server
        Socket socket = null;
        ObjectInputStream dis = null;
        ObjectOutputStream dos = null;
            try {
                byte[] ipAddr = new byte[]{127,0,0,1};

                // getting localhost ip
                InetAddress ip = InetAddress.getByAddress(ipAddr);

                // establish the connection with server port 5000
                socket = new Socket(ip, 5000);

                // obtaining input and out streams
                dis = new ObjectInputStream(socket.getInputStream());
                dos = new ObjectOutputStream(socket.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        dc.socket = socket;
        dc.dis = dis;
        dc.dos = dos;
        dc.enterState(Main.LEVEL1);
        }
        

        // elements in menus
        private class Element {
            String message;
            Color color;
            float x;
            float y;
            
            public Element(String message, Color color, float x, float y) {
                this.message = message;
                this.color = color;
                this.x = x;
                this.y = y;
            }
        }

        // menues
        private class SubMenu {
            boolean selected;
            String name;

            public SubMenu(boolean selected, String name) {
                this.name = name;
                this.selected = selected;
            }
        }
    
}
