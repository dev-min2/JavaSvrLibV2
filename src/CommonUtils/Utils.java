package CommonUtils;

public final class Utils {
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
}
