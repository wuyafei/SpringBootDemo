package me.fly.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wuyafei on 16/5/31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HelloResponse {
    private boolean success;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<HashMap> tieredPrice;
    private String status;
    private int num;

    public boolean isSuccess(){
        return success;
    }
    public void setSuccess(boolean success){
        this.success = success;
    }
    public List<HashMap> getTieredPrice(){
        return tieredPrice;
    }
    public void setTieredPrice(List<HashMap> tieredPrice){
        this.tieredPrice = tieredPrice;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public int getNum(){
        return num;
    }
    public void setNum(int num){
        this.num = num;
    }
}
