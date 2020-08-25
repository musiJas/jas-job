package com.sunbox.job.jobexecutor.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: jflow
 * @description: 参数结构
 * @author: chen_mx
 * @create: 2019-04-09 11:20
 **/
@Slf4j
public class Params {
    private  final Logger logger = LoggerFactory.getLogger(Params.class);
    private  Integer  code;
    private  String  msg;
    private  static  final  Map<String,Object>  hashMap=new ConcurrentHashMap<>();

    public  Params(){

    }
    public  Params(Map<String,Object> map){
        hashMap.putAll(map);
    }

    public    Map<String,Object>  forMap(){
        return   hashMap;
    }

    public  static  void  setValue(String key,Object value){
        if(StringUtils.isBlank(key)){
            return ;
        }
        hashMap.put(key,value);
    }
    public  static  Object  getValue(String key){
        if(StringUtils.isBlank(key)){
            return  null;
        }
        return  hashMap.get(key);
    }
    public  static  <T> T  getValue(String  key,Class<T>  cls){
        if(StringUtils.isBlank(key)){
            return  null;
        }
        return (T)hashMap.get(key);
    }

    public  static  String  writeString(){
        StringBuffer  sb=new StringBuffer();
        for(Map.Entry<String, Object>  ty:hashMap.entrySet()){
            sb.append("&").append(ty.getKey()).append("=").append(ty.getValue());
        }
        return  sb.toString();
    }


    public  static  Map<String,Object>  init(Map<String,Object>  map){
        if(map.size()==0){
            return hashMap;
        }
        hashMap.putAll(map);
        return  hashMap;
    }
    public  static Params init(String  key, Object val){
        Params params=new Params();
        params.setValue(key,val);
        return params;
    }

    public  static  Map<String,Object>   init (Object  obj)  {
       /* Map<String,Object> map=new HashMap<String, Object>();*/
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            try {
                hashMap.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
        }
        return hashMap;
    }



}
