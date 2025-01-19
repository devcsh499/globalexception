package com.springboot.globalexception.controller;


import com.springboot.globalexception.validation.*;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String ERROR_PROPERTY = "errors";

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllException(Exception e) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 요청 처리중 문제가 발생했습니다."
        );
    }

    // ResponseEntityExceptionHandler 메서드 오버라이딩

    /*
     * TypeMismatchException
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        TypeMismatchException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        // 오버라디잉 하지 않은 경우 출력 메세지
//        Object[] args = {ex.getPropertyName(), ex.getValue()};
//        String defaultDetail = "Failed to convert '" + args[0] + "' with value: '" + args[1] + "'";
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage()))
                             .build();
    }

    /*
     * MethodArgumentNotValidException
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        if (ex.getBindingResult()
              .getFieldErrorCount() == 0)
            return super.handleMethodArgumentNotValid(ex, headers, status, request);

        Map<String, List<String>> fieldMap
            = ValidationUtil.createFieldMapFromFieldErrorList(ex.getBindingResult()
                                                                .getFieldErrors());

        Map<String, Map<String, List<String>>> validationTypeWrapperMap = new HashMap<>(1);
        ValidationType validationType = ValidationUtil.findValidationTypeOfAnnotation(ex.getParameter());
        validationTypeWrapperMap.put(validationType.name(), fieldMap);

        ProblemDetail problem = ex.getBody();
        problem.setProperties(Map.of(ERROR_PROPERTY, validationTypeWrapperMap));

        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    /*
     * HandlerMethodValidationException
     */
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
        HandlerMethodValidationException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ValidationResultVisitor visitor = new ValidationResultVisitor();
        ex.visitResults(visitor);

        Map<String, List<ParamError>> map = createErrorMapFromValidationErrorDetailList(visitor.getErrors());

        ProblemDetail problem = ex.getBody();
        problem.setProperties(Map.of(ERROR_PROPERTY, map));

        return super.handleHandlerMethodValidationException(ex, headers, status, request);
    }

    private Map<String, List<ParamError>> createErrorMapFromValidationErrorDetailList(
        List<ValidationErrorDetail> validationErrorDetails
    ) {
        Map<String, List<ParamError>> map = new HashMap<>();
        validationErrorDetails.forEach(detail -> {
            addParamError(map, detail.getValidationType(), detail);
        });
        return map;
    }

    private void addParamError(
        Map<String, List<ParamError>> map,
        ValidationType validationType,
        ValidationErrorDetail validationErrorDetail
    ) {
        String key = validationType.name();
        if (map.get(key) == null) {
            map.put(key, new ArrayList<>(3));
        }
        map.get(key)
           .add(new ParamError(validationErrorDetail));
    }

}
