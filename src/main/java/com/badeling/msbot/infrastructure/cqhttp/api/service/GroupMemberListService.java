package com.badeling.msbot.infrastructure.cqhttp.api.service;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//https://docs.go-cqhttp.org/api/#获取群成员列表
@Service
public class GroupMemberListService {
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public List<GroupMemberListResponseEntity> list(long groupId){
        GroupMemberListRequest request = new GroupMemberListRequest();
        request.setGroup_id(groupId);
//        Result<Object[]> result = restTemplate.postForObject("http://127.0.0.1:5700/get_group_member_list", request, Result.class);
//        System.err.println(result);
//        return Arrays.stream(result.getData())
//                .map(object -> mapper.convertValue(object, GroupMemberListResponseEntity.class))
////                .map(User::getName)
//                .collect(Collectors.toList());
        GroupMemberListResponse result= restTemplate.postForObject("http://127.0.0.1:5700/get_group_member_list", request, GroupMemberListResponse.class);
        return result.getData();
    }
}
