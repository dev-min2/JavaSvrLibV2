package CoreAcitive;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MessageInfo {
	private Calendar packetArriveTime = null;
	// DispatchBot에서 던져주는 sessionData
	private Map<String,String> sessionDataByJsonKey = null;
	
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
	
	public void deleteSession() {
		sessionDataByJsonKey = null;
	}
}