package com.ebaytoamazonprovider.test;

import com.ebaytoamazonprovider.Constants;
import com.ebaytoamazonprovider.beans.Configuration;
import com.ebaytoamazonprovider.service.AmazonService;
import com.ebaytoamazonprovider.util.ConfigurationLoader;
import org.junit.Test;

import java.io.IOException;

/**
 * Test cases for {@link com.ebaytoamazonprovider.service.AmazonService}.
 */
public class AmazonServiceTest {

    @Test
    public void testCreateFulfillmentOrder() throws IOException {
        Configuration configuration = ConfigurationLoader.newInstance().load(Constants.APPLICATION_CONFIGURATION_FILE_NAME);

        AmazonService amazonService = new AmazonService(configuration);

        amazonService.createFulfillmentOrder("Test Amazon SKU");
    }
}
