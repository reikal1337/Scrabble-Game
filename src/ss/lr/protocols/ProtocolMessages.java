package ss.lr.protocols;

public class ProtocolMessages {

    /***
     *All messages used to communicate between server and client!
     *@author Lukas Reika s2596237.
     */

    public static final String DELIMITER = ";";
    public static final String EOT = "!";

    //Client protocols

    public static final String MOVE = "MOVE";
    public static final String SWAP = "SWAP";
    public static final String QUIT = "QUIT";
    public static final String JOIN = "JOIN";
    public static final String READY = "READY";
    public static final String CHAT = "CHAT";

    //Server protocols

    public static final String WELCOME = "WELCOME";
    public static final String ERROR = "ERROR";
    public static final String GAMESTART = "GAMESTART";
    public static final String TILES = "TILES";
    public static final String CURRENT = "CURRENT";
    public static final String UPDATE = "UPDATE";
    public static final String GAMEOVER = "GAMEOVER";


}
