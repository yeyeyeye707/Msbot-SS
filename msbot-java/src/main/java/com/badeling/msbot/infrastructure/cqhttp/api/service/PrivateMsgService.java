package com.badeling.msbot.infrastructure.cqhttp.api.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.PrivateMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PrivateMsgService {

    public PrivateMsgService(
            final ConstRepository constRepository
    ){
        restTemplate = new RestTemplate();
        url = constRepository.getFrontEndUrl()  + "/send_private_msg";
    }

    private final RestTemplate restTemplate;
    private final String url;

    public Result<?> sendPrivateMsg(PrivateMsg privateMsg) {
        privateMsg.setMessage(privateMsg.getMessage().replaceAll("\\\\", "/"));
        Result<?> result = restTemplate.postForObject(url, privateMsg, Result.class);
//        System.err.println(result.toString());
        return result;
    }
}
