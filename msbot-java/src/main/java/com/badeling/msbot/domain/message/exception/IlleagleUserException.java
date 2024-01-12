package com.badeling.msbot.domain.message.exception;

public class IlleagleUserException extends Exception {

    public IlleagleUserException() {
        super("宁是什么东西也配命令老娘？爬爬爬！");
    }

    public IlleagleUserException(String msg) {
        super(msg);
    }
}
