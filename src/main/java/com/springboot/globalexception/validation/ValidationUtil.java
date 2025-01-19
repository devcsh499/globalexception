package com.springboot.globalexception.validation;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationUtil {

    public static ValidationType findValidationTypeOfAnnotation(MethodParameter methodParameter) {
        if (methodParameter.getParameterAnnotation(ModelAttribute.class) != null) {
            return ValidationType.modelAttribute;
        } else if (methodParameter.getParameterAnnotation(RequestBody.class) != null) {
            return ValidationType.requestBody;
        } else if (methodParameter.getParameterAnnotation(RequestPart.class) != null) {
            return ValidationType.requestPart;
        } else {
            return ValidationType.other;
        }
    }

    public static Map<String, List<String>> createFieldMapFromFieldErrorList(List<FieldError> fieldErrors) {
        Map<String, List<String>> fieldMap = new HashMap<>(3);
        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            if (fieldMap.get(fieldName) == null) {
                fieldMap.put(fieldName, new ArrayList<>(3));
            }
            fieldMap.get(fieldName)
                    .add(fieldError.getDefaultMessage());
        }
        return fieldMap;
    }

    public static List<String> createMessageListFromParameterValidationResult(List<MessageSourceResolvable> resolvableErrors) {
        return resolvableErrors.stream()
                               .map(MessageSourceResolvable::getDefaultMessage)
                               .toList();
    }

}
