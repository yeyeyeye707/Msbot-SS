package com.badeling.msbot.infrastructure.flag.service;

import com.badeling.msbot.infrastructure.cq.mapper.CqMessageMapper;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.entity.FlagListener;
import com.badeling.msbot.infrastructure.dao.repository.FlagListenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlagRaceRemindScheduledComponent {

    private final FlagListenerRepository flagListenerRepository;
    private final GroupMsgService groupMsgService;
    private final CqMessageBuildService cqMessageBuildService;
    private final CqMessageMapper cqMessageMapper;


    //    @Scheduled(cron ="0 0 18-22 * * Sun")
//    @Scheduled(cron ="0 */1 * * * *")
    public void msg() {
        List<FlagListener> listeners = flagListenerRepository.getListenersQQ();

        if (listeners == null || listeners.isEmpty()) {
            return;
        }

        for (FlagListener l : listeners) {
            var cq = cqMessageBuildService.create()
                    .atAll().text(l.getMsg()).changeLine()
                    .changeLine()
                    .text("——来自群管理员:").text(String.valueOf(l.getCreator()));

            groupMsgService.sendGroupMsg(cqMessageMapper.toGroupMsg(cq, l.getQq()));
        }
    }
}
