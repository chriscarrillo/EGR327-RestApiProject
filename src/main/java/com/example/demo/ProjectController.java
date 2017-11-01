package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

}
