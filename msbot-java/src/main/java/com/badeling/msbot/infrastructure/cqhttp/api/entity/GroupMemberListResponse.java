package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.Data;

import java.util.List;

@Data
public class GroupMemberListResponse extends Result<List<GroupMemberListResponseEntity>>{
//public class GroupMemberListResponse{
    List<GroupMemberListResponseEntity> data;

    int retCode;

    String status;
}
