package com.badeling.msbot.infrastructure.cq.entity.impl;

import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;

public class CqMessageEntityCqhttp implements CqMessageEntity {
    private final StringBuilder sb;
    private boolean autoEscape;

    public CqMessageEntityCqhttp() {
        sb = new StringBuilder();
        autoEscape = true;
    }


    @Override
    public CqMessageEntityCqhttp atAll() {
        sb.append("[CQ:at,qq=all]");
        autoEscape = false;
        return this;
    }

    @Override
    public CqMessageEntityCqhttp atQQ(Long qq) {
        if (qq != null && qq > 0) {
            sb.append("[CQ:at,qq=").append(qq).append(']');
            autoEscape = false;
        }
        return this;
    }

    @Override
    public CqMessageEntityCqhttp image(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            //https://docs.go-cqhttp.org/cqcode/#图片
            sb.append("[CQ:image,file=").append(filePath).append(']');
            autoEscape = false;
        }
        return this;
    }


    @Override
    public CqMessageEntityCqhttp imageAbsolute(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            sb.append("[CQ:image,file=file://").append(filePath).append(']');
            autoEscape = false;
        }
        return this;
    }

    @Override
    public CqMessageEntityCqhttp imageUrl(String fileUrl) {
        return this;
    }

    @Override
    public CqMessageEntityCqhttp text(String text) {
        if (text != null && !text.isEmpty()) {
            sb.append(text);
        }

        return this;
    }

    @Override
    public CqMessageEntity changeLine() {
        sb.append("\r\n");
        return this;
    }

    @Override
    public CqMessageEntity notAutoEscape(){
        autoEscape = false;
        return this;
    }

    @Override
    public boolean isAutoEscape() {
        return autoEscape;
    }


    @Override
    public String getMessage() {
        return sb.toString();
    }
}