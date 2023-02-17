package com.badeling.msbot.infrastructure.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LevelExp {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int lv;

    private long total_exp;

    private long need_exp;

    public LevelExp(){

    }
    public LevelExp(int lv, long total_exp, long need_exp){
        this.lv = lv;
        this.total_exp = total_exp;
        this.need_exp = need_exp;
    }

    public  long getTotalExp(){
        return total_exp;
    }
    public long getNeedExp(){
        return need_exp;
    }
}
