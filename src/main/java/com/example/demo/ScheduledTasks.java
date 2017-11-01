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

//    @Scheduled(cron = "1/2 * * * * *")
//    public void getGreeting() {
//        String getURL = "http://localhost:8080/greeting";
//        Greeting g = restTemplate.getForObject(getURL, Greeting.class);
//        System.out.println(g.getContent());
//    }
//
//    @Scheduled(cron = "*/2 * * * * *")
//    public void putGreeting() {
//        String url = "http://localhost:8080/updateGreeting";
//        String getURL = "http://localhost:8080/greeting";
//        Greeting g = restTemplate.getForObject(getURL, Greeting.class);
//        if (g.getContent().equals("Hello World")) {
//            restTemplate.put(url, "Goodbye World");
//        } else {
//            restTemplate.put(url, "Hello World");
//        }
//    }
}
