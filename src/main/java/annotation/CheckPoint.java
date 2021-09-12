package annotation;

import java.lang.annotation.*;

/**
 * @author wangyangyang
 * @date 2020/11/11 18:51
 * @description
 **/
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CheckPoints.class)
public @interface CheckPoint {
    String value();
}
