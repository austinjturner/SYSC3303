package net;

public class StringMessage extends Message {

	public StringMessage(int requestType, String s) {
		super(requestType);	
		this.dumpDataObject(s);

	}
	
	public StringMessage(Message m) {
		super(m.requestType, m.data);
	}
	
	@SuppressWarnings("unchecked")
	public String getDataObject(){
		String s = null;
		try {
			s = (String) super.getDataObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
}
