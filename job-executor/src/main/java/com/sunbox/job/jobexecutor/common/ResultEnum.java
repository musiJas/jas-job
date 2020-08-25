package com.sunbox.job.jobexecutor.common;

public enum ResultEnum {

    Success(200,"请求成功"),fail(500,"请求忙！请重试.");

    public  Integer  code;
    public  String   msg;
    ResultEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public  String  getMsg(String  key){
       return  this.getMsg(key);
    }
}
