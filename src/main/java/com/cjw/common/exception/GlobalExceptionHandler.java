package com.cjw.common.exception;


import com.cjw.common.lang.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public RpcResult handler401(ShiroException e) {
        log.error("Shiro异常：----->", e);
        return RpcResult.fail(401, e.getMessage(), null);
    }

    /*
     * 捕获运行时异常
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public RpcResult handler(RuntimeException e) {
        log.error("运行时异常：----->", e);
        return RpcResult.fail(e.getMessage());
    }

    /*
     * 捕获校验异常
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RpcResult handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常：----->", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return RpcResult.fail(objectError.getDefaultMessage());
    }

    /*
    * 捕获断言异常
    * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public RpcResult handler(IllegalArgumentException e) {
        log.error("Assert异常：----->", e);
        return RpcResult.fail(e.getMessage());
    }
}
