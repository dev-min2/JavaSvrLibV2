package CoreAcitive;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import PacketUtils.Packet;

public class HandlerAdapter {

	public Packet requestProcessing(String protocol, Object controller, Packet requestPacket) throws Exception {
		// �տ��� ��ȿ�� �˻������� ���� ��������.
		Method requestMethod = null;
		// �ش� ��û�� �´� �޼ҵ带 ã��,
		Method[] methods = controller.getClass().getDeclaredMethods();
		for(Method m : methods) {
			RequestMapping annotation = m.getDeclaredAnnotation(RequestMapping.class);
			if(annotation != null && annotation.value().equals(protocol)) {
				requestMethod = m;
			}
		}
		
		// ��û �޼ҵ忡�� ��Ƽ� ����Ѵ�.
		Packet ackPacket = null;
		requestMethod.invoke(requestPacket, ackPacket);
		
		return ackPacket;
	}
}
