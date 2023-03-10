package com.badeling.msbot.infrastructure.cqhttp.api.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsgList;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//https://docs.go-cqhttp.org/api/#发送群消息
@Service
public class GroupMsgService {
    public GroupMsgService(
            final ConstRepository constRepository
    ){
        restTemplate = new RestTemplate();
        url = constRepository.getFrontEndUrl()  + "/send_group_msg";
    }

    private final RestTemplate restTemplate;
    private final String url;

    /**
     * 发送群消息
     *
     * @param groupMsg
     * @return
     */
    public Result<?> sendGroupMsg(GroupMsg groupMsg) {
        groupMsg.setMessage(groupMsg.getMessage().replaceAll("\\\\", "/"));
        Result<?> result = restTemplate.postForObject(url, groupMsg, Result.class);
        System.err.println(result.toString());
        return result;
    }

    public void sendGroupMsgList(GroupMsgList groupMsgList) {
        Runnable task = new DelayTask(this, groupMsgList);
        Thread t = new Thread(task);
        t.start();
    }


    public static class DelayTask implements Runnable {
        public DelayTask(GroupMsgService msgService, GroupMsgList msgList) {
            this.msgService = msgService;
            this.init(msgList);
        }

        private void init(GroupMsgList msgList) {
            this.msgList = msgList.getMessage();
            this.i = 0;
            this.j = 0;
            int l = msgList.getGroup_id().length;
            this.msg = new GroupMsg[l];

            while (l-- != 0) {
                GroupMsg _msg = new GroupMsg();
                _msg.setGroup_id(msgList.getGroup_id()[l]);
                _msg.setAuto_escape(msgList.isAuto_escape());
                this.msg[l] = _msg;
            }
        }

        private GroupMsgService msgService;
        private String[] msgList;
        private int i;
        private int j;

        private GroupMsg[] msg;

        @Override
        public void run() {
            GroupMsg _msg;
            while (i < msg.length) {
                j = 0;
                _msg = msg[i];

                while (j < msgList.length) {
                    try {
                        Thread.sleep(300L);
                    } catch (InterruptedException ignored) {

                    }
                    _msg.setMessage(msgList[j]);
                    msgService.sendGroupMsg(_msg);
                    j++;
                }
                i++;
            }
        }
    }
}
