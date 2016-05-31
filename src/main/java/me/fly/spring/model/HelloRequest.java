package me.fly.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wuyafei on 16/5/31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HelloRequest {
    private String name;
    private int age;
    private List<HashMap> tieredPrice;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getAge(){
        return age;
    }
    public void setAge(int age){
        this.age = age;
    }
    public List<HashMap> getTieredPrice(){
        return tieredPrice;
    }
    public void setTieredPrice(List<HashMap> tieredPrice){
        this.tieredPrice = tieredPrice;
    }
}
