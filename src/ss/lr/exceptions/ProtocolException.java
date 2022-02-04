package ss.lr.exceptions;

public class ProtocolException extends Exception {

	private static final long serialVersionUID = 5574774762493692470L;

	private String errorMessage;
	public ProtocolException(String msg) {
		super(msg);
		this.errorMessage = msg;
	}

	@Override
	public String getMessage(){
		return this.errorMessage;
	}

}
