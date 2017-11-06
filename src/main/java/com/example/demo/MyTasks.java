package com.example.demo;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Component
public class MyTasks {

    // https://earnest-sandbox-184720.appspot.com/ <-- Cloud URL
    RestTemplate restTemplate = new RestTemplate();
    private int id = 1;

    @Scheduled(cron = "1/2 * * * * *")
    public void addVehicle() {
        Random rand = new Random();
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('A', 'z').build();
        int randomYear = rand.nextInt((2016 - 1986) + 1) + 1986;
        double randomPrice = (double) rand.nextInt((45000 - 15000) + 1) + 15000;

        String addUrl = "https://earnest-sandbox-184720.appspot.com/addVehicle";
        Vehicle vehicle = new Vehicle(id++, generator.generate(35), randomYear, randomPrice);
        restTemplate.postForObject(addUrl, vehicle, Vehicle.class);
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void deleteVehicle() {
        Random rand = new Random();
        int randomId = rand.nextInt(101 + 1);
        String deleteUrl = "https://earnest-sandbox-184720.appspot.com/deleteVehicle/" + randomId;
        restTemplate.delete(deleteUrl);
    }

    @Scheduled(cron = "* */1 * * * *")
    public void updateVehicle() {
        Random rand = new Random();
        int randomId = rand.nextInt(101 + 1);
        Vehicle vehicle = new Vehicle(randomId, "Toyota Corolla", 2009, 10000.0);

        String updateUrl = "https://earnest-sandbox-184720.appspot.com/updateVehicle";
        String getUrl = "https://earnest-sandbox-184720.appspot.com/getVehicle/" + randomId;
        restTemplate.put(updateUrl, vehicle);

        Vehicle getVehicle = restTemplate.getForObject(getUrl, Vehicle.class);
        System.out.println(getVehicle.toString());
    }

    @Scheduled(cron = "0 0 * * * *")
    public void latestVehiclesReport() {
        String getLatestUrl = "https://earnest-sandbox-184720.appspot.com/getLatestVehicles";
        List<Vehicle> recentVehicles = restTemplate.getForObject(getLatestUrl, List.class);
        System.out.println(recentVehicles.toString());
    }

}
