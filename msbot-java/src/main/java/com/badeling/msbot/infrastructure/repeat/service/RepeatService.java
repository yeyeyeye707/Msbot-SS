package com.badeling.msbot.infrastructure.repeat.service;

import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
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
    private final CqMessageBuildService cqMessageBuildService;

    public CqMessageEntity getRepeatReport(Long gid) {
        var cq = cqMessageBuildService.create();
        cq.notAutoEscape()
                .text("——————————————————").changeLine();

        var sentence = repeatSentenceRepository.getFirstSentence(gid);
        if (sentence != null) {
            cq.text("本周最长的复读长龙是：").changeLine();
            cq.text(sentence.getMsg()).changeLine();
            cq.text("此金句出自———————").atQQ(sentence.getUser_id());
            cq.text("当时被复读机们连续复读了" + sentence.getCount() + "次!").changeLine();
        }

        var times = repeatTimeRepository.find3thByGroup(gid);
        if (!times.isEmpty()) {
            var first = times.get(0);
            cq.text("本周最佳复读机的称号授予").atQQ(first.getUser_id()).changeLine();
            cq.text("他在过去的一周里疯狂复读" + first.getCount() + "次！简直太丧病了。").changeLine();

            if (times.size() > 2) {
                cq.text("此外，以下两名成员获得了亚军和季军，也是非常优秀的复读机：").changeLine();
                for (int i = 1; i < 3; i++) {
                    var record = times.get(i);
                    cq.atQQ(record.getUser_id()).text(" 复读次数：" + record.getCount()).changeLine();
                }
            } else if (times.size() > 1) {
                cq.text("此外，以下成员获得了亚军，也是非常优秀的复读机：").changeLine();
                var record = times.get(1);
                cq.atQQ(record.getUser_id()).text(" 复读次数：" + record.getCount()).changeLine();
            }

            cq.text("为了成为最佳复读机，努力复读吧！uwu");
        } else {
            cq.text("owo,本群没有复读机。");
        }
        return cq;
    }

    @Async
    public void clearAll() {
        repeatTimeRepository.clearAll();
        repeatSentenceRepository.clearAll();
    }
}
