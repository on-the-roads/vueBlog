package com.cjw.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResult implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public RpcResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static RpcResult succ(Object data) {
        return new RpcResult(200, "请求成功", data);
    }

    public static RpcResult fail(Object data) {
        return new RpcResult(400, "请求失败", data);
    }

    public static RpcResult fail(int code, String msg, Object data) {
        return new RpcResult(code, msg, data);

    }
}
