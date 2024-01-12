package com.badeling.msbot.domain.notice.decrease.service;

import com.badeling.msbot.application.entity.NoticeResult;
import com.badeling.msbot.domain.notice.decrease.entity.GroupDecreaseEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupDecreaseService {
    private final CqMessageBuildService cqMessageBuildService;

    public NoticeResult handleResult(GroupDecreaseEntity entity) {
        var e = cqMessageBuildService.create();
        NoticeResult result = new NoticeResult();
        result.setReply(e.image("img/leave.png").getMessage());
        result.setAt_sender(false);
        result.setAuto_escape(true);
        return result;
    }
}
