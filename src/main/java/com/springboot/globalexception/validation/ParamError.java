package com.springboot.globalexception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParamError {
    private String name;
    private List<String> messages;

    public ParamError(ValidationErrorDetail validationErrorDetail) {
        name = validationErrorDetail.getParameterName();
        messages = validationErrorDetail.getMessages();
    }
}
