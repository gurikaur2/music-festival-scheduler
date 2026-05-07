package com.gurnoor.music_event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MusicFestivalSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicFestivalSchedulerApplication.class, args);
    }
}