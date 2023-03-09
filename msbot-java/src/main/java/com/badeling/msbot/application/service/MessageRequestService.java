package com.badeling.msbot.application.service;

import com.badeling.msbot.application.entity.MessageResult;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.service.GroupRequestService;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageEntity;
import com.badeling.msbot.domain.message.personal.service.PersonalMessageService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageRequestService {
    @Autowired
    private PersonalMessageService personalMessageService;

    @Autowired
    private GroupRequestService groupRequestService;

    private ObjectMapper om = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


    public MessageResult handler(Map<String, Object> request){
        String messageType = (String) request.get("message_type");
        if("private".equals(messageType)){
            PersonMessageEntity message = om.convertValue(request, PersonMessageEntity.class);
            return personalMessageService.handler(message);
        }else if("group".equals(messageType)){
            GroupMessagePostEntity message = om.convertValue(request, GroupMessagePostEntity.class);
            return groupRequestService.handler(message);
        }else {
            return null;
        }
    }
}
