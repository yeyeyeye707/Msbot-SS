package com.badeling.msbot.domain.notice.decrease.service;

import com.badeling.msbot.application.entity.NoticeResult;
import com.badeling.msbot.domain.notice.decrease.entity.GroupDecreaseEntity;
import org.springframework.stereotype.Service;

@Service
public class GroupDecreaseService {

    public NoticeResult handleResult(GroupDecreaseEntity entity) {
//        if(!noticeMsg.getSub_type().equals("leave")) {
//            return null;
//        }
//        GroupMsg groupMsg = new GroupMsg();
//        groupMsg.setAuto_escape(false);
//        groupMsg.setGroup_id(Long.parseLong(groupId));
//        groupMsg.setMessage("[CQ:image,file=img/leave.png]");
////        System.out.println(noticeMsg.toString());
//        groupMsgService.sendGroupMsg(groupMsg);
        NoticeResult result = new NoticeResult();
        result.setReply("[CQ:image,file=img/leave.png]");
        result.setAt_sender(false);
        result.setAuto_escape(true);
        return result;
    }
}
