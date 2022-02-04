package ss.lr.exceptions;

public class ClientUnavailableException extends Exception {

    private static final long serialVersionUID = 3194494346431589825L;
    private String errorMessage;

    public ClientUnavailableException(String msg) {
        super(msg);
        errorMessage = msg;
    }

    public ClientUnavailableException() {
        super("Can't reach server");
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
