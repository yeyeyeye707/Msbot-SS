package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.dao.entity.RankInfo;
import com.badeling.msbot.infrastructure.dao.repository.RankInfoRepository;
import com.badeling.msbot.infrastructure.maplegg.service.RankService;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Order(1)
public class MessageHandlerRank implements MessageHandler {
    private static Pattern AT_QQ_P = Pattern.compile("\\[CQ:at,qq=(\\d+)\\]");

    @Autowired
    private RankInfoRepository rankInfoRepository;

    @Autowired
    private RankService rankService;

    @Override
    public boolean canHandle(String msg) {
        return msg.startsWith("联盟查询");
    }

    @Override
    public String help(){
        return  "├── 联盟查询[角色名/@某人]\r\n" +
                "│   ├── 查询角色努力程度,可以指定角色名\r\n" +
                "│   ├── 未指定时 查询自身绑定的角色\r\n" +
                "│   └── 指定@某人时 查询该成员绑定角色\r\n";
    }

    @Override
    public GroupMessageResult handle(GroupMessagePostEntity message) {
        GroupMessageResult result = new GroupMessageResult();
        String msg = message.getRawMessage()
                .replace(" ","")
                .substring(4);

        //根据qq号绑定找？
        String uid = null;
        if(msg.isEmpty()){
            uid = String.valueOf(message.getUserId());
        }else{
            Matcher m = AT_QQ_P.matcher(msg);
            if(m.find()){
                uid = m.group(1);
            }
        }

        //找到角色名字
        String name;
        if(uid == null || uid.isEmpty()){
            name = msg;
        }else{
            RankInfo rankInfo = rankInfoRepository.getInfoByUserId(uid);
            if(rankInfo==null) {
                result.setReply("请先绑定角色\r\n" +
                        "例如：查询绑定lFor");
                return result;
            }
            name = rankInfo.getUser_name();
        }


        String rankInfo = rankService.getRank(name);
        result.setReply(rankInfo);
        return result;
    }
}
