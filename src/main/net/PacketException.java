package src.main.net;

/**
 * A PacketException will be thrown when errors are encountered in the package.
 * At some point, they should be mitigated by the Requester/Responder interactions.
 * If there is an unrecoverable network error (like no network exists), it 
 * will need to be a fatal error for the entire system.
 * 
 * @author austinjturner
 *
 */
public class PacketException extends Exception {
	private static final long serialVersionUID = 2326858832173686378L;

	public PacketException(String message) {
		super(message);
	}
}
