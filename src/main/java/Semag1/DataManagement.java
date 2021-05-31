package Semag1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * JSON File saving using Jackson library
 * Must convert to Maven Project in order to use
 * Every class that uses this MUST contain
     * DEFAULT CONSTRUCTORS
     * GETTER AND SETTER METHODS FOR ALL INSTANCE VARIABLES that wants to be saved
 * HOW TO USE?
     * Once the user logged in, it is recommended to call the loadData() method in the specific classes(chaining down the implementations ~ Windows -> Project -> Issue)
     * Once done, call the saveData() in the specific classes to load the data into the JSON file.
 */

public class DataManagement implements Serializable {

    /*
     * Below are all methods to save data into JSON File.
     * All receives parameters of object and will straight away write into their separate JSON fileNames
     * Have to create a method in
     */



    public void writeData(Project obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("Project.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(labelCounter obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("LabelCounter.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(User obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("User.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(Window obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(Issue obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("Issue.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(Comment obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("Comment.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(PeopleADT obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("bla.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Below are all methods to read data into JSON File.
     * All will read the individual JSON file and return the object.
     * Have to refer to the object itself using 'this' keyword.
     */


    public Project readProjectData() {
        Project returnObj = new Project();
        try {
            returnObj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readerFor(Project.class).readValue(new File("Project.json"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public labelCounter readLabelCounterData() {
        labelCounter returnObj = new labelCounter();
        try {
            returnObj = new ObjectMapper().readerFor(labelCounter.class).readValue(new File("LabelCounter.json"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public User readUserData() {
        User returnObj = new User();
        try {
            returnObj = new ObjectMapper().readerFor(User.class).readValue(new File("User.json"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public Window readWindowData() {
        Window returnObj = new Window();
        try {
            returnObj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readerFor(Window.class).readValue(new File("data.json"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public Issue readIssueData() {
        Issue returnObj = new Issue();
        try {
            returnObj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readerFor(Issue.class).readValue(new File("Issue.json"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public Comment readCommentData() {
        Comment returnObj = new Comment();
        try {
            returnObj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readerFor(Comment.class).readValue(new File("Comment.json"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public PeopleADT readPeopleADTData() {
        PeopleADT returnObj = new PeopleADT();
        try {
            returnObj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readerFor(Comment.class).readValue(new File("PeopleADT.json"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }
}

