package org.fu.blockchain_backend.exception;

import org.fu.blockchain_backend.common.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice

public class GlobalExceptionHandler {
    // 处理 @Valid 校验失败异常（如 POST JSON 对象）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        return Result.error("参数校验失败：" + msg);
    }

    // 处理 URL 参数校验失败（@RequestParam 或 @PathVariable）
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(cv -> cv.getMessage())
                .findFirst()
                .orElse("参数校验失败");
        return Result.error("参数校验失败：" + msg);
    }

    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace(); // 开发阶段建议打印
        return Result.error("系统异常：" + e.getMessage());
    }
}
