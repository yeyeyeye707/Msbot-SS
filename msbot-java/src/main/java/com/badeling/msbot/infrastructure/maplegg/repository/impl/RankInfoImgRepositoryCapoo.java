package com.badeling.msbot.infrastructure.maplegg.repository.impl;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.dao.repository.LevelExpRepository;
import com.badeling.msbot.infrastructure.maplegg.entity.CharacterData;
import com.badeling.msbot.infrastructure.maplegg.entity.GraphData;
import com.badeling.msbot.infrastructure.maplegg.entity.RankRenderConfig;
import com.badeling.msbot.infrastructure.maplegg.repository.RankInfoImgRepository;
import com.badeling.msbot.infrastructure.util.NumberSuffixesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.UUID;

@Repository
@Slf4j
public class RankInfoImgRepositoryCapoo extends RankInfoImgRepositoryImpl {
    BufferedImage transImage;

    public RankInfoImgRepositoryCapoo(final ConstRepository constRepository, final LevelExpRepository levelExpRepository) {
        super(constRepository, levelExpRepository, RankRenderConfig.CAPOO);
    }


    @Override
    public Integer getId() {
        return 2;
    }

    @Override
    public int getOrder() {
        return 1;
    }

//    /**
//     * 渲染背景层
//     *
//     * @param g2d 图形
//     */
//    protected void renderBg(Graphics2D g2d) {
//        renderTransport(g2d);
//
//        var bg = getBgImage();
//
//        if (bg != null) {
//            g2d.drawImage(bg, 0, 0, null);
//        }
//    }
//
//    protected void renderTransport(Graphics2D g2d) {
//
//        //透明
//        if (transImage == null) {
//            var image = new BufferedImage(config.getSize().x, config.getSize().y, BufferedImage.TYPE_3BYTE_BGR);
//            //透明
//            var g = image.createGraphics();
//            transImage = g.getDeviceConfiguration().createCompatibleImage(config.getSize().x, config.getSize().y, Transparency.TRANSLUCENT);
//        }
//
//        g2d.drawImage(transImage, 0, 0, null);
//    }
}
