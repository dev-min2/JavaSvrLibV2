package CoreAcitive;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MessageInfo {
	// ������ �ð�
	private Calendar packetArriveTime = null;
	// Session����.
	private Map<String,String> sessionDataByJsonKey = new HashMap<String,String>();
	
	public MessageInfo(Map<String,String> sessionDataByJsonKey) {
		packetArriveTime = Calendar.getInstance();
		this.sessionDataByJsonKey = sessionDataByJsonKey;
	}
	
	public String getParameter(String key) {
		return sessionDataByJsonKey.get(key);
	}
	
	public void setParameter(String key, String value) {
		sessionDataByJsonKey.put(key, value);
	}
	
	public String getTime() {
		return packetArriveTime.toString();
	}
	
	public Calendar getNow() {
		return packetArriveTime;
	}
}