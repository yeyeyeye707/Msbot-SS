package com.badeling.msbot.infrastructure.dao.entity;


import jakarta.persistence.*;

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
