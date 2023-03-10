package com.badeling.msbot.infrastructure.cqhttp.api.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupBanRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//https://docs.go-cqhttp.org/api/#群组单人禁言
@Service
public class GroupBanService {
    private final RestTemplate restTemplate;
    private final String url;

    public GroupBanService(
            final ConstRepository constRepository
    ){
        restTemplate = new RestTemplate();
        url = constRepository.getFrontEndUrl() + "/set_group_ban";
    }

    public void ban(GroupBanRequest request){

        String result = restTemplate.postForObject(url, request, String.class);
        System.err.println(result);
    }
}
