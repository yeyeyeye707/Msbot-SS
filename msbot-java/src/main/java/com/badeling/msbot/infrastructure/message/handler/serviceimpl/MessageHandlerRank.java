package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.dao.entity.RankInfo;
import com.badeling.msbot.infrastructure.dao.repository.RankInfoRepository;
import com.badeling.msbot.infrastructure.maplegg.service.RankService;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Order(1)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageHandlerRank implements MessageHandler {
    private static final Pattern AT_QQ_P = Pattern.compile("\\[CQ:at,qq=(\\d+)\\]");


    private final RankInfoRepository rankInfoRepository;

    private final RankService rankService;

    private final ConstRepository constRepository;
    private final CqMessageBuildService cqMessageBuildService;

    @Override
    public boolean canHandle(String msg) {
        return msg.startsWith("联盟查询");
    }

    @Override
    public String help() {
        return "├── 联盟查询[角色名/@某人]\r\n" +
                "│   ├── 查询角色努力程度,可以指定角色名\r\n" +
                "│   ├── 未指定时 查询自身绑定的角色\r\n" +
                "│   └── 指定@某人时 查询该成员绑定角色\r\n";
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handle(GroupMessagePostEntity message) {
        String msg = message.getRawMessage()
                .replace(" ", "")
                .substring(4);
        var entity = cqMessageBuildService.create();

        //根据qq号绑定找？
        String uid = null;
        if (msg.isEmpty()) {
            uid = String.valueOf(message.getUserId());
        } else {
            Matcher m = AT_QQ_P.matcher(msg);
            if (m.find()) {
                uid = m.group(1);
            }
        }

        // 特殊渲染
        Integer render = null;

        //找到角色名字
        String name;
        if (uid == null || uid.isEmpty()) {
            name = msg.replace(constRepository.getBotName(), "")
                    .replace("联盟", "")
                    .replace(" ", "");
        } else {
            RankInfo rankInfo = rankInfoRepository.getInfoByUserId(uid);
            if (rankInfo == null) {
                return Tuple2.of(entity.text("请先绑定角色").changeLine().text("例如：查询绑定lFor"), false);
            }
            name = rankInfo.getUser_name();
            render = rankInfo.getRender_set();
        }

        rankService.getRank(name, message.getGroupId(), render);

        return Tuple2.of(entity.text("努力查询中."), false);
    }
}
