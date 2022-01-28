package ss.lr.Protocols;

public class ProtocolMessages {
    /**
     * Delimiter used to separate arguments sent over the network.
     */
    public static final String DELIMITER = ";";
    /**
     * Sent as last line in a multi-line response to indicate the end of the text.
     */
    public static final String EOT = "!";

    //Client protocols

    public static final String MOVE = "MOVE";
    public static final String SWAP = "SWAP";
    public static final String QUIT = "QUIT";
    public static final String JOIN = "JOIN";
    public static final String READY = "READY";

    //Server protocols

    public static final String WELCOME = "WELCOME";
    public static final String ERROR = "ERROR";
    public static final String GAMESTART = "GAMESTART";
    public static final String TILES = "TILES";
    public static final String CURRENT = "CURRENT";
    public static final String UPDATE = "UPDATE";
    public static final String GAMEOVER = "GAMEOVER";





}
