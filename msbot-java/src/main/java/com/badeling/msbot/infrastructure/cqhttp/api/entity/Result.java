package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.Data;

@Data
public class Result<T> {

    /**
     * 数据
     */
    private T data;

    /**
     * 响应码
     */
    private int retCode;

    /**
     * 状态
     */
    private String status;

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", retCode=" + retCode +
                ", status='" + status + '\'' +
                '}';
    }
}