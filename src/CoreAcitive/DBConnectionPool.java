package CoreAcitive;

public class DBConnectionPool {
	private DBConnectionPool() {}
	
	private static DBConnectionPool inst = null;
	private static Object instLock = new Object();
	
	private static DBConnectionPool getConnectionPool() {
		if(inst == null) {
			synchronized(instLock) {
				if(inst == null) {
					inst = new DBConnectionPool();
				}
			}
		}
		
		return inst;
	}
	
	public static void init() {
		DBConnectionPool pool = getConnectionPool();
		
		
	}
}
