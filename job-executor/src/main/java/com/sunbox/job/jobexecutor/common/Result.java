package com.sunbox.job.jobexecutor.common;

/**
 * @program:
 * @description: json 返回结果集
 * @author: chen_mx
 * @create: 2019-04-01 17:33
 **/
public class Result {
    private String msg;
    private Integer  code;
    private Object  obj;

    public Result(){

    }

    public Result(String msg){
        this.msg=msg;
    }

    public  Result(Integer  code  ,String msg){
        this.msg=msg;
        this.code=code;
    }
    public  Result(Integer  code  ,String msg,Object  obj){
        this.msg=msg;
        this.code=code;
        this.obj=obj;
    }
    public Object  forkValue(){
        return  this.obj;
    }

    public  static Result isOk(){
        return  new Result(ResultEnum.Success.code,ResultEnum.Success.msg);
    }

    public  static Result isOk(Object  obj){
        return  new Result(ResultEnum.Success.code,ResultEnum.Success.msg,obj);
    }
    public  static Result failArgs(String  msg){
        return  new Result(ResultEnum.Success.code,msg);
    }
    public  static Result  initialResult(int  code,String msg){
        return new Result(code,msg);
    }

    public  static Result transArgs(String msg){
        return  new Result(ResultEnum.Success.code,msg);
    }

    public  static Result isError(){
        return  new Result(ResultEnum.fail.code,ResultEnum.fail.msg);
    }
    public  static Result isError(String errMsg){
        return  new Result(ResultEnum.fail.code,errMsg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public  String  toString(){
        return  this.msg+","+this.code+","+this.obj;
    }

}
