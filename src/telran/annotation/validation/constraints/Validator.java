package telran.annotation.validation.constraints;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.*;

import telran.annotation.example.DataSql;

public class Validator {
/**
 * validates the given object against the constraints in the package telran.annotation.validation.constraints
 * @param obj
 * @return list constraint violation messages or empty list if no violations
 */
	public List<String> validate(Object obj) {
		List<String> violations = new LinkedList<>();
		Arrays.stream(obj.getClass().getDeclaredFields()).forEach(field -> validate(field, violations, obj));
		return violations;
	}
	
	private void validate(Field field, List<String> violations, Object obj) {
		Valid annotation = field.getAnnotation(Valid.class);
		if(annotation==null) {
			regularValidation(field, violations, obj);
			return;
		}
		// nested validation
		Object nestedObj;
		try {
			nestedObj = field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		Arrays.stream(nestedObj.getClass().getDeclaredFields()).forEach(f -> validate(f, violations, nestedObj));
	}
	
	private void regularValidation(Field field, List<String> violations,
			Object obj)  {
		String violationMsg = max(field, obj);
		if(!violationMsg.isEmpty()) {
			violations.add(violationMsg);
		}
		violationMsg  = min(field, obj);
		if(!violationMsg.isEmpty()) {
			violations.add(violationMsg);
		}
		violationMsg  = pattern(field, obj);
		if(!violationMsg.isEmpty()) {
			violations.add(violationMsg);
		}
	}
	private String max(Field field, Object obj)  {
		Max annotation = field.getAnnotation(Max.class);
		try {
			if(annotation != null) {
				double fieldValue = field.getDouble(obj);
				double annotationValue = annotation.value();
				if(fieldValue > annotationValue) {
					return String.format("Field: %s.  %s : %,.0f is more than %,.0f",
							field.getName(), annotation.message(), fieldValue, annotationValue);
				}
			}
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return "";
	}
	private String min(Field field, Object obj) {
		Min annotation = field.getAnnotation(Min.class);
		try {
			if(annotation != null) {
				double fieldValue = field.getDouble(obj);
				double annotationValue = annotation.value();
				if(fieldValue < annotationValue) {
					return String.format("Field: %s.  %s : %,.0f is less than %,.0f",
							field.getName(), annotation.message(), fieldValue, annotationValue);
				}
			}
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return "";
	}
	private String pattern(Field field, Object obj) {
		Patern annotation = field.getAnnotation(Patern.class);
		try {
			if(annotation != null) {
				String name = (String)field.get(obj);
				String value = annotation.value();
				if(!name.matches(value)) {
					return String.format("Field: %s.  %s : %s",
						field.getName(), annotation.message(), name);
				}
			}
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return "";
	}
}
