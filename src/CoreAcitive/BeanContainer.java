package CoreAcitive;

import java.util.AbstractMap;
import java.util.HashMap;

/*
��ü(Bean)���� �����ϴ� �����̳�.
*/
public class BeanContainer {
	private static BeanContainer instance = null;
	private static Object instLock = new Object();
	
	//private ApplicationBeanLoader beanLoader = null;
	// ���� Bean��ü�� ���� ��. -> �ܺο��� ���� �����ؾ��ϴ°�? ����
	private HashMap<AbstractMap.SimpleEntry<String, Class>,Object> beanObjByIdClass = null;
	
	private BeanContainer() {}
	
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
	
	public void init(HandlerMapping mapper, HandlerAdapter adapter) throws Exception {
		beanObjByIdClass = ApplicationBeanLoader.parseBean();
		mapper.init(beanObjByIdClass,adapter);
	}
	
	public Object getBean(String id, Class className) {
		return beanObjByIdClass.get(new AbstractMap.SimpleEntry<String, Class>(id,className));
	}
}