package CoreAcitive;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MessageInfo {
	private Calendar packetArriveTime = null;
	// DispatchBot에서 던져누는 sessionData
	private Map<String,Object> sessionDataByJsonKey = new HashMap<String,Object>();
	
	public MessageInfo(Map<String,Object> sessionDataByJsonKey) {
		packetArriveTime = Calendar.getInstance();
		this.sessionDataByJsonKey = sessionDataByJsonKey;
	}
	
	public Object getParameter(String key) {
		return sessionDataByJsonKey.get(key);
	}
	
	public void setParameter(String key, Object value) {
		sessionDataByJsonKey.put(key, value);
	}
	
	public String getTime() {
		return packetArriveTime.toString();
	}
	
	public Calendar getNow() {
		return packetArriveTime;
	}
	
	public void deleteSession() {
		sessionDataByJsonKey = null;
	}
	
	public Map<String,Object> getSessionData() {
		return sessionDataByJsonKey;
	}
}