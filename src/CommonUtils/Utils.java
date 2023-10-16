package CommonUtils;

import java.util.Calendar;

public final class Utils {
	private static final int WEEKID_OFFSET_MONDAY = 2;
	
	public static byte[] StringIpToBytes(String ip) {
		if(ip == null || ip.isEmpty())
			return null;
		
		String[] bytes = ip.split("\\.");
		byte[] ret = new byte[bytes.length];
		int cnt = 0;
		for(String s : bytes) {
			ret[cnt++] = (byte)Integer.parseInt(s);
		}
		
		return ret;
	}
	
	public static int getDayID(Calendar time) {
		if(time == null)
			return 0;
		
		int[] info = getTimeInfo(time);
		
		return (info[0] * 10000) + (info[1] * 100) + info[2];
	}

	
	// 해당주의 기준점이 되는 WeekID를 구한다.
	public static int getWeekID(Calendar time, int dayOfWeek) {
		if(time == null)
			return 0;
		
		if(dayOfWeek < Calendar.SUNDAY || dayOfWeek > Calendar.SATURDAY) {
			dayOfWeek = WEEKID_OFFSET_MONDAY;
		}
		
		int diffDayOfWeek = time.get(Calendar.DAY_OF_WEEK) - dayOfWeek;
		if(diffDayOfWeek < 1) {
			diffDayOfWeek += 7;
		}
		
		Calendar retTime = (Calendar)time.clone();
		retTime.add(Calendar.DAY_OF_WEEK, -diffDayOfWeek);
		
		return getDayID(retTime);
	}
	
	public static boolean isDouble(String value) {
		if(value == null || value.isEmpty())
			return false;
		
		try {
			Double.parseDouble(value);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean isInt(String value) {
		if(value == null || value.isEmpty())
			return false;
		
		try {
			Integer.parseInt(value);
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	private static int[] getTimeInfo(Calendar time) {
		return new int[] { time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH)};
	}
}
