package com.badeling.msbot.infrastructure.cq.config;

import com.badeling.msbot.infrastructure.cq.entity.impl.*;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CpMessageConfig {

//    @Bean
//    public CqMessageBuildService cqMessageBuildService() {
//        return CqMessageEntityCqhttp::new;
//
//        return new CqMessageBuildService(){
//
//            @Override
//            public CqMessageEntity create() {
//                return new CqMessageEntityCqhttp();
//            }
//        }
//    }

    @Bean
    public CqMessageBuildService cqMessageBuildService(@Value("${const.img-folder}") String imageFolder) {
        CqMessageEntityShamrock.FOLDER_PATH = imageFolder + '/';
        return CqMessageEntityShamrock::new;
    }
}
