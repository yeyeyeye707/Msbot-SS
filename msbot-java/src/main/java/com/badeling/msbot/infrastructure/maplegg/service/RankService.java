package com.badeling.msbot.infrastructure.maplegg.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.maplegg.repository.RankInfoMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankService {
    @Autowired
    private ConstRepository constRepository;

    @Autowired
    private RankInfoMsgRepository rankInfoMsgRepository;


    public String getRank(String raw_message, Long groupId) {
        String characterName = raw_message.replace(constRepository.getBotName(), "")
                .replace("联盟", "")
                .replace(" ", "");

        //慢慢查
        rankInfoMsgRepository.executeAsync(characterName, groupId);

        //立即响应
        return "努力查询中";
    }
}

