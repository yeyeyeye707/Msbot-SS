package com.badeling.msbot.infrastructure.rban.service;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupBanRequest;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMemberListRequest;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMemberListResponseEntity;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupBanService;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMemberListService;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RandomBanService {
    //随机ban一个
    Random random = new Random();
    @Autowired
    GroupMemberListService memberListService;

    @Autowired
    GroupBanService banService;

    @Autowired
    GroupMsgService msgService;


//    @Scheduled(cron ="*/5 * * * * *")
//    public void random(){
//        ban(982603323L);
//    }

    public void ban(Long groupId){
        List<GroupMemberListResponseEntity> all = memberListService.list(groupId);
        if(all == null || all.isEmpty()){
            return;
        }


        List<GroupMemberListResponseEntity> members = all.stream()
                .filter(_m -> "member".equalsIgnoreCase(_m.getRole()) )
                .collect(Collectors.toList());
        if(members.isEmpty()){
            return;
        }


        int haha = random.nextInt(members.size());
        System.out.println(members.get(haha).getUser_id());

        GroupBanRequest request = new GroupBanRequest();
        request.setDuration(60);
        request.setGroup_id(groupId);
        request.setUser_id(members.get(haha).getUser_id());
        banService.ban(request);


//        GroupMsg msg = new GroupMsg();
//        msg.setGroup_id(groupId);
//        msg.setMessage("死!");
//        msgService.sendGroupMsg(msg);
    }
}
