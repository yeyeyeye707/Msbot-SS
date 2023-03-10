package com.badeling.msbot.infrastructure.util;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class ImgUtil {
    @Autowired
    private ConstRepository constRepository;

    public String saveTempImage(String raw_message) throws Exception {
        String imageName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        download(raw_message, constRepository.getImageUrl(), imageName);
        return imageName;
    }

    public void download(String urlString, String savePath, String imageName) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        // 新的图片文件名 = 编号 +"."图片扩展名
        String newFileName = imageName;
        OutputStream os = new FileOutputStream(sf.getPath() + "/" + newFileName);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();

    }
}
