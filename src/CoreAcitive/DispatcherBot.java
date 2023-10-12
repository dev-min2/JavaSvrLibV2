package CoreAcitive;

import PacketUtils.Packet;

public class DispatcherBot {
	private static DispatcherBot inst = null;
	private static Object instLock = new Object();
	
	// Ư�� ��
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
	
	// �����忡 �����ұ�? 
	public Packet dispatch(Packet requestPacket) throws Exception {
		Packet ackPacket = null;
		
		if(requestPacket == null) 
			return ackPacket;
		
		// 1. HandlerMapping
		// Get Packet protocol
		var annotations = requestPacket.getClass().getDeclaredAnnotations();
		if(annotations == null || annotations.length <= 0 || !(annotations[0] instanceof RequestMapping))
			return ackPacket;
		
		String protocol = ((RequestMapping)annotations[0]).value();
		if(protocol == null || protocol.isEmpty())
			return ackPacket;
		
		Object controller = handlerMapping.requestControllerMapping(protocol);
		if(controller == null)
			return ackPacket;
		
		ackPacket = handlerAdapter.requestProcessing(protocol,controller, requestPacket);
		
		return ackPacket;
	}
}
