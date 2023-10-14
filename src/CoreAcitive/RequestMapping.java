package CoreAcitive;

import java.lang.annotation.*;

//패킷 및 메소드에 적용..
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
	String value();
}
