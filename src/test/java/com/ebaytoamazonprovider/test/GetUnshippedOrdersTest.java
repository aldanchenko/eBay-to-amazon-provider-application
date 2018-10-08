package com.ebaytoamazonprovider.test;

import com.ebay.sdk.*;
import com.ebay.sdk.call.GetOrdersCall;
import com.ebay.sdk.util.eBayUtil;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.OrderIDArrayType;
import com.ebay.soap.eBLBaseComponents.OrderType;
import com.ebaytoamazonprovider.Constants;
import com.ebaytoamazonprovider.beans.Configuration;
import com.ebaytoamazonprovider.util.ConfigurationLoader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GetUnshippedOrdersTest {

    @Test
    public void testGetUnshippedOrders() throws Exception {
        ApiContext apiContext = new ApiContext();

        // Enable Call-Retry.
        CallRetry callRetry = new CallRetry();
        callRetry.setMaximumRetries(3);
        callRetry.setDelayTime(1000); // Wait for one second between each
        // retry-call.
        String[] apiErrorCodes = new String[] {
                "10007", // Internal error to the application.
                "931", // Validation of the authentication token in API request failed.
                "521", // Test of Call-Retry: "The specified time window is invalid".
                "124" // Test of Call-Retry: "Developer name invalid."
        };

        callRetry.setTriggerApiErrorCodes(apiErrorCodes);

        // Set trigger exceptions for CallRetry.
        // Build a dummy SdkSoapException so that we can get its Class.
        java.lang.Class[] exceptionEbaySdkClasses = new java.lang.Class[] { com.ebay.sdk.SdkSoapException.class };

        callRetry.setTriggerExceptions(exceptionEbaySdkClasses);

        apiContext.setCallRetry(callRetry);

        apiContext.setTimeout(180000);

        Configuration configuration = ConfigurationLoader.newInstance().load(Constants.APPLICATION_CONFIGURATION_FILE_NAME);

        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setApplication(configuration.getEBayAppId());
        apiAccount.setCertificate(configuration.getEBayCertId());
        apiAccount.setDeveloper(configuration.getEBayDevId());

        ApiCredential apiCredential = new ApiCredential();
        apiCredential.seteBayToken(configuration.getEBayToken());
        apiCredential.setApiAccount(apiAccount);

        apiContext.setApiCredential(apiCredential);

        apiContext.setApiServerUrl(configuration.getEBayUrl());

        // Add listener for token renewal event.
        apiContext.getApiCredential().addTokenEventListener(new DemoTokenEventListener());

        ApiLogging logging = new ApiLogging();

        logging.setLogSOAPMessages(true);
        logging.setLogExceptions(true);

        apiContext.setApiLogging(logging);

        String orderIdsStr = "";

        DetailLevelCodeType[] detailLevels = new DetailLevelCodeType[]{DetailLevelCodeType.RETURN_ALL};

        GetOrdersCall getOrdersCall = new GetOrdersCall(apiContext);
        getOrdersCall.setDetailLevel(detailLevels);

        StringTokenizer stringTokenizer = new StringTokenizer(orderIdsStr, ",");

        List<String> orderIdsList = new ArrayList<>();

        while (stringTokenizer.hasMoreTokens()) {
            orderIdsList.add(stringTokenizer.nextToken());
        }

        int size = orderIdsList.size();

        String[] orderIds = new String[size];

        for (int i = 0; i < size; i++) {
            orderIds[i] = orderIdsList.get(i).trim();
        }

        OrderIDArrayType orderIDArrayType = new OrderIDArrayType();

        orderIDArrayType.setOrderID(orderIds);

        getOrdersCall.setOrderIDArray(orderIDArrayType);

        OrderType[] orderTypes = getOrdersCall.getOrders();

        for (OrderType order : orderTypes) {
            if (order.getShippedTime() == null) {
                String orderID = order.getOrderID();
                String numberOfTransaction = Integer.toString(order.getTransactionArray().getTransaction().length);
                String transactionPrice = Double.toString(order.getTotal().getValue());

                String paidTime = "";

                if (order.getPaidTime() != null) {
                    paidTime = eBayUtil.toAPITimeString(order.getPaidTime().getTime());
                }

                String buyerUserID = order.getBuyerUserID();

                System.out.println(String.format("Order information: id {%s}, number of transaction {%s}, "
                        + "transaction price {%s}, paid time {%s}, buyer {%s}",
                        orderID, numberOfTransaction, transactionPrice, paidTime, buyerUserID));
            }
        }
    }

    public static class DemoTokenEventListener implements TokenEventListener {

        public void renewToken(String newToken) {
            System.out.println("eBay Token has been renewed successfully!");
        }

        public void warnHardExpiration(java.util.Date expirationDate) {
            System.out.println("Received token hard-expiration-warning: " + eBayUtil.toAPITimeString(expirationDate));
        }
    }
}
