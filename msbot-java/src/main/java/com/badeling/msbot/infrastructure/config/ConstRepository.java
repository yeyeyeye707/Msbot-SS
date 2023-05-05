package com.badeling.msbot.infrastructure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Getter
//@ConfigurationProperties(prefix = "const")
public class ConstRepository {

    @Value("${const.front-end-url}")
    private String frontEndUrl;

    @Value("${const.img-folder}")
    private String imageUrl;

    @Value("${const.bot-name}")
    private String botName;

    @Value("${const.bot-qq}")
    private String botQQ;

    @Value("${const.master-qq}")
    private Long masterQQ;

    @Value("#{'${const.manager-qq}'.split(',')}")
    private List<Long> managerQQ;
}
