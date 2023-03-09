package com.badeling.msbot.infrastructure.util;

public class NumberSuffixesUtil {
    private static final char[] EXP_SUFFIXES = new char[]{'k', 'm', 'b', 't'};

    public static String getExpStr(Long exp) {
        String expStr = String.valueOf(exp);
        if (expStr.length() < 4) {
            return expStr;
        }

        int i = Math.min(((expStr.length() - 1) / 3), EXP_SUFFIXES.length);
        StringBuilder sb = new StringBuilder();
        int end = expStr.length() - i * 3;
        sb.append(expStr.substring(0, end))
                .append('.')
                .append(expStr.substring(end, end + 2))
                .append(EXP_SUFFIXES[i - 1]);
        return sb.toString();
    }
}
