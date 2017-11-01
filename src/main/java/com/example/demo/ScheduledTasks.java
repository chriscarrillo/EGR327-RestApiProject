package com.example.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    RestTemplate restTemplate = new RestTemplate();

    private void doUpdate(String newMessage) {
        String putUrl = "http://localhost:8080/updateGreeting";
        restTemplate.put(putUrl, newMessage);
    }

    private Greeting getGreeting() {
        String getUrl = "http://localhost:8080/greeting";
        return restTemplate.getForObject(getUrl, Greeting.class);
    }

    @Scheduled(cron="*/1 * * * * *")
    public void task() {
        String hello = "Hello World";
        String bye = "Bye World";

        System.out.println(getGreeting().getContent());

        if (getGreeting().getContent().equals(hello)) {
            doUpdate(bye);
        } else {
            doUpdate(hello);
        }
    }
}
