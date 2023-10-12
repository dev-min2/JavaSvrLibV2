package CoreAcitive;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import PacketUtils.Packet;

public class HandlerAdapter {

	public Packet requestProcessing(String protocol, Object controller, Packet requestPacket) throws Exception {
		// 앞에서 유효성 검사했으니 따로 하지않음.
		Method requestMethod = null;
		// 해당 요청에 맞는 메소드를 찾고,
		Method[] methods = controller.getClass().getDeclaredMethods();
		for(Method m : methods) {
			RequestMapping annotation = m.getDeclaredAnnotation(RequestMapping.class);
			if(annotation != null && annotation.value().equals(protocol)) {
				requestMethod = m;
			}
		}
		
		// 요청 메소드에서 담아서 줘야한다.
		Packet ackPacket = null;
		requestMethod.invoke(requestPacket, ackPacket);
		
		return ackPacket;
	}
}
