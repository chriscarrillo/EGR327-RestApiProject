package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class ProjectController {

    private int count = 1;

    @RequestMapping(value = "/createGreeting", method = RequestMethod.POST)
    public Greeting createGreeting(@RequestBody String name) throws IOException {
        Greeting newGreeting = new Greeting(count++, name);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("./message.txt"), newGreeting);
        return newGreeting;
    }

    @RequestMapping(value="/updateGreeting", method=RequestMethod.PUT)
    public Greeting updateGreeting(@RequestBody String newMessage) throws IOException {
        // ObjectMapper provides functionality for reading and writing JSON
        ObjectMapper mapper = new ObjectMapper();

        String message = FileUtils.readFileToString(new File("./message.txt"), StandardCharsets.UTF_8);

        // Deserialize JSON to greeting object
        Greeting greeting = mapper.readValue(message, Greeting.class);

        // Update message
        greeting.setContent(newMessage);

        // Serialize greeting object to JSON
        mapper.writeValue(new File("./message.txt"), greeting);

        return greeting;
    }

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public Greeting greeting() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("./message.txt"), Greeting.class);
    }

    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileWriter output = new FileWriter("./inventory.txt", true);
        mapper.writeValue(output, newVehicle);

        FileUtils.writeStringToFile(new File("./inventory.txt"),
                System.lineSeparator(),
                StandardCharsets.UTF_8,
                true);
        return newVehicle;
    }

    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileReader input = new FileReader("./inventory.txt");
        BufferedReader reader = new BufferedReader(input);
        String line;

        while ((line = reader.readLine()) != null) {
            Vehicle vehicle = mapper.readValue(line, Vehicle.class);

            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        input.close();
        return null;
    }

    @RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inventoryFile = new File("./inventory.txt");

        List<String> inventoryList = FileUtils.readLines(inventoryFile, StandardCharsets.UTF_8);

        for (int i = 0; i < inventoryList.size(); i++) {
            Vehicle vehicle = mapper.readValue(inventoryList.get(i), Vehicle.class);
            if (vehicle.getId() == newVehicle.getId()) {
                inventoryList.set(i, "{\"id\":" + newVehicle.getId() + ",\"makeModel\":\"" + newVehicle.getMakeModel()
                        + "\",\"year\":" + newVehicle.getYear() + ",\"retailPrice\":" + newVehicle.getRetailPrice() + "}");
            }
        }
        FileUtils.writeLines(inventoryFile, inventoryList);
        return newVehicle;
    }

    @RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {
        if (getVehicle(id) != null) {
            ObjectMapper mapper = new ObjectMapper();
            File inventoryFile = new File("./inventory.txt");

            List<String> inventoryList = FileUtils.readLines(inventoryFile, StandardCharsets.UTF_8);

            for (int i = 0; i < inventoryList.size(); i++) {
                Vehicle vehicle = mapper.readValue(inventoryList.get(i), Vehicle.class);

                if (vehicle.getId() == id) {
                    inventoryList.remove(i);
                }
            }
            FileUtils.writeLines(inventoryFile, inventoryList);
            return new ResponseEntity("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity("Not found", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getLatestVehicles", method = RequestMethod.GET)
    public List<Vehicle> getLatestVehicle() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inventoryFile = new File("./inventory.txt");

        List<String> list = FileUtils.readLines(inventoryFile, StandardCharsets.UTF_8);
        Collections.reverse(list);
        List<Vehicle> returnedList = new ArrayList<>();

        for (int i = 9; i >= 0; i--) {
            Vehicle vehicle = mapper.readValue(list.get(i), Vehicle.class);
            returnedList.add(vehicle);
        }
        return returnedList;
    }

}
