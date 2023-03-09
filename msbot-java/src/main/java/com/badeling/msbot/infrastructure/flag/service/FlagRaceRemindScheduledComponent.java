package com.badeling.msbot.infrastructure.flag.service;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.entity.FlagListener;
import com.badeling.msbot.infrastructure.dao.repository.FlagListenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlagRaceRemindScheduledComponent {

    @Autowired
    private FlagListenerRepository flagListenerRepository;

    @Autowired
    private GroupMsgService groupMsgService;

    @Scheduled(cron ="0 0 18-22 * * Sun")
//    @Scheduled(cron ="0 */1 * * * *")
    public void msg(){
        List<FlagListener> listeners =flagListenerRepository.getListenersQQ();

        if(listeners == null || listeners.isEmpty()){
            return;
        }

        for(FlagListener l : listeners){
            GroupMsg msg = new GroupMsg();
            msg.setGroup_id(l.getQq());
            msg.setAuto_escape(false);
            StringBuilder sb = new StringBuilder();
            sb.append("[CQ:at,qq=all] ");
            sb.append(l.getMsg());
            sb.append("\r\n\r\n——来自群管理员:");
            sb.append(l.getCreator());
            msg.setMessage(sb.toString());
            groupMsgService.sendGroupMsg(msg);
        }
    }
}
