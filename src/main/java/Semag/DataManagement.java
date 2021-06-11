package Semag;

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

public final class DataManagement {

    /*
        * Below these two methods are used to read and write data into and out of the Window.json file.
     */

    /**
     * Method to use Jackson's ObjectMapper to map POJO to Json objects with the destination file name of "Window.json"
     * @param obj The window class instance to save
     */
    public static void writeData(Window obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("Window.json"), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method using Jackson's ObjectMapper to map Json objects back to POJO with the source file name of "Window.json"
     * @return A Window class POJO that has all the data in "Window.json" loaded into it
     */
    public static Window readWindowData() {
        Window returnObj = new Window();
        try {
            returnObj = new ObjectMapper().readerFor(Window.class).readValue(new File("Window.json"));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }
}

