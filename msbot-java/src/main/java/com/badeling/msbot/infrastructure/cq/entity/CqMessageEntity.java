package com.badeling.msbot.infrastructure.cq.entity;


public interface CqMessageEntity {

    /** at全部*/
    CqMessageEntity atAll();

    /** at某人*/
    CqMessageEntity atQQ(Long qq);

    /** 本地图片 相对qq路径 */
    CqMessageEntity image(String filePath);

    /** 本地图片 绝对路径 */
    CqMessageEntity imageAbsolute(String filePath);

    /** 网络图片 */
    CqMessageEntity imageUrl(String fileUrl);

    /** 文本 */
    CqMessageEntity text(String text);

    /** 换行 */
    CqMessageEntity changeLine();

    boolean isAutoEscape();

    String getMessage();
}
