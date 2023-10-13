package CoreAcitive;

import java.util.HashMap;
import java.util.Map;

import PacketUtils.Packet;

public class DispatcherBot {
	private static DispatcherBot inst = null;
	private static Object instLock = new Object();
	
	// Ư�� ��
	private HandlerAdapter handlerAdapter = new HandlerAdapter();
	private HandlerMapping handlerMapping = new HandlerMapping();
	
	//Session�� ������ ��� ��
	private Map<Integer, HashMap<String,String>> jsonSessionDataBySessionID = new HashMap<Integer,HashMap<String,String>>();
	private Object sessionMapLock = new Object();
	
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
	public Packet dispatch(Packet requestPacket, Integer sessionId) throws Exception {
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
		
		
		// �ش� �ؽ� ���̺� ��ü�� write�� read�� Lock.
		// �ؽ����̺��� key�� ��Ī�Ǵ� value�� Lock�� �����ʴ´�.
		// �ش� value�� ���� �����μ� Session ���θ��� read&write�� �� �� ����(�� �ٸ� ������ ����x)
		HashMap<String,String> sessionDataByJsonKey = null;
		synchronized(sessionMapLock) {
			if(!jsonSessionDataBySessionID.containsKey(sessionId)) {
				jsonSessionDataBySessionID.put(sessionId, new HashMap<String,String>());
			}
			sessionDataByJsonKey = jsonSessionDataBySessionID.get(sessionId);
		}
		
		MessageInfo msgInfo = new MessageInfo(sessionDataByJsonKey);
		ackPacket = handlerAdapter.requestProcessing(protocol,controller, requestPacket, msgInfo);
		
		return ackPacket;
	}
	
	public void closeSession(Integer sessionId) {
		if(jsonSessionDataBySessionID.containsKey(sessionId)) {
			synchronized(sessionMapLock) {
				jsonSessionDataBySessionID.remove(sessionId);
			}
		}
	}
}
