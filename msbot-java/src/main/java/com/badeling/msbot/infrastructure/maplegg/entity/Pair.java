package com.badeling.msbot.infrastructure.maplegg.entity;

import lombok.Getter;

@Getter
public class Pair <K,V>{

    public Pair(K k , V v){
        this.k = k;
        this.v = v;
    }
    private K k;
    private V v;
}
