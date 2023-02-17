package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.Data;

@Data
public class GetImageData {
    int size;
    String filename;
    String url;
}
