package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerIgnore implements BotHandler {
    private static final Pattern pattern = Pattern.compile("^( *)无视( *)(\\d+)(.*)");
    private static final Pattern MODIFY_PATTERN = Pattern.compile("(\\+|\\-)(\\d+)");

    private final CqMessageBuildService cqMessageBuildService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 无视 数字+/-数字[+/-数字...]\r\n" +
                "│   │   └── 计算方式是作为不同项计算\r\n";
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) {
        StringBuilder sb = new StringBuilder();
        try {
            double ign = Double.parseDouble(m.group(3)) / 100;
            sb.append("无视").append(m.group(3));

            String modifyStr = m.group(4);
            System.out.println(modifyStr);
            Matcher _m = MODIFY_PATTERN.matcher(modifyStr);
            while (_m.find()) {
                double modify = Double.parseDouble(_m.group(2)) / 100;
                if (modify > 0 && modify < 1) {

                    if ("+".equals(_m.group(1))) {
                        ign = 1 - (1 - ign) * (1 - modify);
                    } else {
                        ign = 1 - (1 - ign) / (1 - modify);
                    }
                    sb.append(_m.group());
                }

                modifyStr = modifyStr.substring(_m.end());
                _m = MODIFY_PATTERN.matcher(modifyStr);
            }
            sb.append('=').append(Math.floor(ign * 100));

        } catch (NumberFormatException ignored) {
//            System.out.println();
        }

        return Tuple2.of(cqMessageBuildService.create().text(sb.toString()), false);
    }
}
