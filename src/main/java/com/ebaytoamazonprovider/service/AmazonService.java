package com.ebaytoamazonprovider.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.amazon.mws.finances._2015_05_01.MWSFinancesServiceAsyncClient;
import com.amazon.mws.finances._2015_05_01.MWSFinancesServiceClient;
import com.amazon.mws.finances._2015_05_01.MWSFinancesServiceConfig;
import com.amazon.mws.finances._2015_05_01.MWSFinancesServiceException;
import com.amazon.mws.finances._2015_05_01.model.FinancialEventGroup;
import com.amazon.mws.finances._2015_05_01.model.ListFinancialEventGroupsRequest;
import com.amazon.mws.finances._2015_05_01.model.ListFinancialEventGroupsResponse;
import com.amazon.mws.finances._2015_05_01.model.ListFinancialEventGroupsResult;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.*;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.*;
import com.ebaytoamazonprovider.beans.Configuration;

/**
 * Amazon service.
 */
public class AmazonService {

    private float dollars;

    private Configuration configuration;

    public AmazonService(Configuration configuration) {
        this.configuration = configuration;
    }

    public void invokeGetServiceStatus(FBAInventoryServiceMWS service, GetServiceStatusRequest request) {
        try {
            GetServiceStatusResponse response = service.getServiceStatus(request);

            System.out.println("GetServiceStatus Action Response");
            System.out.println("=============================================================================");
            System.out.println();
            System.out.println("    GetServiceStatusResponse");
            System.out.println();

            if (response.isSetGetServiceStatusResult()) {

                System.out.println("        GetServiceStatusResult");
                System.out.println();

                GetServiceStatusResult getServiceStatusResult = response.getGetServiceStatusResult();

                if (getServiceStatusResult.isSetStatus()) {
                    System.out.println("            ServiceStatusInfo");
                    System.out.println();
                    System.out.println("                " + getServiceStatusResult.getStatus());
                    System.out.println();
                }
            }

            if (response.isSetResponseMetadata()) {
                System.out.println("        ResponseMetadata");
                System.out.println();

                ResponseMetadata responseMetadata = response.getResponseMetadata();

                if (responseMetadata.isSetRequestId()) {
                    System.out.println("            RequestId");
                    System.out.println();
                    System.out.println("                " + responseMetadata.getRequestId());
                    System.out.println();
                }
            }

            System.out.println();
        } catch (FBAInventoryServiceMWSException ex) {
            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
        }
    }

    public ListInventorySupplyResponse invokeListInventorySupply(FBAInventoryServiceMWS service, ListInventorySupplyRequest request) {
        try {
            ListInventorySupplyResponse listInventorySupplyResponse = service.listInventorySupply(request);

            System.out.println("ListInventorySupply Action Response");
            System.out.println("=============================================================================");
            System.out.println();
            System.out.println("    ListInventorySupplyResponse");
            System.out.println();

            if (listInventorySupplyResponse.isSetListInventorySupplyResult()) {

                ListInventorySupplyResult listInventorySupplyResult = listInventorySupplyResponse.getListInventorySupplyResult();

                if (listInventorySupplyResult.isSetInventorySupplyList()) {
                    InventorySupplyList inventorySupplyList = listInventorySupplyResult.getInventorySupplyList();
                    java.util.List<InventorySupply> memberList = inventorySupplyList.getMember();

                    for (InventorySupply member : memberList) {

                        if (member.isSetSellerSKU()) {
                            System.out.println("SellerSKU " + member.getSellerSKU());
                        }

                        if (member.isSetTotalSupplyQuantity()) {
                            System.out.println("Quantity " + member.getTotalSupplyQuantity());

                            int totalSupllyQuantity = member.getTotalSupplyQuantity();
                            float cost = Float.valueOf(member.getSellerSKU());
                            float value = totalSupllyQuantity * cost;

                            System.out.print(totalSupllyQuantity);
                            System.out.print("x");
                            System.out.print(cost);
                            System.out.println("=" + value);

                            dollars = dollars + value;
                            //System.out.println("Current Dollars= "+Float.toString(dollars));
                        }
                    }
                }

                if (listInventorySupplyResult.isSetNextToken()) {
                    System.out.println("            NextToken");
                    System.out.println();
                    System.out.println("                " + listInventorySupplyResult.getNextToken());
                    System.out.println();
                }
            }

            if (listInventorySupplyResponse.isSetResponseMetadata()) {
                System.out.println("        ResponseMetadata");
                System.out.println();

                ResponseMetadata responseMetadata = listInventorySupplyResponse.getResponseMetadata();

                if (responseMetadata.isSetRequestId()) {
                    System.out.println("            RequestId");
                    System.out.println();
                    System.out.println("                " + responseMetadata.getRequestId());
                    System.out.println();
                }
            }

            System.out.println();

            return listInventorySupplyResponse;
        } catch (FBAInventoryServiceMWSException ex) {
            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
        }

        System.out.println("TOTAL VALUE= " + Float.toString(dollars));

        Float balance = getBalance();

        System.out.println("TOTAL Balance= " + balance);
        System.out.println("Total = " + (dollars + balance));

        return null;
    }

    /**
     * Send CreateFulfillmentOrder request.
     *
     * @param amazonSku - result Amazon SKU
     *
     * @return ListInventorySupplyResponse
     */
    public ListInventorySupplyResponse createFulfillmentOrder(String amazonSku) {
        final String appName = "FBA Inventory Handling";
        final String appVersion = "�2010-10-01�";

        System.out.println("Welcome User Please Enter the Following Information:-");
        System.out.print("1)	AccessKeyId :- ");

        FBAInventoryServiceMWSConfig fbaInventoryServiceMWSConfig = new FBAInventoryServiceMWSConfig();

        fbaInventoryServiceMWSConfig.setServiceURL("https://mws.amazonservices.com/FulfillmentInventory/2010-10-01/");

        FBAInventoryServiceMWS fbaInventoryServiceMWSClient =
                new FBAInventoryServiceMWSClient(configuration.getAmazonAccessKeyId(), configuration.getAmazonSecretAccessKey(), appName, appVersion, fbaInventoryServiceMWSConfig);
        GetServiceStatusRequest getServiceStatusRequest = new GetServiceStatusRequest();

        String marketplaceId = configuration.getAmazonMarketplaceId();
        String merchantId = configuration.getAmazonMerchantId();

        getServiceStatusRequest.setMarketplace(marketplaceId);
        getServiceStatusRequest.setSellerId(merchantId);
        getServiceStatusRequest.setMarketplace(marketplaceId);

        ListInventorySupplyRequest listInventorySupplyRequest = new ListInventorySupplyRequest();
        listInventorySupplyRequest.setMarketplace(marketplaceId);
        listInventorySupplyRequest.setSellerId(merchantId);
        listInventorySupplyRequest.setMarketplace(marketplaceId);

        SellerSkuList sellerSkus = new SellerSkuList();

        sellerSkus.setMember(Collections.singletonList(amazonSku));

        // request1.setQueryStartDateTime(date2);
        listInventorySupplyRequest.setSellerSkus(sellerSkus);

        System.out.println("=============================================================================");
        System.out.println();

        return invokeListInventorySupply(fbaInventoryServiceMWSClient, listInventorySupplyRequest);
    }

    private Float getBalance() {
        // Get a client connection.
        // Make sure you've set the variables in MWSFinancesServiceSampleConfig.
        MWSFinancesServiceClient mwsFinancesServiceClient; // = MWSFinancesServiceSampleConfig.getClient();
        MWSFinancesServiceConfig mwsFinancesServiceConfig = new MWSFinancesServiceConfig();

        mwsFinancesServiceConfig.setServiceURL(this.configuration.getAmazonServiceUrl());

        mwsFinancesServiceClient = new MWSFinancesServiceAsyncClient(configuration.getAmazonAccessKeyId(),
                configuration.getAmazonSecretAccessKey(), "replaceWithAppName", "replaceWithAppVersion", mwsFinancesServiceConfig, (ExecutorService) null);

        // Create a request.
        ListFinancialEventGroupsRequest listFinancialEventGroupsRequest = new ListFinancialEventGroupsRequest();

        listFinancialEventGroupsRequest.setSellerId(configuration.getAmazonMerchantId());

        Integer maxResultsPerPage = 1;
        listFinancialEventGroupsRequest.setMaxResultsPerPage(maxResultsPerPage);

        Date date = new Date();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        XMLGregorianCalendar xmlGregorianCalendar = null;
        Float currencyAmountFloatValue = null;

        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException exception) {
            exception.printStackTrace();
        }

        listFinancialEventGroupsRequest.setFinancialEventGroupStartedAfter(xmlGregorianCalendar);
        //  ListFinancialEventGroupsSample.invokeListFinancialEventGroups(client, request);
        com.amazon.mws.finances._2015_05_01.model.ResponseHeaderMetadata responseHeaderMetadata;
        ListFinancialEventGroupsResponse listFinancialEventGroupsResponse;

        try {
            listFinancialEventGroupsResponse = mwsFinancesServiceClient.listFinancialEventGroups(listFinancialEventGroupsRequest);
            responseHeaderMetadata = listFinancialEventGroupsResponse.getResponseHeaderMetadata();

            System.out.println("Response:");
            System.out.println("RequestId: " + responseHeaderMetadata.getRequestId());
            System.out.println("Timestamp: " + responseHeaderMetadata.getTimestamp());

            String responseXml = listFinancialEventGroupsResponse.toXML();

            System.out.println(responseXml);

            ListFinancialEventGroupsResult financialEventGroupsResultList = listFinancialEventGroupsResponse.getListFinancialEventGroupsResult();
            List<FinancialEventGroup> financialEventGroupList = financialEventGroupsResultList.getFinancialEventGroupList();

            for (FinancialEventGroup financialEventGroup : financialEventGroupList) {
                if (financialEventGroup.isSetOriginalTotal()) {
                    com.amazon.mws.finances._2015_05_01.model.Currency currency = financialEventGroup.getOriginalTotal();
                    BigDecimal currencyAmount = currency.getCurrencyAmount();
                    currencyAmountFloatValue = currencyAmount.floatValue();

                    System.out.println("Current Balance = " + currencyAmountFloatValue);
                }
            }

            //List<InventorySupply> memberList = inventorySupplyList.getMember();
            //ListFinancialEventGroupResult l=r.getFinancialEventGroupList();
        } catch (MWSFinancesServiceException exception) {
            System.out.println("Service Exception: " + exception.getMessage());
        }

        return currencyAmountFloatValue;
    }
}


