package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerRoll implements BotHandler {
    private static final Pattern pattern = Pattern.compile("[R|r][O|o][L|l][L|l]");
    private static final Pattern numbersPattern = Pattern.compile("(\\d+)-(\\d+)");
    private static final Pattern numberPattern = Pattern.compile("(\\d+)");

    private final Random random = new Random();

    private final CqMessageBuildService cqMessageBuildService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── ROLL[[最小-]最大]\r\n" +
                "│   │   └── 大小写不敏感\r\n";
    }

    @Override
    public int getOrder() {
        return 8;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher useless) {
        String msg = request.getRawMessage();
        String numStr1 = "0";
        String numStr2 = "100";
        Matcher m = numbersPattern.matcher(msg);
        if (m.find()) {
            numStr1 = m.group(1);
            numStr2 = m.group(2);
        } else {
            m = numberPattern.matcher(msg);
            if (m.find()) {
                numStr2 = m.group(1);
            }
        }

        var cq = cqMessageBuildService.create();
        try {
            int num1 = Integer.parseInt(numStr1);
            int num2 = Integer.parseInt(numStr2);

            int bound = num2 - num1;
            if (bound == 0) {
                cq.text(numStr1);
            } else {
                int min;
                if (bound < 0) {
                    bound = -bound;
                    min = num2;
                } else {
                    min = num1;
                }
                cq.text("随机[" + min + ',' + (bound + min) + ")\r\n");
                int r = random.nextInt(bound) + min;
                cq.text(String.valueOf(r));
            }
        } catch (NumberFormatException e) {
            cq.text("啥玩意")
                    .text(numStr1)
                    .text("-")
                    .text(numStr2);
        }

        return Tuple2.of(cq, true);
    }
}
