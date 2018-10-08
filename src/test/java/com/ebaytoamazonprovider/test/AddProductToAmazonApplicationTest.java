package com.ebaytoamazonprovider.test;

//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;

//import com.amazonaws.mws.*;
//import com.amazonaws.mws.model.*;
//import com.ebaytoamazonprovider.Constants;
//import com.ebaytoamazonprovider.beans.Configuration;
//import com.ebaytoamazonprovider.util.ConfigurationLoader;
//import org.junit.Test;

/**
 * Contains test of add product to amazon code.
 */
public class AddProductToAmazonApplicationTest {

//    @Test
//    public void testAddProductToAmazonApplication() throws IOException {
//        Configuration configuration = ConfigurationLoader.newInstance().load(Constants.APPLICATION_CONFIGURATION_FILE_NAME);
//
//        /************************************************************************
//         * Access Key ID and Secret Access Key ID, obtained from:
//         * http://aws.amazon.com
//         ***********************************************************************/
//        final String accessKeyId = configuration.getAmazonAccessKeyId();
//        final String secretAccessKey = configuration.getAmazonSecretAccessKey();
//
//        final String appName = configuration.getAmazonAppName();
//        final String appVersion = configuration.getAmazonAppVersion();
//
//        MarketplaceWebServiceConfig marketplaceWebServiceConfig = new MarketplaceWebServiceConfig();
//
//        /************************************************************************
//         * Uncomment to set the appropriate MWS endpoint.
//         ************************************************************************/
//
//        // US
//        marketplaceWebServiceConfig.setServiceURL(configuration.getAmazonServiceUrl());
//
//        // UK
//        // config.setServiceURL("https://mws.amazonservices.co.uk/");
//        // Germany
//        // config.setServiceURL("https://mws.amazonservices.de/");
//        // France
//        // config.setServiceURL("https://mws.amazonservices.fr/");
//        // Italy
//        // config.setServiceURL("https://mws.amazonservices.it/");
//        // Japan
//        // config.setServiceURL("https://mws.amazonservices.jp/");
//        // China
//        // config.setServiceURL("https://mws.amazonservices.com.cn/");
//        // Canada
//        // config.setServiceURL("https://mws.amazonservices.ca/");
//        // India
//        // config.setServiceURL("https://mws.amazonservices.in/");
//
//        /************************************************************************
//         * You can also try advanced configuration options. Available options are:
//         *
//         *  - Signature Version
//         *  - Proxy Host and Proxy Port
//         *  - User Agent String to be sent to Marketplace Web Service
//         *
//         ***********************************************************************/
//
//        /************************************************************************
//         * Instantiate Http Client Implementation of Marketplace Web Service
//         ***********************************************************************/
//
//        MarketplaceWebService service = new MarketplaceWebServiceClient(accessKeyId, secretAccessKey, appName, appVersion, marketplaceWebServiceConfig);
//
//
//        /************************************************************************
//         * Setup request parameters and uncomment invoke to try out sample for
//         * Submit Feed
//         ***********************************************************************/
//
//        /************************************************************************
//         * Marketplace and Merchant IDs are required parameters for all
//         * Marketplace Web Service calls.
//         ***********************************************************************/
//        final String merchantId = configuration.getAmazonMerchantId();
//        final String sellerDevAuthToken = configuration.getAmazonSellerDevAuthToken();
//
//        // marketplaces to which this feed will be submitted; look at the
//        // API reference document on the MWS website to see which marketplaces are
//        // included if you do not specify the list yourself
////        final IdList marketplaces = new IdList(Arrays.asList("North America and Europe"));
//
//        SubmitFeedRequest submitFeedRequest = new SubmitFeedRequest();
//
//        submitFeedRequest.setMerchant(merchantId);
//        submitFeedRequest.setMWSAuthToken(sellerDevAuthToken);
////        submitFeedRequest.setMarketplaceIdList(marketplaces);
//
//        submitFeedRequest.setFeedType("_POST_PRODUCT_DATA_");
//
//        // MWS exclusively offers a streaming interface for uploading your
//        // feeds. This is because
//        // feed sizes can grow to the 1GB+ range - and as your business grows
//        // you could otherwise
//        // silently reach the feed size where your in-memory solution will no
//        // longer work, leaving you
//        // puzzled as to why a solution that worked for a long time suddenly
//        // stopped working though
//        // you made no changes. For the same reason, we strongly encourage you
//        // to generate your feeds to
//        // local disk then upload them directly from disk to MWS via Java -
//        // without buffering them in Java
//        // memory in their entirety.
//        // Note: MarketplaceWebServiceClient will not retry a submit feed request
//        // because there is no way to reset the InputStream from our client.
//        // To enable retry, recreate the InputStream and resubmit the feed
//        // with the new InputStream.
//        //
//        // request.setFeedContent( new FileInputStream("my-feed.xml" /*or
//        // "my-flat-file.txt" if you use flat files*/);x
//
//        String productSKU = "56789";
//
//        String productXml = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
//                "<AmazonEnvelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
//                "    xsi:noNamespaceSchemaLocation=\"amzn-envelope.xsd\">\n" +
//                "  <Header>\n" +
//                "    <DocumentVersion>1.01</DocumentVersion>\n" +
//                "    <MerchantIdentifier>" + merchantId + "</MerchantIdentifier>\n" +
//                "  </Header>\n" +
//                "  <MessageType>Product</MessageType>\n" +
//                "  <PurgeAndReplace>false</PurgeAndReplace>\n" +
//                "  <Message>\n" +
//                "    <MessageID>1</MessageID>\n" +
//                "    <OperationType>Update</OperationType>\n" +
//                "    <Product>\n" +
//                "      <SKU>" + productSKU + "</SKU>\n" +
//                "      <StandardProductID>\n" +
//                "        <Type>UPC</Type>\n" +
//                "        <Value>215452154215</Value>\n" +
//                "      </StandardProductID>\n" +
//                "      <DescriptionData>\n" +
//                "        <Title>Example Product Title</Title>\n" +
//                "        <Description>This is an example product description.</Description>\n" +
//                "      </DescriptionData>\n" +
//                "    </Product>\n" +
//                "  </Message>\n" +
//                "</AmazonEnvelope>";
//
//        InputStream xmlInputStream = new ByteArrayInputStream(productXml.getBytes());
//
//        submitFeedRequest.setFeedContent(xmlInputStream);
//
//        invokeSubmitFeed(service, submitFeedRequest);
//
//    }
//
//    /**
//     * Submit Feed request sample Uploads a file for processing together with
//     * the necessary metadata to process the file, such as which type of feed it
//     * is. PurgeAndReplace if true means that your existing e.g. inventory is
//     * wiped out and replace with the contents of this feed - use with caution
//     * (the default is false).
//     *
//     * @param marketplaceWebService instance of MarketplaceWebService service
//     * @param submitFeedRequest Action to invoke
//     */
//    private static void invokeSubmitFeed(MarketplaceWebService marketplaceWebService, SubmitFeedRequest submitFeedRequest) {
//        try {
//
//            SubmitFeedResponse submitFeedResponse = marketplaceWebService.submitFeed(submitFeedRequest);
//
//            System.out.println("SubmitFeed Action Response");
//            System.out.println("=============================================================================");
//            System.out.println();
//
//            System.out.print("    SubmitFeedResponse");
//            System.out.println();
//            if (submitFeedResponse.isSetSubmitFeedResult()) {
//                System.out.print("        SubmitFeedResult");
//                System.out.println();
//
//                SubmitFeedResult submitFeedResult = submitFeedResponse.getSubmitFeedResult();
//
//                if (submitFeedResult.isSetFeedSubmissionInfo()) {
//                    System.out.print("            FeedSubmissionInfo");
//                    System.out.println();
//
//                    FeedSubmissionInfo feedSubmissionInfo = submitFeedResult.getFeedSubmissionInfo();
//
//                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
//                        System.out.print("                FeedSubmissionId");
//                        System.out.println();
//                        System.out.print("                    " + feedSubmissionInfo.getFeedSubmissionId());
//                        System.out.println();
//                    }
//
//                    if (feedSubmissionInfo.isSetFeedType()) {
//                        System.out.print("                FeedType");
//                        System.out.println();
//                        System.out.print("                    " + feedSubmissionInfo.getFeedType());
//                        System.out.println();
//                    }
//
//                    if (feedSubmissionInfo.isSetSubmittedDate()) {
//                        System.out.print("                SubmittedDate");
//                        System.out.println();
//                        System.out.print("                    " + feedSubmissionInfo.getSubmittedDate());
//                        System.out.println();
//                    }
//
//                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
//                        System.out.print("                FeedProcessingStatus");
//                        System.out.println();
//                        System.out.print("                    " + feedSubmissionInfo.getFeedProcessingStatus());
//                        System.out.println();
//                    }
//
//                    if (feedSubmissionInfo.isSetStartedProcessingDate()) {
//                        System.out.print("                StartedProcessingDate");
//                        System.out.println();
//                        System.out.print("                    " + feedSubmissionInfo.getStartedProcessingDate());
//                        System.out.println();
//                    }
//
//                    if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
//                        System.out.print("                CompletedProcessingDate");
//                        System.out.println();
//                        System.out.print("                    " + feedSubmissionInfo.getCompletedProcessingDate());
//                        System.out.println();
//                    }
//                }
//            }
//
//            if (submitFeedResponse.isSetResponseMetadata()) {
//                System.out.print("        ResponseMetadata");
//                System.out.println();
//
//                ResponseMetadata responseMetadata = submitFeedResponse.getResponseMetadata();
//
//                if (responseMetadata.isSetRequestId()) {
//                    System.out.print("            RequestId");
//                    System.out.println();
//                    System.out.print("                " + responseMetadata.getRequestId());
//                    System.out.println();
//                }
//            }
//
//            System.out.println(submitFeedResponse.getResponseHeaderMetadata());
//            System.out.println();
//            System.out.println();
//
//        } catch (MarketplaceWebServiceException webServiceException) {
//            System.out.println("Caught Exception: " + webServiceException.getMessage());
//            System.out.println("Response Status Code: " + webServiceException.getStatusCode());
//            System.out.println("Error Code: " + webServiceException.getErrorCode());
//            System.out.println("Error Type: " + webServiceException.getErrorType());
//            System.out.println("Request ID: " + webServiceException.getRequestId());
//            System.out.print("XML: " + webServiceException.getXML());
//            System.out.println("ResponseHeaderMetadata: " + webServiceException.getResponseHeaderMetadata());
//        }
//    }
}
