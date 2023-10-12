package CoreAcitive;

import java.lang.annotation.*;

/*
 Method[] methos = GreetingService.class.getDeclaredMethods();
        for (Method method : methos) {
            //������ �޼ҵ忡 ������̼��� ����Ǿ� �ִٸ�
            if(method.isAnnotationPresent(GreetingAnnotation.class)) {
                //GreetingAnnotation ��ü�� ��´�.
                GreetingAnnotation greetingAnnotation = method.getAnnotation(GreetingAnnotation.class);
                System.out.print(greetingAnnotation.greeting());
                try {
                    //GreetingService�� �޼ҵ带 ȣ���Ѵ�.
                    method.invoke(new GreetingService());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
 */

// ��Ŷ �� �޼ҵ忡 ����.
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
	String value();
}
