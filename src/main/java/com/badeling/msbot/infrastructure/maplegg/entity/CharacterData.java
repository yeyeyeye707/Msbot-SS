package com.badeling.msbot.infrastructure.maplegg.entity;

import com.badeling.msbot.infrastructure.dao.entity.LevelExp;
import com.badeling.msbot.infrastructure.dao.repository.LevelExpRepository;
import com.badeling.msbot.infrastructure.util.ImgUtil;
import com.badeling.msbot.infrastructure.util.JfreeChartUtil;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import com.badeling.msbot.infrastructure.util.NumberSuffixesUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Data
public class CharacterData {
    @JsonProperty(value = "CharacterImageURL")
    private String imgUrl;

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "Class")
    private String characterClass;

    @JsonProperty(value = "Level")
    private int level;

    @JsonProperty(value = "EXP")
    private  long exp;

    @JsonProperty(value = "EXPPercent")
    private double expPercent;

    @JsonProperty(value = "Server")
    private String server;

    @JsonProperty(value = "ServerRank")
    private int serverRank;

    @JsonProperty(value = "ServerClassRanking")
    private int serverClassRank;

    @JsonProperty(value = "LegionLevel")
    private Integer legionLevel;

    @JsonProperty(value = "LegionRank")
    private Integer legionRank;

    @JsonProperty(value = "LegionPower")
    private Long legionPower;

    @JsonProperty(value = "LegionCoinsPerDay")
    private Integer legionCoinsPerDay;


    @JsonProperty(value = "GraphData")
    private List<GraphData> graph;

    public String getCharacterString(LevelExpRepository levelExpRepository ) {
        StringBuilder sb = new StringBuilder();

        //保存角色图
        try {
            String imageName = ImgUtil.saveTempImage(imgUrl);
            sb.append("[CQ:image,file=").append(imageName).append("]\r\n");
        } catch (Exception e) {

        }

        sb.append("角色：").append(name).append("\r\n");
        sb.append("服务器：").append(server).append("\r\n");
        String _server = server.substring(0,1);
        sb.append("职业：").append(characterClass).append("  (").append(_server).append("区职业第").append(serverClassRank).append("名)\r\n");
        sb.append("等级：").append(level).append('-').append(expPercent).append("%  (").append(_server).append("区第").append(serverRank).append("名)\r\n");
        if (legionLevel != null && legionLevel > 0
                && legionRank != null && legionRank > 0) {
            sb.append("联盟等级：").append(legionLevel).append("（").append(_server).append("区第").append(legionRank).append("名）\r\n");
        }
        if (legionPower != null && legionPower > 0
                && legionCoinsPerDay != null && legionCoinsPerDay > 0) {
            sb.append("联盟战斗力：").append(legionPower).append("（每日").append(legionCoinsPerDay).append("联盟币）\r\n");
        }

        //经验
        if (graph == null) {
            sb.append("努力程度没查到\r\n");
        } else {
            //几天升级
            OptionalDouble averageOptional = graph.stream()
                    .mapToLong(GraphData::getExp)
                    .average();
            LevelExp levelExp = levelExpRepository.getLevelExpBy(level);
            if(averageOptional.isPresent() && levelExp != null){
                double average = averageOptional.getAsDouble();
                if(average > 0){
                    long need = levelExp.getNeedExp() - exp;
                    sb.append("按照平均努力程度，需要").append(Math.round(need/average)).append("天升级\r\n");
                }else{
                    sb.append("摆烂中\r\n");
                }
            }

            List<GraphData> expDatas = graph.stream()
//                    .filter(GraphData::check)
                    .sorted()
                    .collect(Collectors.toList());
            if(!expDatas.isEmpty()){
                String expImg = JfreeChartUtil.createExpBarImg(expDatas, MsbotConst.imageUrl);
                if (expImg != null && !expImg.isEmpty()) {
                    sb.append("[CQ:image,file=").append(expImg).append("]\r\n");
                } else {
                    graph.stream()
                            .forEach(g -> {
                                sb.append(g.getDate()).append(" ：\t").append(NumberSuffixesUtil.getExpStr(g.getExp())).append("\r\n");
                            });
                }
            }
        }

        return sb.toString();
    }
}
