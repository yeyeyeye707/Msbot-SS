package com.badeling.msbot.infrastructure.maplegg.repository;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.maplegg.entity.RankResponse;
import com.badeling.msbot.infrastructure.maplegg.exception.HttpAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class RankInfoMsgRepository {

    @Autowired
    private MapleGGApiRepository mapleGGApiRepository;

    @Autowired
    private RankInfoImgRepository rankInfoImgRepository;

    @Autowired
    private GroupMsgService groupMsgService;


    @Async("asyncExecutor")
    public void executeAsync(String characterName, Long groupId) {

        GroupMsg msg = new GroupMsg();
        msg.setGroup_id(groupId);
        msg.setAuto_escape(false);
        StringBuilder sb = new StringBuilder();


        RankResponse response;
        try {
            response = mapleGGApiRepository.requestDataResponse(characterName);
            if (response == null || response.getCharacterData() == null) {
                sb.append("没找到呀");
            } else {
                String image = rankInfoImgRepository.saveImg(response.getCharacterData());
                sb.append("联盟查询:").append(characterName).append("\r\n");
                sb.append("[CQ:image,file=").append(image).append("]\r\n");
            }
        } catch (HttpAccessException e) {
            sb.append(characterName)
                    .append("查询角色不存在");
        }


        msg.setMessage(sb.toString());
        groupMsgService.sendGroupMsg(msg);
    }
}
