package CoreAcitive;

import java.util.AbstractMap;
import java.util.HashMap;

import SockNet.NetServer;

/*
객체들을 관리하는 컨테이너.
*/
public class BeanContainer {
	private static BeanContainer instance = null;
	private static Object instLock = new Object();
	
	private ApplicationBeanLoader beanLoader = null;
	// 실제 Bean객체가 담기는 곳. -> 외부에서 접근 가능해야하는가? ㅇㅇ
	private HashMap<AbstractMap.SimpleEntry<String, Class>,Object> beanObjByIdClass = null;
	
	private BeanContainer() {
		beanLoader = new ApplicationBeanLoader();
		beanObjByIdClass = new HashMap<AbstractMap.SimpleEntry<String, Class>,Object>();
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
	
	public Object getBean(String id, Class className) {
		return beanObjByIdClass.get(new AbstractMap.SimpleEntry<String, Class>(id,className));
	}
	
}
