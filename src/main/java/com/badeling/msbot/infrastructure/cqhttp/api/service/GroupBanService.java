package com.badeling.msbot.infrastructure.cqhttp.api.service;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupBanRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//https://docs.go-cqhttp.org/api/#群组单人禁言
@Service
public class GroupBanService {
    private RestTemplate restTemplate = new RestTemplate();

    public void ban(GroupBanRequest request){

        String result = restTemplate.postForObject("http://127.0.0.1:5700/set_group_ban", request, String.class);
        System.err.println(result);
    }
}
