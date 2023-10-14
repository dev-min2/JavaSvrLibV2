package CoreAcitive;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import PacketUtils.Packet;

public class DispatcherBot {
	private static DispatcherBot inst = null;
	private static Object instLock = new Object();
	
	// 특수 빈
	private HandlerAdapter handlerAdapter = new HandlerAdapter();
	private HandlerMapping handlerMapping = new HandlerMapping();
	
	//Session의 정보를 담는 곳
	private Map<Integer, HashMap<String,Object>> jsonSessionDataBySessionID = new HashMap<Integer,HashMap<String,Object>>();
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
	
	// 쓰레드에 안전할까?
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
		
		
		// 해당 해시 테이블 자체의 write와 read는 lock.
		// 해시테이블의 key에 매칭되는 value는 Lock을 하지않는다.
		// 해당 value는 현재 구조로서 Session 본인만이(해당 Session을 처리하는 스레드만이) read&write가 가능. 
		HashMap<String,Object> sessionDataByJsonKey = null;
		synchronized(sessionMapLock) {
			if(!jsonSessionDataBySessionID.containsKey(sessionId)) {
				jsonSessionDataBySessionID.put(sessionId, new HashMap<String,Object>());
			}
			sessionDataByJsonKey = jsonSessionDataBySessionID.get(sessionId);
		}
		
		MessageInfo msgInfo = new MessageInfo(sessionDataByJsonKey);
		ackPacket = handlerAdapter.requestProcessing(protocol,controller, requestPacket, msgInfo);
		
		// 요청 처리에서 세션을 무효화했따면 지워준다.
		if(msgInfo.getSessionData() == null) {
			synchronized(sessionMapLock) {
				if(jsonSessionDataBySessionID.containsKey(sessionId)) {
					jsonSessionDataBySessionID.remove(sessionId);
				}
			}
		}
		
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
