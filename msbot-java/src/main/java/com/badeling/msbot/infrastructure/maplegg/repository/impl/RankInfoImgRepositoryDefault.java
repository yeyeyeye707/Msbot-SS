package com.badeling.msbot.infrastructure.maplegg.repository.impl;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.dao.repository.LevelExpRepository;
import com.badeling.msbot.infrastructure.maplegg.entity.CharacterData;
import com.badeling.msbot.infrastructure.maplegg.entity.GraphData;
import com.badeling.msbot.infrastructure.maplegg.entity.RankRenderConfig;
import com.badeling.msbot.infrastructure.maplegg.repository.RankInfoImgRepository;
import com.badeling.msbot.infrastructure.util.NumberSuffixesUtil;
import com.badeling.msbot.infrastructure.util.StringUtil;
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
public class RankInfoImgRepositoryDefault extends RankInfoImgRepositoryImpl {
    public RankInfoImgRepositoryDefault(final ConstRepository constRepository, final LevelExpRepository levelExpRepository) {
        super(constRepository, levelExpRepository, RankRenderConfig.DEFAULT);
    }

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
