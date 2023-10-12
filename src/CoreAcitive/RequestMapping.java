package CoreAcitive;

import java.lang.annotation.*;

/*
 Method[] methos = GreetingService.class.getDeclaredMethods();
        for (Method method : methos) {
            //가져온 메소드에 어노테이션이 적용되어 있다면
            if(method.isAnnotationPresent(GreetingAnnotation.class)) {
                //GreetingAnnotation 객체를 얻는다.
                GreetingAnnotation greetingAnnotation = method.getAnnotation(GreetingAnnotation.class);
                System.out.print(greetingAnnotation.greeting());
                try {
                    //GreetingService의 메소드를 호출한다.
                    method.invoke(new GreetingService());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
 */

// 패킷 및 메소드에 적용.
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
	String value();
}
