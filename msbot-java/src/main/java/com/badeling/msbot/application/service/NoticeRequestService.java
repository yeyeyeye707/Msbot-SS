package com.badeling.msbot.application.service;

import com.badeling.msbot.application.entity.NoticeResult;
import com.badeling.msbot.domain.notice.decrease.entity.GroupDecreaseEntity;
import com.badeling.msbot.domain.notice.decrease.service.GroupDecreaseService;
import com.badeling.msbot.domain.notice.increase.entity.GroupIncreaseEntity;
import com.badeling.msbot.domain.notice.increase.service.GroupIncreaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NoticeRequestService {
    @Autowired
    private GroupIncreaseService groupIncreaseService;

    @Autowired
    private GroupDecreaseService groupDecreaseService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public NoticeResult handler(Map<String, Object> request){
        String noticeType =(String) request.get("notice_type");
        NoticeResult result = null;
        if("group_increase".equals(noticeType)){
            GroupIncreaseEntity entity = objectMapper.convertValue(request, GroupIncreaseEntity.class);
            result = groupIncreaseService.handleResult(entity);
        }else if("group_decrease".equals(noticeType)){
            GroupDecreaseEntity entity = objectMapper.convertValue(request, GroupDecreaseEntity.class);
            result = groupDecreaseService.handleResult(entity);
        }

        if(result != null){
            //TODO..主动发送群消息
        }

        return result;
    }
}
