package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.dao.entity.RankInfo;
import com.badeling.msbot.infrastructure.dao.repository.RankInfoRepository;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(0)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageHandlerBinding implements MessageHandler {

    private final RankInfoRepository rankInfoRepository;
    private final CqMessageBuildService cqMessageBuildService;


    @Override
    public boolean canHandle(String msg) {
        return msg.startsWith("查询绑定");
    }

    @Override
    public String help() {
        return "├── 查询绑定 {角色名}[#1|#2]\r\n" +
                "│   └── 绑定联盟查询命令对应角色名字\r\n";
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handle(GroupMessagePostEntity message) {
        String characterName = message.getRawMessage()
                .substring(4)
                .replace(" ", "");
        var entity = cqMessageBuildService.create();
        if (characterName.isEmpty()) {
            entity.text("笨蛋，你得告诉我绑定的id啊");
        } else {
            String uid = String.valueOf(message.getUserId());

            //删除已有
            RankInfo rankInfo = rankInfoRepository.getInfoByUserId(uid);
            if (rankInfo != null) {
                rankInfoRepository.delete(rankInfo);
            }

            //指定渲染
            var sharpIdx = characterName.indexOf("#");
            Integer render = null;
            if (sharpIdx > 0) {
                try {
                    render = Integer.parseInt(characterName.substring(sharpIdx + 1));
                    characterName = characterName.substring(0, sharpIdx);
                } catch (Exception ex) {
                    return Tuple2.of(entity.text("#命令错误,"), false);
                }
            }

            //保存
            RankInfo ri = new RankInfo();
            ri.setUser_id(uid);
            ri.setUser_name(characterName);
            ri.setRender_set(render);
            rankInfoRepository.save(ri);
            entity.text("绑定成功");
        }

        return Tuple2.of(entity, false);
    }
}
