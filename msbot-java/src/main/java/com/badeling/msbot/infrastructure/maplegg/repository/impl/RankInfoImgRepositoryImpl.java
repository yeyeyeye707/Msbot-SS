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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.UUID;

//@Repository
@Slf4j
public abstract class RankInfoImgRepositoryImpl implements RankInfoImgRepository {

    protected final ConstRepository constRepository;

    protected final LevelExpRepository levelExpRepository;

    protected BufferedImage bgImage;

    protected BufferedImage labelImage;

    protected BufferedImage nameLabelImage;

    protected BufferedImage panelImage;

    protected RankRenderConfig config;

    public RankInfoImgRepositoryImpl(final ConstRepository constRepository,
                                     final LevelExpRepository levelExpRepository,
                                     RankRenderConfig config) {
        this.constRepository = constRepository;
        this.levelExpRepository = levelExpRepository;
        this.config = config;
    }

    @Override
    public String saveImg(CharacterData character) {
        ColorModel cm = ColorModel.getRGBdefault();
        WritableRaster wr = cm.createCompatibleWritableRaster(config.getSize().x, config.getSize().y);

//        var image = new BufferedImage(config.getSize().x, config.getSize().y, BufferedImage.TYPE_3BYTE_BGR);

        var image = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
        var g2d = image.createGraphics();

        //文字抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //背景图.
        renderBg(g2d);

        //左边信息
        renderLabels(g2d, character);

        //右边经验
        renderExp(g2d, character);

        //中间 角色
        renderCharacter(g2d, character);

        //结束
        g2d.dispose();

        //保存
        try {
            String path = constRepository.getImageUrl();
            // 输出的文件流
            File sf = new File(path);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            String imageName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
            var outStream = new FileOutputStream(new File(sf.getPath() + "/" + imageName));
            ImageIO.write(image, "png", outStream);
            outStream.close();

            return imageName;
        } catch (Exception ex) {
            log.warn("save image failed.", ex);
            return null;
        }
    }

    /**
     * 渲染背景层
     *
     * @param g2d 图形
     */
    protected void renderBg(Graphics2D g2d) {
        var bg = getBgImage();
        if (bg != null) {
            g2d.drawImage(bg, 0, 0, null);
        }
    }

    protected BufferedImage getBgImage() {
        if (bgImage != null) {
            return bgImage;
        }

        BufferedImage image = null;
        try {
            var bg = this.getClass().getResourceAsStream(config.getImgFileBg());
            if (bg != null) {
                image = ImageIO.read(bg);
                bg.close();
            }
        } catch (Exception ex) {
            log.warn("load bg failed.", ex);
        }
        if (image == null) {
            return null;
        }
        bgImage = image;
        return image;
    }

    /**
     * 渲染角色
     *
     * @param g2d       图形
     * @param character 角色
     */
    protected void renderCharacter(Graphics2D g2d, CharacterData character) {
        var position = config.getCharacterPosition();
        //画角色
        var characterImage = getCharacterImage(character.getImgUrl());
        int x = position.x - (characterImage.getWidth() / 2);
        int y = position.y - (characterImage.getHeight() / 2);
        g2d.drawImage(characterImage, x, y, null);

        //画名牌戒指
        var nameImage = getNameLabelImage();
        x = position.x - (nameImage.getWidth() / 2);
        y += characterImage.getHeight() + 2;
        g2d.drawImage(nameImage, x, y, null);

        //字体
        Font font = new Font(config.getFontName(), Font.PLAIN, 10);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        //画名字
        String name = character.getName();
        x = position.x - (metrics.stringWidth(name) / 2) + config.getNameLabelPositionOffset().x;
        y += (nameImage.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent() + config.getNameLabelPositionOffset().y;

        g2d.setColor(config.getTextColor());
        g2d.drawString(name, x, y);

//            // 画出基线
//            g2d.drawLine(x, y, x + metrics.stringWidth(name), y);
    }

    protected BufferedImage getCharacterImage(String imgUrl) {
        BufferedImage image = null;
        try {
            // 构造URL
            URL url = new URL(imgUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            // 输入流
            InputStream is = con.getInputStream();
            image = ImageIO.read(is);
            is.close();
        } catch (Exception ex) {
            log.warn("load character image failed.", ex);
        }
        if (image == null) {
            image = new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR);
            image.setRGB(10, 10, Color.RED.getRGB());
        }
        return image;
    }

    protected BufferedImage getNameLabelImage() {
        if (nameLabelImage != null) {
            return nameLabelImage;
        }


        BufferedImage image = null;
        try {
            var bg = this.getClass().getResourceAsStream(config.getImgFileNameLabel());
            if (bg != null) {
                image = ImageIO.read(bg);
                bg.close();
            }
        } catch (Exception ex) {
            log.warn("load name label failed.", ex);
        }
        if (image == null) {
            image = new BufferedImage(101, 21, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            nameLabelImage = image;
        }
        return image;
    }

    /**
     * 渲染文字内容
     *
     * @param g2d       图形
     * @param character 角色
     */
    protected void renderLabels(Graphics2D g2d, CharacterData character) {
        //字体
        Font font = new Font(config.getFontName(), Font.PLAIN, 18);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        g2d.setColor(config.getTextColor());

        var labelImage = getLabelImage();
        int leftX = config.getLabelsStartPosition().x;
        int topY = config.getLabelsStartPosition().y;
        int x;
        int y;

        //key - value pair 绘制
        for (var e : character.getLeftLabelMap()) {

            x = leftX;
            y = topY;
            g2d.drawImage(labelImage, x, y, null);

            x += config.getLabelsTextOffset().x;
            y += (labelImage.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent() + config.getLabelsTextOffset().y;
            g2d.drawString(e.getK(), x, y);

            x += 83;
            g2d.drawString(e.getV(), x, y);

            topY += labelImage.getHeight() + config.getLabelOffset();
        }
    }

    protected BufferedImage getLabelImage() {
        if (labelImage != null) {
            return labelImage;
        }


        BufferedImage image = null;
        try {
            var bg = this.getClass().getResourceAsStream(config.getImgFileLabel());
            if (bg != null) {
                image = ImageIO.read(bg);
                bg.close();
            }
        } catch (Exception ex) {
            log.warn("load name label failed.", ex);
        }
        if (image == null) {
            image = new BufferedImage(284, 40, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            labelImage = image;
        }
        return image;
    }

    /**
     * 渲染经验内容
     *
     * @param g2d       图形
     * @param character 角色
     */
    protected void renderExp(Graphics2D g2d, CharacterData character) {
        var expData = character.getGraphDataList();
        int l = expData.size();
        if (l > 0) {
            //板子
            var panel = getPanelImage();
            int topX = config.getPanelPosition().x;
            int topY = config.getPanelPosition().y;
            g2d.drawImage(panel, topX, topY, null);

            //柱状图
            topX += config.getPanelLinePosition().x;
            topY += config.getPanelLinePosition().y;
            int width = panel.getWidth() - 110;
            int height = panel.getHeight() - 70;
            g2d.setColor(Color.BLACK);
            g2d.drawLine(topX, topY, topX + width, topY);
            g2d.drawLine(topX, topY, topX, topY + height);

            topX += 5;
            topY += 2;
            int offset = 2;
            int h = (height - offset * (l - 1)) / l;
            float maxExp = expData.stream().mapToLong(GraphData::getExp).max().getAsLong();
            long sum = 0;
            long lastDay = 1;

            //字体
            Font font = new Font(config.getFontName(), Font.PLAIN, 12);
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);

            //画柱子
            GraphData data;
            Iterator<GraphData> i = expData.iterator();
            long lastExp = i.next().getTotalExp();
            while (i.hasNext()) {
                data = i.next();

                //柱子
                g2d.setColor(Color.GREEN);
                long exp = data.getTotalExp() - lastExp;
                int w = (int) (width * (exp / maxExp));
                g2d.fillRect(topX, topY, w, h);

                //日期 & 数量
                g2d.setColor(config.getTextColor());
                int y = topY + (h - metrics.getHeight()) / 2 + metrics.getAscent();
                g2d.drawString(data.getDate(), topX - metrics.stringWidth(data.getDate()) - 5, y);
                String expText = NumberSuffixesUtil.getExpStr(exp);
                g2d.drawString(expText, topX + w + 2, y);

                topY += offset + h;
                lastExp = data.getTotalExp();
                sum += exp;
                lastDay = exp;
            }

            //升级预期
            var needExp = levelExpRepository.getLevelExpBy(character.getLevel()).getNeedExp();

            var average = (sum / l);
            if (average > 0) {
                topY += metrics.getHeight() + 2;
                long needDay = (needExp - character.getExp()) / average;
                g2d.drawString("按照平均，还需%d天升级".formatted(needDay), topX, topY);
            }

            if (lastDay > 0) {
                topY += metrics.getHeight() + 2;
                long needDay = (needExp - character.getExp()) / lastDay;
                g2d.drawString("按照最后一天，还需%d天升级".formatted(needDay), topX, topY);
            }
        }
    }

    protected BufferedImage getPanelImage() {
        if (panelImage != null) {
            return panelImage;
        }


        BufferedImage image = null;
        if (!StringUtil.isEmpty(config.getImgFilePanel())) {
            try {
                var bg = this.getClass().getResourceAsStream(config.getImgFilePanel());
                if (bg != null) {
                    image = ImageIO.read(bg);
                    bg.close();
                }
            } catch (Exception ex) {
                log.warn("load name label failed.", ex);
            }
        }

        if (image == null) {
            image = new BufferedImage(config.getPanelSize().x, config.getPanelSize().y, BufferedImage.TYPE_3BYTE_BGR);
            //透明
            var g = image.createGraphics();
            panelImage = g.getDeviceConfiguration().createCompatibleImage(config.getPanelSize().x, config.getPanelSize().y, Transparency.TRANSLUCENT);
        } else {
            panelImage = image;
        }

        return panelImage;
    }
}
