package com.badeling.msbot.infrastructure.repeat.event;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.cqhttp.event.service.MessageImageService;
import com.badeling.msbot.infrastructure.dao.entity.RepeatSentence;
import com.badeling.msbot.infrastructure.dao.repository.RepeatSentenceRepository;
import com.badeling.msbot.infrastructure.dao.repository.RepeatTimeRepository;
import com.badeling.msbot.infrastructure.repeat.entity.RepeatEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RepeatEventListener {
    private final MessageImageService messageImageService;
    private final GroupMsgService groupMsgService;
    private final RepeatTimeRepository repeatTimeRepository;
    private final RepeatSentenceRepository repeatSentenceRepository;

    private Map<Long, RepeatEntity> msgMap = new HashMap<>();

    @Async("asyncExecutor")
    public void onReceiveGroupMsg(GroupMessagePostEntity msg) {
        var gid = msg.getGroupId();

        var last = msgMap.get(gid);

        if (last == null) {
            msgMap.put(gid, new RepeatEntity(msg));
            return;
        }

        boolean same;
        var imageName = messageImageService.getImageName(msg.getRawMessage());
        if (imageName.isPresent()) {
            var lastImage = messageImageService.getImageName(last.getMsg().getRawMessage());
            same = lastImage.isPresent() && Objects.equals(imageName.get(), lastImage.get());
        } else {
            same = Objects.equals(last.getMsg().getRawMessage(), msg.getRawMessage());
        }


        if (same) {
            if (last.onRepeat(msg.getUserId())) {
                //有人复读！
                repeatTimeRepository.addOnce(msg.getUserId(), msg.getGroupId());

                //烧烧复读
                if (last.needRepeat()) {
                    var send = new GroupMsg();
                    send.setGroup_id(msg.getGroupId());
                    send.setAuto_escape(false);
                    send.setMessage(msg.getRawMessage());
                    groupMsgService.sendGroupMsg(send);
                }
            }
        } else {
            var rc = last.getRepeatUserIds().size();
            //被复读
            if (rc > 0) {
                var r = repeatSentenceRepository.getSentence(msg.getGroupId(), last.getFirstUserId());
                if (r == null) {
                    r = new RepeatSentence();
                    r.setGroup_id(msg.getGroupId());
                    r.setUser_id(last.getFirstUserId());
                    r.setCount(rc);
                    r.setMsg(last.getMsg().getRawMessage());

                    repeatSentenceRepository.save(r);
                } else if (rc > r.getCount()) {
                    r.setCount(rc);
                    r.setMsg(last.getMsg().getRawMessage());

                    repeatSentenceRepository.save(r);
                }
            }

            //重新开始
            last.reset(msg);
        }
    }
}
