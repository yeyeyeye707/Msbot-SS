package com.badeling.msbot.infrastructure.maplegg.repository;

import com.badeling.msbot.infrastructure.maplegg.entity.CharacterData;
import jakarta.annotation.Nullable;

public interface RankInfoImgRepository  extends org.springframework.core.Ordered {

    Integer getId();
    @Nullable
    String saveImg(CharacterData character);
}
