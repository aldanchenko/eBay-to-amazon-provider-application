package com.ebaytoamazonprovider.util;

import com.ebaytoamazonprovider.beans.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Load configuration properties {@link Configuration} from properties file.
 */
public class ConfigurationLoader {

    /**
     * Default logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);

    /**
     * Method for quick access.
     *
     * @return ConfigurationLoader
     */
    public static ConfigurationLoader newInstance() {
        return new ConfigurationLoader();
    }

    /**
     * Load application configuration from specified file.
     *
     * @param fileName - path to file
     *
     * @throws IOException -
     *
     * @return Configuration
     */
    public Configuration load(String fileName) throws IOException {
        Properties properties = loadPropertiesFile(fileName);

        return new Configuration(properties);
    }

    /**
     * Save configuration properties to file.
     *
     * @param fileName      - path to file
     * @param configuration - configuration object
     */
    public void save(String fileName, Configuration configuration) throws IOException {
        this.savePropertiesToFile(fileName, configuration.getConfigurationProperties());
    }

    /**
     * Common method for load property files.
     *
     * @throws IOException - exception
     *
     * @return Properties
     */
    private Properties loadPropertiesFile(String filePath) throws IOException {
        Properties properties = new Properties();

        File loginPropertiesFile = new File(filePath);

        if (loginPropertiesFile.exists()) {
            InputStream inputStream = null;

            try {
                inputStream = new FileInputStream(loginPropertiesFile);

                properties.load(inputStream);
            } catch (IOException ioException) {
                throw new IOException(ioException);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException closeException) {
                        logger.error("", closeException);
                    }
                }
            }
        } else {
            throw new FileNotFoundException("Can't find " + filePath);
        }

        return properties;
    }

    /**
     * Save properties file.
     *
     * @param properties - source properties object to save
     */
    private void savePropertiesToFile(String filePath, Properties properties) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(filePath);

            properties.store(outputStream, null);
        } catch (IOException ioException) {
            throw new IOException(ioException);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }
}
