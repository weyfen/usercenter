package com.yupi.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:该类用于封装controller返回给前端的信息
 **/
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -2243004256529006189L;

    //由于Http协议中的状态码无法精准的暴露错误,因此我们可能会写自己的状态码
    private int code;

    //保存数据
    private T data;

    //返回信息
    private String message;

    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data,String message) {
        this(code,data,"","");
    }

    public BaseResponse(int code,T data){
        this(code,data,"","");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
