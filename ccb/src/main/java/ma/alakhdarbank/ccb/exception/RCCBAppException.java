package ma.alakhdarbank.ccb.exception;

import java.util.logging.Level;

/**
 * @author a.bouabidi
 *
 */
public class RCCBAppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8716473631750857442L;

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * 
	 */
	
	private Level level;
	
	public RCCBAppException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RCCBAppException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RCCBAppException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RCCBAppException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RCCBAppException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
	

}
