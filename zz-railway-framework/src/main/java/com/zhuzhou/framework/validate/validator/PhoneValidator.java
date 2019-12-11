package com.zhuzhou.framework.validate.validator;

import com.zhuzhou.framework.validate.annotation.Phone;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号验证器
 * @author chenzeting
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

	@Override
	public void initialize(Phone phone) {
		
	}

	@Override
	public boolean isValid(String phone, ConstraintValidatorContext context) {
		if(StringUtils.isBlank(phone)) {
			return true;
		} else {
			String regExp = "((13|18|17)\\d{9})|((145|147|150|151|152|153|155|156|157|158|159)\\d{8})|((1700|1705|1709)\\d{7})|(^([6|9])\\d{7}$)|(^[6]([8|6])\\d{5}$)";
			Pattern p = Pattern.compile(regExp);
			Matcher m = p.matcher(phone); 
			return m.find();
		}
	}
	
}
