package com.badeling.msbot.infrastructure.maplegg.repository;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.dao.repository.LevelExpRepository;
import com.badeling.msbot.infrastructure.maplegg.entity.CharacterData;
import com.badeling.msbot.infrastructure.maplegg.entity.GraphData;
import com.badeling.msbot.infrastructure.util.NumberSuffixesUtil;
import jakarta.annotation.Nullable;
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
public class RankInfoImgRepository {

    private final ConstRepository constRepository;

    private final LevelExpRepository levelExpRepository;

    private final String fontName;

    private final Color textColor;

    private BufferedImage bgImage;

    private BufferedImage labelImage;

    private BufferedImage nameLabelImage;

    private BufferedImage panelImage;

    public RankInfoImgRepository(
            final ConstRepository constRepository,
            final LevelExpRepository levelExpRepository
    ) {
        this.constRepository = constRepository;
        this.levelExpRepository = levelExpRepository;
        this.fontName = this.initFontName();
        this.textColor = new Color(0x722d34);

        this.bgImage = null;
        this.labelImage = null;
        this.nameLabelImage = null;
    }

    private String initFontName() {

        String f = null;
        try {
            // 字体文件，放在 resources 目录的 fonts 文件下
            var in = getClass().getResourceAsStream("/OPPOSans-M.ttf");

            if (in != null) {
                var font = Font.createFont(Font.TRUETYPE_FONT, in);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);

                f = font.getFontName();
                log.info("font:{}", f);
            }
        } catch (IOException e) {
            log.error("导入字体异常", e);
        } catch (FontFormatException e) {
            log.error("字体格式异常", e);
        }

        //加载失败 默认字体
        if (f == null) {
            Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            if (fonts != null && fonts.length > 0) {
                var font = fonts[0];
                f = font.getFontName();
            }
        }

//        fontName = f;
        return f;
    }

    @Nullable
    public String saveImg(CharacterData character) {

        var image = new BufferedImage(720, 384, BufferedImage.TYPE_3BYTE_BGR);
        var g2d = image.createGraphics();

        //文字抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //背景图.
        {
            var bg = getBgImage();
            if (bg != null) {
                g2d.drawImage(bg, 0, 0, null);
            }
        }


        //中间 角色
        {
            //画角色
            var characterImage = getCharacterImage(character.getImgUrl());
            int x = (image.getWidth() - characterImage.getWidth()) / 2;
            int y = (image.getHeight() - characterImage.getHeight()) / 2 + 33;
            g2d.drawImage(characterImage, x, y, null);

            //画名牌戒指
            var nameImage = getNameLabelImage();
            x = (image.getWidth() - nameImage.getWidth()) / 2;
            y += characterImage.getHeight() + 2;
            g2d.drawImage(nameImage, x, y, null);

            //字体
            Font font = new Font(fontName, Font.PLAIN, 10);
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            //画名字
            String name = character.getName();
            x = (image.getWidth() - metrics.stringWidth(name)) / 2;
            y += (nameImage.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent() + 1;

            g2d.setColor(textColor);
            g2d.drawString(name, x, y);

//            // 画出基线
//            g2d.drawLine(x, y, x + metrics.stringWidth(name), y);
        }

        //左边信息
        {
            //字体
            Font font = new Font(fontName, Font.PLAIN, 18);
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);

            var labelImage = getLabelImage();
            int leftX = 10;
            int topY = 10;
            int offset = 1;
            int x;
            int y;

            //key - value pair 绘制
            for (var e : character.getLeftLabelMap()) {

                x = leftX;
                y = topY;
                g2d.drawImage(labelImage, x, y, null);

                x += 37;
                y += (labelImage.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent() - 2;
                g2d.drawString(e.getK(), x, y);

                x += 83;
                g2d.drawString(e.getV(), x, y);

                topY += labelImage.getHeight() + offset;
            }
        }

        //右边经验
        {
            var expData = character.getGraphDataList();
            int l = expData.size();
            if (l > 0) {
                //板子
                var panel = getPanelImage();
                int topX = image.getWidth() - panel.getWidth() - 10;
                int topY = (image.getHeight() - panel.getHeight()) / 2;
                g2d.drawImage(panel, topX, topY, null);

                //柱状图
                topX += 58;
                topY += 38;
                int width = panel.getWidth() - 110;
                int height = panel.getHeight() - 70;
                g2d.setColor(Color.BLACK);
                g2d.drawLine(topX, topY, topX + width, topY);
                g2d.drawLine(topX, topY, topX, topY + height);

                topX += 5;
                topY += 2;
                int offset = 2;
                int h = (height - offset * (l - 1)) / l;
                float maxExp = expData.stream()
                        .mapToLong(GraphData::getExp)
                        .max()
                        .getAsLong();
                long sum = 0;
                long lastDay = 1;

                //字体
                Font font = new Font(fontName, Font.PLAIN, 12);
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
                    g2d.setColor(textColor);
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
                var needExp = levelExpRepository.getLevelExpBy(character.getLevel())
                        .getNeedExp();

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

    private BufferedImage getBgImage() {
        if (bgImage != null) {
            return bgImage;
        }

        BufferedImage image = null;
        try {
            var bg = this.getClass().getResourceAsStream("/img/bg.png");
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

    private BufferedImage getCharacterImage(String imgUrl) {
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


    private BufferedImage getLabelImage() {
        if (labelImage != null) {
            return labelImage;
        }


        BufferedImage image = null;
        try {
            var bg = this.getClass().getResourceAsStream("/img/label.png");
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

    private BufferedImage getNameLabelImage() {
        if (nameLabelImage != null) {
            return nameLabelImage;
        }


        BufferedImage image = null;
        try {
            var bg = this.getClass().getResourceAsStream("/img/nameLabel.png");
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


    private BufferedImage getPanelImage() {
        if (panelImage != null) {
            return panelImage;
        }


        BufferedImage image = null;
        try {
            var bg = this.getClass().getResourceAsStream("/img/panel.png");
            if (bg != null) {
                image = ImageIO.read(bg);
                bg.close();
            }
        } catch (Exception ex) {
            log.warn("load name label failed.", ex);
        }
        if (image == null) {
            image = new BufferedImage(267, 339, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            panelImage = image;
        }
        return image;
    }
}
