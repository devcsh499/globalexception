package com.springboot.globalexception.controller;

import com.springboot.globalexception.dto.ValidationTestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/validation")
@RestController
@Slf4j
public class ValidationController {

    /*
    Request Parameter
     */
    @GetMapping("/request-param/1")
    public ResponseEntity<String> requestParam1(@RequestParam String stringParam) {
        log.debug("{}", stringParam);
        return ResponseEntity.ok("200 ok");
    }

    @GetMapping("/request-param/2")
    public ResponseEntity<String> requestParam2(@RequestParam Integer integerParam) {
        log.debug("{}", integerParam);
        return ResponseEntity.ok("200 ok");
    }

    @GetMapping("/request-param/3")
    public ResponseEntity<String> requestParam3(@RequestParam @NotEmpty @NotBlank String stringParam) {
        log.debug("{}", stringParam);
        return ResponseEntity.ok("200 ok");
    }

    @GetMapping("/request-param/4")
    public ResponseEntity<String> requestParam4(@RequestParam(required = false) @NotNull String stringParam) {
        log.debug("{}", stringParam);
        return ResponseEntity.ok("200 ok");
    }

    /*
    Path Variable
     */
    @GetMapping("/path-variable/{pathVariable}/integer")
    public ResponseEntity<String> pathVariable1(@PathVariable @Max(-10) Integer pathVariable) {
        log.debug("{}", pathVariable);
        return ResponseEntity.ok("200 ok");
    }

    /*
    Request Body
     */
    @PostMapping("/request-body/1")
    public ResponseEntity<String> requestBody1(@RequestBody @Valid ValidationTestDto validationTestDto) {
        log.debug("{}", validationTestDto);
        return ResponseEntity.ok("200 ok");
    }

    /*
    ModelAttribute
     */
    @GetMapping("/model-attribute/1")
    public ResponseEntity<String> modelAttribute1(@ModelAttribute @Valid ValidationTestDto validationTestDto) {
        log.debug("{}", validationTestDto);
        return ResponseEntity.ok("200 ok");
    }

}
