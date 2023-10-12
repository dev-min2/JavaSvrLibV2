package CoreAcitive;

import PacketUtils.Packet;

public class DispatcherBot {
	private static DispatcherBot inst = null;
	private static Object instLock = new Object();
	
	// 특수 빈
	private HandlerAdapter handlerAdapter = new HandlerAdapter();
	private HandlerMapping handlerMapping = new HandlerMapping();
	
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
	
	public void init() throws Exception {
		BeanContainer.getBeanContainer().init(handlerMapping, handlerAdapter);
	}
	
	// 쓰레드에 안전할까? 
	public void dispatch(Packet packet) throws Exception {
		// Todo~
		if(packet == null) 
			return;
		
		// 1. HandlerMapping
		// Get Packet protocol
		var annotations = packet.getClass().getDeclaredAnnotations();
		if(annotations == null || annotations.length <= 0 || !(annotations[0] instanceof RequestMapping))
			return;
		
		String protocol = ((RequestMapping)annotations[0]).value();
		if(protocol == null || protocol.isEmpty())
			return;
		
		Object controller = handlerMapping.requestControllerMapping(protocol);
		if(controller == null)
			return;
		
		Packet ackPacket = handlerAdapter.requestProcessing(protocol,controller, packet);
	}
}
