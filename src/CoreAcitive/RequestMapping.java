package CoreAcitive;

import java.lang.annotation.*;

//��Ŷ �� �޼ҵ忡 ����..
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
	String value();
}
