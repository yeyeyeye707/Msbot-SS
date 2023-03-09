package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.dao.entity.RankInfo;
import com.badeling.msbot.infrastructure.dao.repository.RankInfoRepository;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(0)
public class MessageHandlerBinding implements MessageHandler {
    @Autowired
    private RankInfoRepository rankInfoRepository;
    @Override
    public boolean canHandle(String msg) {
        return msg.startsWith("查询绑定");
    }

    @Override
    public String help(){
        return  "├── 查询绑定 {角色名}\r\n" +
                "│   └── 绑定联盟查询命令对应角色名字\r\n";
    }

    @Override
    public GroupMessageResult handle(GroupMessagePostEntity message) {
        GroupMessageResult replyMsg = new GroupMessageResult();
        replyMsg.setAuto_escape(true);
        String characterName = message.getRawMessage()
                .substring(4)
                .replace(" ", "");
        if(characterName.equals("")) {
            replyMsg.setReply("笨蛋，你得告诉我绑定的id啊");
        }else {
            String uid = String.valueOf(message.getUserId());
            RankInfo rankInfo = rankInfoRepository.getInfoByUserId(uid);
            if(rankInfo!=null) {
                rankInfoRepository.delete(rankInfo);
            }
            RankInfo ri = new RankInfo();
            ri.setUser_id(uid);
            ri.setUser_name(characterName);
            rankInfoRepository.save(ri);
            replyMsg.setReply("绑定成功");
        }
        return replyMsg;
    }
}
