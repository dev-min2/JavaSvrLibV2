package CoreAcitive;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MessageInfo {
	// 도착한 시간
	private Calendar packetArriveTime = null;
	// Session정보.
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
	
	public int getDayID() {
		int[] info = getTimeInfo();
		
		int year = info[0];
		int month = info[1];
		int day = info[2];
		
		return (year * 10000) + (month * 100) + day;
	}
	
	// 해당주의 기준점이 되는 WeekID를 구한다.
	
	// 월요일이 기준.
	public int getWeekID() {
		int[] info = getTimeInfo();
		int year = info[0];
		int month = info[1];
		int day = info[2];
		
		int weekDay = packetArriveTime.get(Calendar.DAY_OF_WEEK);
		return 0;
	}
	
	// 설정한 값이 기준
	public int getWeekID(int weekDay) {
		return 0;
	}
	
	private int[] getTimeInfo() {
		return new int[] { packetArriveTime.get(Calendar.YEAR), packetArriveTime.get(Calendar.MONTH) + 1, packetArriveTime.get(Calendar.DAY_OF_MONTH)};
	}
}