package com.zhuzhou.framework.validate.annotation;

import com.zhuzhou.framework.validate.validator.NotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证参数不能为空
 * @author chenzeting
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmpty {

	/**
	 * 验证失败错误信息
	 * @return
	 */
	String message() default "{value}参数不能为空值";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
  
}
