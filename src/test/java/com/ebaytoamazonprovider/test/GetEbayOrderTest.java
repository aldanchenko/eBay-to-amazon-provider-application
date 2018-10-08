package com.ebaytoamazonprovider.test;

import com.ebay.sdk.*;
import com.ebay.sdk.util.eBayUtil;
import com.ebay.soap.eBLBaseComponents.*;
import com.ebaytoamazonprovider.Constants;
import com.ebaytoamazonprovider.beans.Configuration;
import com.ebaytoamazonprovider.util.ConfigurationLoader;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

/**
 * Main eBayToAmazonProvider application class.
 */
public class GetEbayOrderTest {

    @Test
    public void testGetEbayOrder() throws IOException, SdkException {
        Configuration configuration = ConfigurationLoader.newInstance().load(Constants.APPLICATION_CONFIGURATION_FILE_NAME);

        ApiAccount apiAccount = new ApiAccount();

        apiAccount.setDeveloper(configuration.getEBayDevId());
        apiAccount.setApplication(configuration.getEBayAppId());
        apiAccount.setCertificate(configuration.getEBayCertId());

        ApiCredential apiCredential = new ApiCredential();
        apiCredential.setApiAccount(apiAccount);
        apiCredential.seteBayToken(configuration.getEBayToken());

        ApiContext apiContext = new ApiContext();
        apiContext.setApiCredential(apiCredential);

        apiContext.setApiServerUrl(configuration.getEBayUrl());

        ApiLogging logging = new ApiLogging();

        logging.setLogSOAPMessages(true);
        logging.setLogExceptions(true);

        apiContext.setApiLogging(logging);

        ApiCall apiCall = new ApiCall(apiContext);

        GetOrdersRequestType getOrdersRequest = new GetOrdersRequestType();

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_MONTH, -30);

        getOrdersRequest.setCreateTimeFrom(startCalendar);
        getOrdersRequest.setCreateTimeTo(Calendar.getInstance());

        getOrdersRequest.setOrderRole(TradingRoleCodeType.SELLER);
        getOrdersRequest.setOrderStatus(OrderStatusCodeType.ALL);

        GetOrdersResponseType getOrdersResponse = (GetOrdersResponseType) apiCall.executeByApiName("GetOrders", getOrdersRequest);

        OrderArrayType orderArray = getOrdersResponse.getOrderArray();

        if (Objects.nonNull(orderArray)) {
            OrderType[] orders = orderArray.getOrder();

            if (Objects.nonNull(orders) && orders.length > 0) {
                for (OrderType order : orders) {
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
            } else {
                System.out.println("Empty OrderType[].");
            }
        } else {
            System.out.println("Empty OrderArrayType.");
        }
    }
}
