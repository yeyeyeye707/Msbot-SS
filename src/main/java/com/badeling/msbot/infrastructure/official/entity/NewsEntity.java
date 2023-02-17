package com.badeling.msbot.infrastructure.official.entity;

import com.badeling.msbot.infrastructure.dao.entity.OfficialNews;
import lombok.Data;

@Data
public class NewsEntity {
    String title;

    String imgUrl;

    String url;

    String imgPath;

    public OfficialNews createDAO(){
        OfficialNews n = new OfficialNews();
        n.setTitle(title);
        n.setUrl(url);
        n.setImg_path(imgPath);
        return n;
    }

}
