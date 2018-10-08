package com.ebaytoamazonprovider.service;

import com.ebay.sdk.*;
import com.ebay.soap.eBLBaseComponents.*;
import com.ebaytoamazonprovider.beans.Configuration;

import java.util.*;

/**
 * eBay service calls.
 */
public class EbayService {

    private Configuration configuration;

    public EbayService(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * FIXME: simplify this method.
     *
     * @return
     * @throws Exception
     */
    public List<OrderType> getUnshippedOrders() throws Exception {
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

        List<OrderType> orders = new ArrayList<>();

        if (Objects.nonNull(orderArray)) {
            OrderType[] orderTypes = orderArray.getOrder();

            if (Objects.nonNull(orderTypes) && orderTypes.length > 0) {
                for (OrderType order : orderTypes) {
                    if (order.getShippedTime() == null) {
                        orders.add(order);
                    }
                }
            }
        }

        return orders;
    }
}
