package at.fhtw.bif3.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    /**
     * Convenience method to read default properties file if no other file name was specified
     *
     * @return Properties
     */
    public static Properties getProperties() {
        return getProperties(DEFAULT_PROPERTIES_FILE);
    }

    /**
     * Method to read specified properties file
     *
     * @param fileName is the file that will be looked for
     * @return Properties
     */
    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = PropertiesReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println(fileName + " not found on classpath");
                throw new FileNotFoundException();
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
