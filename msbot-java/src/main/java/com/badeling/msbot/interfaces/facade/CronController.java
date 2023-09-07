package com.badeling.msbot.interfaces.facade;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.official.service.OfficialNewScheduledComponent;
import com.badeling.msbot.infrastructure.repeat.service.RepeatService;
import com.badeling.msbot.infrastructure.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cron")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CronController {

    private final OfficialNewScheduledComponent officialNewScheduledComponent;
    private final RepeatService repeatService;
    private final GroupMsgService groupMsgService;

    @GetMapping("news")
    public String news() {
        officialNewScheduledComponent.check();

        return "start";
    }

    @GetMapping("repeat")
    public String repeat(@RequestParam String gid) {
        if (!StringUtil.isEmpty(gid)) {
            try {
                var _gid = Long.parseLong(gid);
                //发送周报
                var report = repeatService.getRepeatReport(_gid);

                if (!StringUtil.isEmpty(report)) {
                    var send = new GroupMsg();
                    send.setGroup_id(_gid);
                    send.setAuto_escape(false);
                    send.setMessage(report);
                    groupMsgService.sendGroupMsg(send);
                }
            } catch (Exception ex) {

            }
        }

        //清理
        repeatService.clearAll();

        return "clear";
    }
}
