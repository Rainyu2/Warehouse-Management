package com.project.house.common;

import lombok.Data;

public class Result {
    private Integer code; //编码 200/400
    private String msg; //成功或失败
    private Long total; //总记录数
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private static Result result(int code, String msg, Long total, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setTotal(total);
        r.setData(data);
        return r;
    }
    public static Result fail() {
        return result(400, "失败", 0L, null);
    }

    public static Result success() {
        return result(200, "成功", 0L,null );
    }

    public static Result success(Object data) {
        return result(200, "成功", 0L, data);
    }

    public static Result success(Object data,Long total) {
        return result(200, "成功", total, data);
    }
}
