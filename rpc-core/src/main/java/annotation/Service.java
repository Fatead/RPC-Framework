package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 该注解作用在类或者接口上
@Retention(RetentionPolicy.RUNTIME) //该注解在程序运行时发挥作用
public @interface Service {

    public String name() default "";

}