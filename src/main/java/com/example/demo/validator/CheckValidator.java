package com.example.demo.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.form.NameForm;

@Component
public class CheckValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO 自動生成されたメソッド・スタブ
		return NameForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO 自動生成されたメソッド・スタブ
		NameForm form = (NameForm)target;
		if(form.getAddress().isBlank() && form.getKeyword().isBlank()) {
			errors.reject("com.example.demo.validator.CheckValidator.message");
		}
	}

}
