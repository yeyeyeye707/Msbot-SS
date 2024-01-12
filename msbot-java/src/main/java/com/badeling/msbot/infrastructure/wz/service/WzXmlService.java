package com.badeling.msbot.infrastructure.wz.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.dao.entity.MobInfo;
import com.badeling.msbot.infrastructure.dao.entity.MobName;
import com.badeling.msbot.infrastructure.dao.repository.MobInfoRepository;
import com.badeling.msbot.infrastructure.dao.repository.MobNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class WzXmlService {
    @Autowired
    private MobInfoRepository mobInfoRepository;

    @Autowired
    private MobNameRepository mobNameRepository;

    @Autowired
    private ConstRepository constRepository;

    @Autowired
    private CqMessageBuildService cqMessageBuildService;

    public void updateMobInfo() {
        //TODO..
    }

    public String searchMobById(String mob_id) {
        MobInfo mobInfo = mobInfoRepository.findMobInfoByMobId(mob_id);
        MobName mobName = mobNameRepository.findByMobId(mob_id);
        return buildReply(mobInfo, mobName);
    }

    public String searchMobByName(String name) {
        List<MobName> mobNames = mobNameRepository.findByName(name);
        StringBuilder r = new StringBuilder();
        MobInfo mobInfo;
        for (MobName n : mobNames) {
            mobInfo = mobInfoRepository.findMobInfoByMobId(n.getMobId());
            r.append(buildReply(mobInfo, n));
        }
        return r.toString();
    }

    private String buildReply(MobInfo mobInfo, MobName mobName) {
        if (mobInfo == null || mobName == null) {
            return "";
        }

        StringBuilder reply = new StringBuilder();

        appedImg(reply, mobInfo.getLink());
        appedImg(reply, mobInfo.getMobId());

        reply.append("\r\n")
                .append(mobName.getName())
                .append('(')
                .append(mobName.getMobId())
                .append(")\r\n");

        reply.append("怪物类型:")
                .append(mobInfo.getBoss() == null ? "普通怪" : "首领怪")
                .append("\r\n");

        reply.append("等级:").append(mobInfo.getLevel()).append("\r\n")
                .append("血量:").append(mobInfo.getMaxHp()).append("\r\n")
//				.append("蓝量:").append(mobInfo.getMaxMp()).append("\r\n")
                .append("防御:").append(mobInfo.getPdRate()).append("%\r\n")
//				.append("魔法防御:").append(mobInfo.getMdRate()).append("%\r\n")
//				.append("移动速度:").append(mobInfo.getSpeed()).append("\r\n")
//				.append("击退伤害:").append(mobInfo.getPushed()).append("\r\n")
                .append("经验:").append(mobInfo.getExp()).append("\r\n")
                .append("冰雷火毒圣暗物").append("\r\n");

        if (mobInfo.getElemAttr() == null) {
            reply.append("○○○○○○○");
        } else {
            String attr = mobInfo.getElemAttr();
            Map<String, String> attrMap = new HashMap<>();
            attrMap.put("I", "○");
            attrMap.put("L", "○");
            attrMap.put("F", "○");
            attrMap.put("S", "○");
            attrMap.put("H", "○");
            attrMap.put("D", "○");
            attrMap.put("P", "○");

            int index = 0;
            while (index < attr.length()) {
                if (attr.charAt(index + 1) == '1') {
                    attrMap.put(attr.substring(index, index + 1), "×");
                } else if (attr.charAt(index + 1) == '2') {
                    attrMap.put(attr.substring(index, index + 1), "△");
                } else if (attr.charAt(index + 1) == '3') {
                    attrMap.put(attr.substring(index, index + 1), "◎");
                }
                index = index + 2;
                System.out.println(index);
            }
            reply.append(attrMap.get("I"))
                    .append(attrMap.get("L"))
                    .append(attrMap.get("F"))
                    .append(attrMap.get("S"))
                    .append(attrMap.get("H"))
                    .append(attrMap.get("D"))
                    .append(attrMap.get("P"));
        }

        return reply.toString();
    }

    private void appedImg(StringBuilder reply, String folder) {
        if (folder == null || folder.isEmpty()) {
            return;
        }

        var f = "result_img/" + folder + ".img/" + folder + ".img";
        String path = constRepository.getImageUrl() + f;
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                var e = cqMessageBuildService.create()
                        .image(f + "/" + files[0].getName());
                reply.append(e.getMessage());
            }
        }
    }
}
