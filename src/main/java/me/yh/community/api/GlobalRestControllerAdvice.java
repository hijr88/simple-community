package me.yh.community.api;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestControllerAdvice {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String methodArgumentTypeMismatchException() {

        return "Bad Request!";
    }
}
