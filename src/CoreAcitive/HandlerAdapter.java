package CoreAcitive;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import PacketUtils.Packet;

public class HandlerAdapter {
	
	private Map<String,Method> requestMethodByProtocol = null;
	
	public HandlerAdapter() {
		requestMethodByProtocol = new HashMap<String,Method>();
	}
	
	public void init(Map<String,Object> controllerByRequestURL) {
		for(Entry<String,Object> entry : controllerByRequestURL.entrySet()) {
			Object controller = entry.getValue();
			Method[] methods = controller.getClass().getDeclaredMethods();
			for(Method m : methods) {
				String requestURLkey = m.getDeclaredAnnotation(RequestMapping.class).value();
				
				requestMethodByProtocol.put(requestURLkey, m);
			}
		}
	}

	public Packet requestProcessing(String protocol, Object controller, Packet requestPacket, MessageInfo msgInfo) throws Exception {
		// 앞에서 유효성 검사했으니 따로 매개변수 유효검사 하지않음.
		Method method = requestMethodByProtocol.get(protocol);
		if(method == null)
			return null;
		
		// 요청 메소드에서 담아서 줘야한다.
		// 어느 오브젝트의? 메소드인지를 알아야하는듯.
		// 메소드 호출은 되는데 내부 패킷 캐스팅에서 오류남. 확인필요.
		Packet ackPacket = null;
		try {
			ackPacket = (Packet)method.invoke(controller, requestPacket, msgInfo);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return ackPacket;
	}
}
