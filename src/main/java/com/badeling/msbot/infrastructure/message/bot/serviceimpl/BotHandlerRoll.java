package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerRoll implements BotHandler {
    private static  Pattern pattern = Pattern.compile("[R|r][O|o][L|l][L|l]");
    private static Pattern numbersPattern = Pattern.compile("(\\d+)-(\\d+)");
    private static Pattern numberPattern = Pattern.compile("(\\d+)");

    private Random random = new Random();
    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help(){
        return "│   ├── ROLL[[最小-]最大]\r\n" +
                "│   │   └── 大小写不敏感\r\n";
    }

    @Override
    public int getOrder(){
        return 8;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher useless) {
        String msg = request.getRawMessage();
        String numStr1 = "0";
        String numStr2 = "100";
        Matcher m = numbersPattern.matcher(msg);
        if(m.find()){
            numStr1 = m.group(1);
            numStr2 = m.group(2);
        }else{
            m = numberPattern.matcher(msg);
            if(m.find()){
                numStr2 = m.group(1);
            }
        }

        StringBuilder sb = new StringBuilder();
        try {
            int num1 = Integer.parseInt(numStr1);
            int num2 = Integer.parseInt(numStr2);

            int bound = num2 - num1;
            if(bound == 0){
                sb.append(numStr1);
            }else {
                int min;
                if (bound < 0) {
                    bound = - bound;
                    min = num2;
                } else {
                    min = num1;
                }
                sb.append("随机[").append(min)
                        .append(',').append(bound + min).append(")\r\n");
                int r = random.nextInt(bound) + min;
                sb.append(r);
            }
        }catch (NumberFormatException e){
            sb.append("啥玩意").append(numStr1).append('-').append(numStr2);
        }

        GroupMessageResult replyMsg = new GroupMessageResult();
        replyMsg.setReply(sb.toString());
        replyMsg.setAt_sender(true);
        return replyMsg;
    }
}
