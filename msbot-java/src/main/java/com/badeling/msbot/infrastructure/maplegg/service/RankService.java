package com.badeling.msbot.infrastructure.maplegg.service;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.maplegg.entity.RankResponse;
import com.badeling.msbot.infrastructure.maplegg.exception.HttpAccessException;
import com.badeling.msbot.infrastructure.maplegg.repository.MapleGGApiRepository;
import com.badeling.msbot.infrastructure.maplegg.repository.RankInfoImgRepository;
import com.badeling.msbot.infrastructure.maplegg.repository.impl.RankInfoImgRepositoryDefault;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankService {


    private final MapleGGApiRepository mapleGGApiRepository;

    private final RankInfoImgRepository[] rankInfoImgRepositories;
    private final RankInfoImgRepositoryDefault rankInfoImgRepositoryDefault;

    private final GroupMsgService groupMsgService;


    @Async("asyncExecutor")
    public void getRank(String characterName, Long groupId, Integer render) {
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

                String image = null;
                boolean match = false;
                if (render != null) {
                    for (var rankInfoImgRepository : rankInfoImgRepositories) {
                        if (Objects.equals(render, rankInfoImgRepository.getId())) {
                            image = rankInfoImgRepository.saveImg(response.getCharacterData());
                            match = true;
                            break;
                        }
                    }
                }
                if (!match) {
                    image = rankInfoImgRepositoryDefault.saveImg(response.getCharacterData());
                }

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

