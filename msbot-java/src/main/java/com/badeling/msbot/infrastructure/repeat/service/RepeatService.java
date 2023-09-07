package com.badeling.msbot.infrastructure.repeat.service;

import com.badeling.msbot.infrastructure.dao.repository.RepeatSentenceRepository;
import com.badeling.msbot.infrastructure.dao.repository.RepeatTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RepeatService {
    private final RepeatTimeRepository repeatTimeRepository;
    private final RepeatSentenceRepository repeatSentenceRepository;

    public String getRepeatReport(Long gid) {
        var sb = new StringBuilder();
        sb.append("——————————————————\r\n");

        var sentence = repeatSentenceRepository.getFirstSentence(gid);
        if (sentence != null) {
            sb.append("本周最长的复读长龙是：\r\n");
            sb.append(sentence.getMsg())
                    .append("\r\n");

            sb.append("此金句出自———————[CQ:at,qq=")
                    .append(sentence.getUser_id())
                    .append("]");

            sb.append("当时被复读机们连续复读了")
                    .append(sentence.getCount())
                    .append("次！\r\n");
        }

        var times = repeatTimeRepository.find3thByGroup(gid);
        if (!times.isEmpty()) {
            var first = times.get(0);
            sb.append("本周最佳复读机的称号授予[CQ:at,qq=")
                    .append(first.getUser_id())
                    .append("]！\r\n");
            sb.append("他在过去的一周里疯狂复读")
                    .append(first.getCount())
                    .append("次！简直太丧病了。\r\n");

            if (times.size() > 2) {
                sb.append("此外，以下两名成员获得了亚军和季军，也是非常优秀的复读机：\r\n");
                for (int i = 1; i < 3; i++) {
                    var record = times.get(i);
                    sb.append("[CQ:at,qq=")
                            .append(record.getUser_id())
                            .append("] 复读次数：")
                            .append(record.getCount())
                            .append("\r\n");
                }
            } else if (times.size() > 1) {
                sb.append("此外，以下成员获得了亚军，也是非常优秀的复读机：\r\n");
                var record = times.get(1);
                sb.append("[CQ:at,qq=")
                        .append(record.getUser_id())
                        .append("] 复读次数：")
                        .append(record.getCount())
                        .append("\r\n");
            }

            sb.append("为了成为最佳复读机，努力复读吧！uwu");
        } else {
            sb.append("owo,本群没有复读机。");
        }


        return sb.toString();
    }

    @Async
    public void clearAll() {
        repeatTimeRepository.clearAll();
        repeatSentenceRepository.clearAll();
    }
}
