package CoreAcitive;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class HandlerMapping {
	// 요청에 맞는 Controller
	
	
	public void init(HashMap<AbstractMap.SimpleEntry<String, Class>,Object> beanObjByIdClass) {
		// Controller인 Bean들을 찾고, HandlerMapping에 엮어준다.
		List<Object> controllerList = beanObjByIdClass.entrySet().
								 						 stream().
								 						 map(entry -> entry.getValue()).
								 						 filter(obj -> obj.getClass().getAnnotation(Controller.class) != null).
								 						 collect(Collectors.toList());
		
		// 메소드(REQ)는 여러개. Controller는 한개.
		
		// 요청 프로토콜에 맞는 컨트롤러 반환을 위한 처리. (MultiKeyMap 사용?)		
		for(Object controllerObj : controllerList) {
			Method[] methods = controllerObj.getClass().getDeclaredMethods();
			List<String> requestKeys = new ArrayList<String>(methods.length); // 최소 메소드수만큼 Capacity.
			for(Method m : methods) {
				var checkAnnotationMethod = m.getAnnotation(RequestMapping.class);
				if(checkAnnotationMethod != null) {
					requestKeys.add(checkAnnotationMethod.value()); // 요청 Value
				}
			}
		}
	}
	
	public Object requestControllerMapping(String protocol) {
		return new Object();
	}
}
