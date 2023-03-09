package com.badeling.msbot.infrastructure.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MsbotConst {
	
	//go cqhttp本体
	public static final String miraiUrl = "/home/ubuntu/msbot/cqhttp";
	//go-cqhttp图片目录
	public static final String imageUrl = miraiUrl+"/data/images/";
	//机器人昵称
	public static final String botName = "烧烧";
	//自己的昵称 
	public static final String masterName = "BadeLing";
	//机器人qq号
	public static final String botId = "2908840550";
	//自己的qq号 超级管理员
	public static final long masterId = 724507540L;
	//管理员qq号
	public static final List<Long> managerId = List.of(550578418L);// java 9.
	// public static final List<Long> managerId = Arrays.asList(550578418L, 709480621L);
	//拉黑的qq号
	public static final long[] blackList = {};
	
	//第三方机器人 也就是上面登录的账号 允许发言的频道 QQ频道发一句话 然后后台查看频道id
	//"channel_id":"1609899"
	public static final String[] botChannel = {};
	
	//茉莉机器人key
	public static final String moliKey = "";
	//茉莉机器人Secret
	public static final String moliSecret = "";
	
	//茉莉机器人key 可以和上面填一样 一个是官方频道机器人用的 一个是上面登录的第三方机器人用的
	public static final String moliKey2 = "";
	//茉莉机器人Secret
	public static final String moliSecret2 = "";
	
	//百度云识图Key 可不填 只影响 识图和起源39自动识别功能 原本的气象功能已废弃
	//百度云识图 普通模式每天免费5w次 高精度模式每天免费500次
	public static final String baiduKey = "";
	//百度云识图Secret
	public static final String baiduSecret = "";
	//百度翻译api 跟上面一样 每天有免费的次数 用不完
	public static final String appid="";
	//百度翻译key
	public static final String securityKey="";

	
	//腾讯云key 这个可以不填 是为官方机器人用的 这个以后有条件再教
	public static final String tencentSecretId = "";
	//腾讯云Secret
	public static final String tencentSecretKey = "";
	//存储桶id
	public static final String bucketName = "image";
	//存储桶地址
	public static final String bucketRegion = "ap-chengdu";
	
	//QQ频道官方机器人
	public static final String channelBotId = "123456";
	//QQ频道官方机器人 昵称
	public static final String channelBotName = "拉拉";
	
}
