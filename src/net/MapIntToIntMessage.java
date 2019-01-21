package net;
import java.util.*;

public class MapIntToIntMessage extends Message {

	public MapIntToIntMessage(int requestType, Map<Integer, Integer> portMap) {
		super(requestType);	
		this.dumpDataObject(portMap);

	}
	
	public MapIntToIntMessage(Message m) {
		super(m.requestType, m.data);
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> getDataObject(){
		Map<Integer, Integer> portMap = null;
		try {
			portMap = (Map<Integer, Integer>) super.getDataObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return portMap;
	}
}
