package CoreAcitive;

import PacketUtils.Packet;

public class DispatcherBot {
	private static DispatcherBot inst = null;
	private static Object instLock = new Object();
	private DispatcherBot() {}
	
	public static DispatcherBot getDispatcherBot() {
		if(inst == null) {
			synchronized(instLock) {
				if(inst == null) {
					inst = new DispatcherBot();
				}
			}
		}
		return inst;
	}
	
	// Thread-safe한가 한글테스트?
	public void dispatch(Packet packet) {
		// Todo~
	}
}
