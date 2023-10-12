package CoreAcitive;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HandlerMapping {
	// ��û�� �´� Controller
	private Map<String,Object> controllerByRequestURL = null;
	
	public HandlerMapping() {
		controllerByRequestURL = new HashMap<String,Object>();
	}
	
	
	public void init(HashMap<AbstractMap.SimpleEntry<String, Class>,Object> beanObjByIdClass, HandlerAdapter adpater) {
		// Controller�� Bean���� ã��, HandlerMapping�� �����ش�.
		List<Object> controllerList = beanObjByIdClass.entrySet().
								 						 stream().
								 						 map(entry -> entry.getValue()).
								 						 filter(obj -> obj.getClass().getAnnotation(Controller.class) != null).
								 						 collect(Collectors.toList());
		
		// ��û �������ݿ� �´� ��Ʈ�ѷ� ��ȯ�� ���� ó��. (MultiKeyMap ���?)		
		for(Object controllerObj : controllerList) {
			Method[] methods = controllerObj.getClass().getDeclaredMethods();
			List<String> requestKeys = new ArrayList<String>(methods.length); // �ּ� �޼ҵ����ŭ Capacity.
			for(Method m : methods) {
				var checkAnnotationMethod = m.getAnnotation(RequestMapping.class);
				if(checkAnnotationMethod != null) {
					requestKeys.add(checkAnnotationMethod.value()); // ��û Value
				}
			}
			
			if(requestKeys.size() <= 0)
				continue;
			
			requestKeys.stream().forEach(str -> controllerByRequestURL.put(str, controllerObj));
		}
		
		adpater.init(controllerByRequestURL);
	}
	
	public Object requestControllerMapping(String protocol) {
		return controllerByRequestURL.get(protocol);
	}
}
