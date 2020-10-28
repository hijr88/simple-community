package me.yh.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(annotations = Controller.class)
public class GlobalControllerAdvice {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String methodArgumentTypeMismatchException(RedirectAttributes ra) {
        ra.addFlashAttribute("type","BAD_REQUEST");
        return "redirect:/error-redirect";
    }
}
