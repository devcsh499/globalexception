package com.springboot.globalexception.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ValidationErrorDetail {
    private ValidationType validationType;
    private String parameterName;
    private List<String> messages;
}