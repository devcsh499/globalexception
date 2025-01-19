package com.springboot.globalexception.validation;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ToString
@Getter
public class ValidationResultVisitor implements HandlerMethodValidationException.Visitor {

    private final List<ValidationErrorDetail> errors = new ArrayList<>();

    private void addError(ValidationType type, String name, List<String> messages) {
        ValidationErrorDetail validationErrorDetail = new ValidationErrorDetail(type, name, messages);
        errors.add(validationErrorDetail);
    }

    @Override
    public void requestBody(RequestBody requestBody, ParameterErrors errors) {
        if (errors.getFieldErrors().isEmpty())
            return;

        Map<String, List<String>> fieldMap = ValidationUtil.createFieldMapFromFieldErrorList(errors.getFieldErrors());
        fieldMap.forEach((fieldName, messages) -> {
            addError(ValidationType.requestBody, fieldName, messages);
        });
    }

    @Override
    public void modelAttribute(@Nullable ModelAttribute modelAttribute, ParameterErrors errors) {
        if (errors.getFieldErrors().isEmpty())
            return;

        Map<String, List<String>> fieldMap = ValidationUtil.createFieldMapFromFieldErrorList(errors.getFieldErrors());
        fieldMap.forEach((fieldName, messages) -> {
            addError(ValidationType.modelAttribute, fieldName, messages);
        });
    }

    @Override
    public void pathVariable(PathVariable pathVariable, ParameterValidationResult result) {
        if(result.getResolvableErrors().isEmpty())
            return;

        List<String> messages
            = ValidationUtil.createMessageListFromParameterValidationResult(result.getResolvableErrors());
        addError(ValidationType.pathVariable, result.getMethodParameter().getParameterName(), messages);
    }

    @Override
    public void requestParam(@Nullable RequestParam requestParam, ParameterValidationResult result) {
        if(result.getResolvableErrors().isEmpty())
            return;

        List<String> messages
            = ValidationUtil.createMessageListFromParameterValidationResult(result.getResolvableErrors());
        addError(ValidationType.requestParam, result.getMethodParameter().getParameterName(), messages);
    }

    @Override
    public void requestHeader(RequestHeader requestHeader, ParameterValidationResult result) {
        // todo: not implemented
//        addError(HandlerMethodValidationErrorType.RequestHeader, requestHeader.name(), result);
    }

    @Override
    public void cookieValue(CookieValue cookieValue, ParameterValidationResult result) {
        // todo: not implemented
//        addError(HandlerMethodValidationErrorType.CookieValue, cookieValue.name(), result);
    }

    @Override
    public void matrixVariable(MatrixVariable matrixVariable, ParameterValidationResult result) {
        // todo: not implemented
//        addError(HandlerMethodValidationErrorType.MatrixVariable, matrixVariable.name(), result);
    }

    @Override
    public void requestPart(RequestPart requestPart, ParameterErrors errors) {
        // todo: not implemented
//        addError(HandlerMethodValidationErrorType.RequestPart, requestPart.name(), errors);
    }

    @Override
    public void other(ParameterValidationResult result) {
        // todo: not implemented
//        addError(HandlerMethodValidationErrorType.Other, result.getMethodParameter().getParameterName(), result);
    }

}