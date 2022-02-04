package ss.lr.exceptions;

public class ServerUnavailableException extends Exception {

	private static final long serialVersionUID = 3194494346431589825L;
	private String errorMessage;
	public ServerUnavailableException(String msg) {
		super(msg);
		errorMessage = msg;
	}
	public ServerUnavailableException() {
		super("Can't reach server");
	}

	@Override
	public String getMessage(){
		return this.errorMessage;
	}
}
