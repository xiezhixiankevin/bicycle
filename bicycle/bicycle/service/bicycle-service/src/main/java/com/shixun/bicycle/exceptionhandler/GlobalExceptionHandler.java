package com.shixun.bicycle.exceptionhandler;

import cn.itcast.feign.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * <Description> GlobalExceptionHandler
 *
 * @author 26802
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常:"+e.getMessage());
    }

    //文件大小异常处理MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public R error(MaxUploadSizeExceededException e){
        e.printStackTrace();
        return R.error().message("文件大小超出限制");
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了自定义异常:"+e.getMessage());
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(e.getMsg());
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }


}
