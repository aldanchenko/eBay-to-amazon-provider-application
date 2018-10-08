package com.ebaytoamazonprovider;

import com.ebaytoamazonprovider.ui.ProviderFrame;

import javax.swing.*;

/**
 * Main entry point for application.
 */
public class EbayToAmazonProviderApplication {

    /**
     * Main method.
     *
     * @param parameters - application console input parameters
     */
    public static void main(String[] parameters) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        ProviderFrame providerFrame = new ProviderFrame();

        providerFrame.setVisible(Boolean.TRUE);

        providerFrame.initialize();
    }
}
