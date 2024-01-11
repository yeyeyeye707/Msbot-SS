package com.badeling.msbot.infrastructure.cq.entity.impl;

import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;

public class CqMessageEntityShamrock implements CqMessageEntity {
    private final StringBuilder sb;
    private boolean autoEscape;

    public CqMessageEntityShamrock() {
        sb = new StringBuilder();
        autoEscape = true;
    }


    @Override
    public CqMessageEntityShamrock atAll() {
        sb.append("[CQ:at,qq=0]");
        autoEscape = false;
        return this;
    }

    @Override
    public CqMessageEntityShamrock atQQ(Long qq) {
        if (qq != null && qq > 0) {
            sb.append("[CQ:at,qq=").append(qq).append(']');
            autoEscape = false;
        }
        return this;
    }


    @Override
    public CqMessageEntityShamrock image(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            //https://yuyue-amatsuki.github.io/OpenShamrock/message/media.html
            sb.append("[CQ:image,file=").append(filePath).append(']');
            autoEscape = false;
        }
        return this;
    }


    @Override
    public CqMessageEntityShamrock imageAbsolute(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            //https://yuyue-amatsuki.github.io/OpenShamrock/message/media.html
            sb.append("[CQ:image,file=file://").append(filePath).append(']');
            autoEscape = false;
        }
        return this;
    }

    @Override
    public CqMessageEntityShamrock imageUrl(String fileUrl) {
        if (fileUrl != null && fileUrl.startsWith("http")) {
            sb.append("[CQ:image,file=").append(fileUrl).append(']');
            autoEscape = false;
        }
        return this;
    }

    @Override
    public CqMessageEntityShamrock text(String text) {
        if (text != null && !text.isEmpty()) {
            sb.append(text);
        }

        return this;
    }

    @Override
    public CqMessageEntityShamrock changeLine() {
        sb.append("\r\n");
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
