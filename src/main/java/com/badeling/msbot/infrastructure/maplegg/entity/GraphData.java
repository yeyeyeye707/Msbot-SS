package com.badeling.msbot.infrastructure.maplegg.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Data;

@Getter
public class GraphData implements Comparable<GraphData>{

    @JsonProperty(value = "DateLabel")
    private String date;
    @JsonProperty(value = "EXPDifference")
    private Long exp;
    @JsonProperty(value = "TotalOverallEXP")
    private Long totalExp;

    public void setDate(String date){
        if(date == null || date.length() != 10){
            this.date = null;
        }else{
            this.date = date.substring(5);
        }
    }

    public boolean check() {
        return date != null && exp != null && exp > 0;
    }

    @Override
    public int compareTo(GraphData o) {
        return this.date.compareTo(o.date);
    }
}
