package com.badeling.msbot.interfaces.facade;

import com.badeling.msbot.infrastructure.official.service.OfficialNewScheduledComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cron")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CronController {

    private final OfficialNewScheduledComponent officialNewScheduledComponent;

    @GetMapping("news")
    public String news(){
        officialNewScheduledComponent.check();

        return "start";
    }
}
