package telran.annotation.validation.constraints;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Validator {
/**
 * validates the given object against the constraints in the package telran.annotation.validation.constraints
 * @param obj
 * @return list constraint violation messages or empty list if no violations
 */
	HashSet<Class<?>> clazzSet = new HashSet<>();
	public List<String> validate(Object obj) {
		List<String> violations = new LinkedList<>();
		if(obj==null) { // !!! #1
			violations.add("Validated object can not be null");
			return violations;
		}
		clazzSet.add(obj.getClass());
		Arrays.stream(obj.getClass().getDeclaredFields()).forEach(field -> validate(field, violations, obj));
		return violations;
	}
	
	private void validate(Field field, List<String> violations, Object obj) {
		field.setAccessible(true);   // !!! #2
		Valid annotation = field.getAnnotation(Valid.class);
		if(annotation==null) {
			regValidation(field, violations, obj);
//			regularValidation(field, violations, obj);
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
		// !!! #3
		if(nestedObj != null && !clazzSet.contains(nestedObj.getClass())) {
			clazzSet.add(nestedObj.getClass());
			Arrays.stream(nestedObj.getClass().getDeclaredFields()).forEach(f -> validate(f, violations, nestedObj));
		}
	}
	
	private void regValidation(Field field, List<String> violations, Object obj)  {
		for(Annotation annotation: field.getAnnotations()) {
			String methodName = null;
			try {
				methodName = annotation.annotationType().getSimpleName().toLowerCase();
				Method method = Validator.class.getDeclaredMethod(methodName, Field.class,  Object.class);
				method.setAccessible(true);
				String message = (String)method.invoke(this, field,  obj);
				if (!message.isEmpty()) {
					violations.add(message);
				}				
			}catch (Exception e) {
				System.out.printf("anootation %s is not a validation annotation\n", methodName);
			}
		}		
	}
	
	private void regularValidation(Field field, List<String> violations, Object obj)  {
		String violationMsg = max(field, obj);
		if(!violationMsg.isEmpty()) {
			violations.add(violationMsg);
		}
		violationMsg  = min(field, obj);
		if(!violationMsg.isEmpty()) {
			violations.add(violationMsg);
		}
		violationMsg  = patern(field, obj);
		if(!violationMsg.isEmpty()) {
			violations.add(violationMsg);
		}
	}
	private String max(Field field, Object obj)  {
		Max annotation = field.getAnnotation(Max.class);
		try {
			if(annotation != null) {
				// !!! #4
				double fieldValue = Double.parseDouble(field.get(obj).toString());
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
				// !!! #5
				double fieldValue = Double.parseDouble(field.get(obj).toString());
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
	private String patern(Field field, Object obj) {
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
