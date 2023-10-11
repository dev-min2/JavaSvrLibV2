package CoreAcitive;

import java.util.AbstractMap;
import java.util.HashMap;

import SockNet.NetServer;

/*
��ü(Bean)���� �����ϴ� �����̳�.
*/
public class BeanContainer {
	private static BeanContainer instance = null;
	private static Object instLock = new Object();
	
	//private ApplicationBeanLoader beanLoader = null;
	// ���� Bean��ü�� ���� ��. -> �ܺο��� ���� �����ؾ��ϴ°�? ����
	private HashMap<AbstractMap.SimpleEntry<String, Class>,Object> beanObjByIdClass = null;
	
	private BeanContainer() {
		
	}
	
	public static BeanContainer getBeanContainer() {
		if(instance == null) {
			synchronized(instLock) {
				if(instance == null) {
					instance = new BeanContainer();
				}
			}
		}
		return instance;
	}
	
	public void init() throws Exception {
		beanObjByIdClass = new ApplicationBeanLoader().parseBean();
	}
	
	public Object getBean(String id, Class className) {
		return beanObjByIdClass.get(new AbstractMap.SimpleEntry<String, Class>(id,className));
	}
	
	
}
