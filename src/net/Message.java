package src.net;

public class Message {
	protected int requestType, value;
	
	public Message(int requestType, int value) {
		this.requestType = requestType;
		this.value = value;
	}

	public Message(int requestType) {
		this(requestType, 0);
	}

	public int getRequestType() {
		return this.requestType;
	}

	public int getValue() {
		return this.value;
	}
}