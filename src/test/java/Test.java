import com.badeling.msbot.infrastructure.maplegg.entity.RankResponse;
import com.badeling.msbot.infrastructure.maplegg.exception.HttpAccessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Test {
    private static  Pattern pattern = Pattern.compile("( {0,3})删除问题( *)(\\d+)");
    public static void main(String[] args){
        try {
            String a = "https://maplestory.nexon.net/news";
            URL url = new URL(a);
            Document doc = new SAXReader().read(url);

            List<Node> list = doc.selectNodes("//ul[@class='news-container']");
                  for (Node node : list) {
                           System.out.println(node);
                        }
        }catch (Exception ex){

        }
    }


    private static final String MSG_IMG_TYPE  = "[CQ:image";
    private static final Pattern MSG_IMG_URL_PATTERN = Pattern.compile("url=(\\S+?)(\\]|\\,)");
    private  static final Pattern MSG_IMG_FILE_PATTERN = Pattern.compile("file=(\\S+?)(\\]|\\,)");

    public static String saveMessageToLocal(String msg) {
        System.out.println(msg);

        StringBuilder sb = new StringBuilder();
        String imgData;
        while (msg.contains(MSG_IMG_TYPE)) {
            int start = msg.indexOf(MSG_IMG_TYPE);
            int end = msg.indexOf(']') + 1;
            sb.append(msg.substring(0, start));
            imgData = msg.substring(start, end);
//            System.out.println(imgData);

            //下载url.
            String url = null;
            Matcher m = MSG_IMG_URL_PATTERN.matcher(imgData);
            if(m.find()){
                url = m.group(1);
            }else{
                System.out.println("no url. read file");
                m = MSG_IMG_FILE_PATTERN.matcher(imgData);
                if(m.find()){
                    String fileName = m.group(1);
                    System.out.println(fileName);
                }
            }

            if(url != null) {
                System.out.println(url);
                try {
                    String imageName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
//                download(url, MsbotConst.imageUrl + "save/", imageName);
                    sb.append("[CQ:image,file=save/");
                    sb.append(imageName);
                    sb.append("]");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            msg = msg.substring(end);
//            System.out.println(msg);
        }
        sb.append(msg);

        return sb.toString();
    }
}
