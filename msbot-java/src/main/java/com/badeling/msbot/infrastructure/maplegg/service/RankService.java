package com.badeling.msbot.infrastructure.maplegg.service;

import com.badeling.msbot.infrastructure.cq.mapper.CqMessageMapper;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
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
    private final CqMessageBuildService cqMessageBuildService;
    private final CqMessageMapper cqMessageMapper;


    @Async("asyncExecutor")
    public void getRank(String characterName, Long groupId, Integer render) {
        var cq = cqMessageBuildService.create();


        RankResponse response;
        try {
            response = mapleGGApiRepository.requestDataResponse(characterName);
            if (response == null || response.getCharacterData() == null) {
                cq.text("没找到呀");
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

                cq.text("联盟查询:").text(characterName).changeLine();
                cq.image(image).changeLine();
            }
        } catch (HttpAccessException e) {
            cq.text(characterName).text("查询角色不存在");
        }

        var msg = cqMessageMapper.toGroupMsg(cq, groupId);
        if (msg != null) {
            groupMsgService.sendGroupMsg(msg);
        }
    }
}

