package com.badeling.msbot.interfaces.facade;

import com.badeling.msbot.application.entity.MessageResult;
import com.badeling.msbot.application.entity.NoticeResult;
import com.badeling.msbot.interfaces.assembler.CqhttpResponseAssembler;
import com.badeling.msbot.interfaces.dto.CqhttpResponseDto;
import com.badeling.msbot.domain.request.repository.LastMsgRepository;
import com.badeling.msbot.application.service.NoticeRequestService;
import com.badeling.msbot.application.service.MessageRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("msg")
public class CqhttpController {

    @Autowired
    private LastMsgRepository lastMsg;

    @Autowired
    private NoticeRequestService noticeRequestService;

    @Autowired
    private MessageRequestService messageRequestService;

    @Autowired
    private CqhttpResponseAssembler cqhttpResponseAssembler;

    //https://docs.go-cqhttp.org/event/
    @PostMapping("receive")
    @ResponseBody
    public String cqhttpRequest(@RequestBody Map<String, Object> request) {

        if (request == null || request.isEmpty()) {
            return null;
        }
        if (!lastMsg.addMsg(request.hashCode())) {
            return null;
        }
        if (!request.containsKey("post_type")) {
            return null;
        }

        CqhttpResponseDto response = null;
        String postType = (String) request.get("post_type");
        if ("notice".equals(postType)) {
            NoticeResult result = noticeRequestService.handler(request);
            response = cqhttpResponseAssembler.notice(result);
        } else if ("message".equals(postType)) {
            MessageResult result = messageRequestService.handler(request);
            response = cqhttpResponseAssembler.message(result);
        }
        if (response != null) {
            return response.getJson();
        }
        return null;
    }
}
