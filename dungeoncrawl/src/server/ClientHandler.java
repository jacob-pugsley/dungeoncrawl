package server;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;


// TODO investigate why reading and writing different position values is not working
//  it seems the client is not getting the updated value, but the server is sending it
public class ClientHandler extends Thread {
    private Socket socket;   // Socket of client and server
    private ObjectOutputStream outStream;  // the output stream
    private ObjectInputStream inStream;  // the input stream
    private int id;    /// the thread id (based on port number in socket)
    private boolean writeSuccess;
    public BlockingQueue<Msg> threadQueue;
    //    private float[][] weights;
    public Msg hero;
    private boolean debug = false;

    public ClientHandler(Socket s, ObjectInputStream is, ObjectOutputStream os, BlockingQueue<Msg> queue) {
        socket = s;
        this.inStream = is;
        this.outStream = os;
        id = s.getPort();
        threadQueue = queue;
        writeSuccess = true;
    }

    @Override
    public void run() {
        boolean init = true;
        try {
            // Write the map onto the client for rendering
            outStream.writeObject(Server.map);
            if (debug) System.out.println("send map " + Server.map.getClass().getSimpleName());
            outStream.reset();

            outStream.writeInt(id);
            if (debug) System.out.println("send id " + id);
            outStream.reset();

            sendEnemyList();
            sendItemList();
            sendCharactersToClient();
            while (true) {
                try {
                    readCharactersFromClient();
                    sendCharactersToClient();
                    // TODO handle exit
                } catch (Exception e) {
                    if (debug) System.out.println("Client " + id + " closed unexpectedly.\nClosing connections " +
                            "and terminating thread.");
                    break;
                }
            }
//                try {
//                    // Receive coordinate message from the client about the Hero
////                    Msg message = (Msg) inStream.readObject();
////                    if (debug) System.out.printf("read %s\n", message);
////                    if (init) {
////                        Server.characters.add(message);
////                        init = false;
////                    }
//                    readCharactersFromClient();
//                    sendCharactersToClient();
////                    if (debug) System.out.println("reading " + message.toString());
////                    if (debug) System.out.println("Client Handler "+id+": "+message);
//                    //toServer(message);
//                    //writeSuccess = writeToClient();
////                    if (!writeSuccess || message.type.equals("Exit"))
////                        break;
//
//                    // Update the AI Positions
////                    readAIStatusFromClient();
////                    weights = AI.updatePosition(message);      // takes the hero's x, y coordinates
////                    message.dijkstraWeights = weights;
////                    sendAIStatusToClient();
////                    sendWeightsToClient(message);
//
//                } catch (SocketException | ClassNotFoundException e) {
//                    if (debug) System.out.println("Client " + id + " closed unexpectedly.\nClosing connections " +
//                            "and terminating thread.");
//                    break;
//                }
//            }
//            Server.clientQueues.remove(threadQueue);
            Server.clients.remove(this);
            outStream.close();
            inStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    Send weights from dijkstra's to the client
     */
    private void sendWeightsToClient(Msg msg) {
        toServer(msg);
        writeToClient();
    }


    /**
     * update the client with the position of the ai
     */
    public void sendAIStatusToClient() {
//        if (debug) System.out.println("sendAIStatusToClient()");
        synchronized (Server.enemies) {
            for (Msg ai : Server.enemies) {
                toServer(ai);
                writeToClient();
            }
        }
//         if (debug) System.out.println();
    }


    /*
    read the information about the AI from the server
     */
    private void readAIStatusFromClient() {
        if (debug) System.out.println("readAIStatusFromClient()");
        synchronized (Server.enemies) {
            for (Msg ai : Server.enemies) {
                try {
                    Msg msg = (Msg) inStream.readObject();
                    if (debug) System.out.printf("send " + msg);
                    ai.wx = msg.wx;
                    ai.wy = msg.wy;
                    ai.hp = msg.hp;
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (debug) System.out.println();
    }

    /**
     * This function places the string into the Server Queue.
     *
     * @param m message to send
     */
    private void toServer(Msg m) {
        try {
            Server.serverQueue.put(m);
//            if (debug) System.out.println("Adding to queue: " + m.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the list of enemies tot he client
     */
    private void sendEnemyList() {
        if (debug) System.out.println("sendEnemyList()");
        try {
            synchronized (Server.enemies) {
                outStream.writeObject(Server.enemies);
                if (debug) System.out.printf("send Server.enemies\n");
            }
            outStream.reset();
//            if (debug) System.out.println("Wrote ArrayList Server.enemies");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (debug) System.out.println();
    }


    /**
     * This method takes from what the server gives to the client
     * and writes to the client.
     */
    // TODO why are we putting and taking from different queues?
    private boolean writeToClient() {
        if (debug) System.out.println("writeToClient()");
        try {
            Msg toClient = threadQueue.take();
            outStream.writeObject(toClient);
            if (debug) System.out.printf("send %s\n", toClient);
            outStream.reset();
//            if (debug) System.out.println("Sent to client "+id+": "+toClient);
            if (debug) System.out.println();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void readCharactersFromClient() {
        if (debug) System.out.println("readCharactersFromClient()");
        int count = 0;
        try {
            count = inStream.readInt();
            if (debug) System.out.printf("Read: %s\n", count);
            synchronized (Server.characters) {
                for (int i = 0; i < count; i++) {
                    Msg character = Server.characters.get(i);
//                    if (debug) System.out.printf("character %s before: wx= %s, wy= %s, ks= %s\n", i, character.wx,
//                            character.wy, character.ks);
                    Msg msg = (Msg) inStream.readObject();
                    if (debug) System.out.printf("read: %s\n", msg);
                    character.wx = msg.wx;
                    character.wy = msg.wy;
                    character.ks = msg.ks;
                    character.hp = msg.hp;
//                    if (debug) System.out.printf("character %s after: wx= %s, wy= %s, ks= %s\n\n", i, character.wx,
//                            character.wy, character.ks);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (debug) System.out.println();
    }

    public void sendCharactersToClient() {
        if (debug) System.out.println("sendCharactersToClient()");
        synchronized (Server.characters) {
            int count = Server.characters.size();
            try {
                outStream.writeInt(count);
                outStream.reset();
                if (debug) System.out.printf("Send %s items\n", count);
                for (int i = 0; i < count; i++) {
                    Msg character = Server.characters.get(i);
                    outStream.writeObject(character);
                    outStream.reset();
                    if (debug) System.out.printf("send %s\n", character);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (debug) System.out.println();
    }

    public void sendItemList() {
        if (debug) System.out.println("sendItemList()");
        try {
            int count = Server.worldItems.size();
            outStream.writeInt(count);
            if (debug) System.out.println("send " + count);
            outStream.reset();
            synchronized (Server.worldItems) {
                for (int i = 0; i < count; i++) {
                    ItemMsg item = Server.worldItems.get(i);
                    outStream.writeObject(item);
                    if (debug) System.out.print("send item\n");
                    outStream.reset();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (debug) System.out.println();
    }

    public int getClientId() {
        return id;
    }

}
