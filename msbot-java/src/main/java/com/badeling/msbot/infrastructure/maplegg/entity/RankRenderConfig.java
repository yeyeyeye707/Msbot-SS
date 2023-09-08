package com.badeling.msbot.infrastructure.maplegg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;

@Data
@Slf4j
public class RankRenderConfig {

    String fontName;
    Color textColor;

    XY size;
    String imgFileBg;

    XY characterPosition;
    String imgFileNameLabel;
    XY nameLabelPositionOffset;

    String imgFileLabel;
    XY labelsStartPosition;
    XY labelsTextOffset;
    int labelOffset;

    XY panelSize;
    String imgFilePanel;
    XY panelPosition;
    XY panelLinePosition;

    public static RankRenderConfig DEFAULT = createDefault();
    public static RankRenderConfig CAPOO = createCapoo();

    protected String initFontName() {

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

    static RankRenderConfig createDefault() {
        var c = new RankRenderConfig();

        c.fontName = c.initFontName();
        c.textColor = new Color(0x722d34);

        c.size = new XY(720, 384);
        c.imgFileBg = "/img/render0/bg.png";

        c.characterPosition = new XY(720 / 2, 384 / 2 + 33);
        c.imgFileNameLabel = "/img/render0/nameLabel.png";
        c.nameLabelPositionOffset = new XY(0, 1);

        c.imgFileLabel = "/img/render0/label.png";
        c.labelsStartPosition = new XY(10, 10);
        c.labelsTextOffset = new XY(37, -2);
        c.labelOffset = 1;

        c.panelSize = new XY(267, 339);
        c.imgFilePanel = "/img/render0/panel.png";
        c.panelPosition = new XY(720 - 267 - 10, (384 - 339) / 2);
        c.panelLinePosition = new XY(58, 38);

        return c;
    }

    static RankRenderConfig createCapoo() {
        var c = new RankRenderConfig();

        c.fontName = c.initFontName();
        c.textColor = new Color(0x494d4e);

        c.size = new XY(740, 720);
        c.imgFileBg = "/img/render1/bg.png";

        c.characterPosition = new XY(740 / 2, 500);
        c.imgFileNameLabel = "/img/render1/name.png";
        c.nameLabelPositionOffset = new XY(3, 1);

        c.imgFileLabel = "/img/render1/label.png";
        c.labelsStartPosition = new XY(35, 340);
        c.labelsTextOffset = new XY(47, 0);
        c.labelOffset = -13;

        c.panelSize = new XY(267, 339);
        c.imgFilePanel = null;
        c.panelPosition = new XY(740 - 267 - 15, 330);
        c.panelLinePosition = new XY(58, 38);

        return c;
    }

    @AllArgsConstructor
    public static class XY {
        public final int x;
        public final int y;

        public static XY add(XY v1, XY v2) {
            return new XY(v1.x + v2.x, v1.y + v2.y);
        }

        public static XY add(XY v, int x, int y) {
            return new XY(v.x + x, v.y + y);
        }
    }
}
