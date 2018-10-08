package com.ebaytoamazonprovider.beans;

import java.util.Properties;

/**
 * Contains application configuration properties.
 */
public class Configuration {

    /**
     * Contains application configuration properties.
     */
    private Properties configurationProperties;

    /**
     * Default constructor.
     *
     * @param properties - configuration properties
     */
    public Configuration(Properties properties) {
        this.configurationProperties = properties;
    }

    public Properties getConfigurationProperties() {
        return configurationProperties;
    }

    public String getEBayToken() {
        return this.configurationProperties.getProperty("ebay.token");
    }

    public String getEBayUrl() {
        return this.configurationProperties.getProperty("ebay.url");
    }

    public String getEBayAppId() {
        return this.configurationProperties.getProperty("ebay.app.id");
    }

    public String getEBayDevId() {
        return this.configurationProperties.getProperty("ebay.dev.id");
    }

    public String getEBayCertId() {
        return this.configurationProperties.getProperty("ebay.cert.id");
    }

    public String getAmazonAccessKeyId() {
        return this.configurationProperties.getProperty("amazon.access.key.id");
    }

    public String getAmazonSecretAccessKey() {
        return this.configurationProperties.getProperty("amazon.secret.access.key");
    }

    public String getAmazonAppName() {
        return this.configurationProperties.getProperty("amazon.app.name");
    }

    public String getAmazonAppVersion() {
        return this.configurationProperties.getProperty("amazon.app.version");
    }

    public String getAmazonServiceUrl() {
        return this.configurationProperties.getProperty("amazon.service.url");
    }

    public String getAmazonMerchantId() {
        return this.configurationProperties.getProperty("amazon.merchant.id");
    }

    public String getAmazonSellerDevAuthToken() {
        return this.configurationProperties.getProperty("amazon.seller.dev.auth.token");
    }

    public String getAmazonMarketplaceId() {
        return this.configurationProperties.getProperty("amazon.marketplace.id");
    }
}
