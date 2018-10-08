package com.ebaytoamazonprovider.ui;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyResponse;
import com.ebay.sdk.util.eBayUtil;
import com.ebay.soap.eBLBaseComponents.OrderType;
import com.ebaytoamazonprovider.Constants;
import com.ebaytoamazonprovider.beans.Configuration;
import com.ebaytoamazonprovider.service.AmazonService;
import com.ebaytoamazonprovider.service.EbayService;
import com.ebaytoamazonprovider.util.ConfigurationLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Main frame of QsConnector application.
 */
public final class ProviderFrame extends JFrame {

    /**
     * Default frame width.
     */
    private static final int FRAME_WIDTH = 1500;

    /**
     * Default frame height.
     */
    private static final int FRAME_HEIGHT = 900;

    /**
     * Main frame/window title.
     */
    private static final String FACE_DETECTION_APPLICATION_TITLE = "eBay to Amazon provider";

    /**
     * Name of application properties file (app.ini for now).
     */
    private static final String APPLICATION_CONFIGURATION_FILE_NAME = "configuration.properties";

    /**
     * Column index for upload action buttons.
     */
    private static final int UPLOAD_BUTTON_COLUMN_INDEX = 5;

    /**
     * Column index for edit action buttons.
     */
    private static final int EDIT_BUTTON_COLUMN_INDEX = 6;

    /**
     * Column index for delete action buttons.
     */
    private static final int DELETE_BUTTON_COLUMN_INDEX = 7;

    /**
     * Drawing providerTable cells.
     */
    private JTable providerTable;

    /**
     * Contains providerTable data.
     */
    private DefaultTableModel providerTableModel;

    /**
     * Default constructor. It setup title, size, default close operation and application UI.
     * <p>
     * It also load application properties and initializeProviderTableModel fields using it.
     */
    public ProviderFrame() {
        this.setTitle(FACE_DETECTION_APPLICATION_TITLE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });

        buildElements();

        centerFrame();
    }

    /**
     * Request eBay data for display.
     */
    public void initialize() {
        Runnable backgroundTask = () -> {
            Configuration configuration = loadApplicationConfiguration();

            if (Objects.nonNull(configuration)) {
                EbayService ebayService = new EbayService(configuration);

                try {
                    List<OrderType> unshippedOrders = ebayService.getUnshippedOrders();

                    for (OrderType order : unshippedOrders) {
                        String orderID = order.getOrderID();
                        String numberOfTransaction = Integer.toString(order.getTransactionArray().getTransaction().length);
                        String transactionPrice = Double.toString(order.getTotal().getValue());

                        String paidTime = "";

                        if (order.getPaidTime() != null) {
                            paidTime = eBayUtil.toAPITimeString(order.getPaidTime().getTime());
                        }

                        String buyerUserID = order.getBuyerUserID();

                        Vector dataVector = new Vector();

                        dataVector.add(orderID);
                        dataVector.add(numberOfTransaction);
                        dataVector.add(transactionPrice);
                        dataVector.add(paidTime);
                        dataVector.add(buyerUserID);
                        dataVector.add(new JTextField(20));
                        dataVector.add(new JButton("Create Fulfillment Order"));

                        providerTableModel.addRow(dataVector);
                    }
                } catch (Exception exception) {
                    showErrorMessage(exception.getMessage());
                }
            }
        };

        Thread backgroundThread = new Thread(backgroundTask);

        backgroundThread.start();
    }

    /**
     * Load application configuration.
     *
     * @return QsAccountSettings
     */
    private Configuration loadApplicationConfiguration() {
        try {
            return ConfigurationLoader.newInstance().load(Constants.APPLICATION_CONFIGURATION_FILE_NAME);
        } catch (IOException exception) {
            showErrorMessage(exception.getMessage());
        }

        return null;
    }

    /**
     * Show error message.
     *
     * @param message - source error message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Center frame at screen.
     */
    private void centerFrame() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);

        this.setLocation(x, y);
    }

    /**
     * Create UI elements and add to frame.
     */
    private void buildElements() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        String[] columnNames = {
                "Id",
                "Number of transaction",
                "Transaction price",
                "Paid type",
                "Buyer",
                "Amazon SKU",
                "Action",
        };

        Object[][] emptyData = {};

        providerTableModel = new DefaultTableModel(emptyData, columnNames);

        providerTable = new JTable(providerTableModel) {
            public TableCellRenderer getCellRenderer(int row, int column) {
                return new ProviderTableCellRenderer();
            }
        };

        providerTable.addMouseListener(new ProviderTableMouseListener());

        providerTable.getTableHeader().setReorderingAllowed(false);

        /*providerTable.getColumnModel().getColumn(6).setMinWidth(100);
        providerTable.getColumnModel().getColumn(6).setMaxWidth(100);
        providerTable.getColumnModel().getColumn(6).setPreferredWidth(100);*/

        // Set rows height for providerTable (for all rows).
        providerTable.setRowHeight(40);

        // Set 'Actions' column width. All buttons normally see.
        //TableColumnModel columnModel = providerTable.getColumnModel();
        //columnModel.getColumn(5).setPreferredWidth(120);

        JScrollPane tableScrollPane = new JScrollPane(providerTable);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        this.add(mainPanel);
    }

    /**
     * ProviderTable cell renderer to render text and action buttons and progress burs.
     */
    private class ProviderTableCellRenderer extends JPanel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(final JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int column) {
            if (String.class.isInstance(value)) {
                JLabel label = new JLabel(value.toString());
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 9));

                panel.add(label);

                return label;
            } else if (JButton.class.isInstance(value)) {
                JButton button = JButton.class.cast(value);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));

                buttonPanel.add(button);

                this.add(buttonPanel);

                return this;
            } else if (JTextField.class.isInstance(value)) {
                JTextField textField = JTextField.class.cast(value);

                this.add(textField);

                return this;
            }

            return this;
        }
    }

    /**
     * Mouse listener for action buttons in {@link #providerTable}.
     */
    private class ProviderTableMouseListener extends java.awt.event.MouseAdapter {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
            int columnIndex = providerTable.getColumnModel().getColumnIndexAtX(mouseEvent.getX());
            int rowIndex = mouseEvent.getY() / providerTable.getRowHeight();

            if (rowIndex < providerTable.getRowCount() && rowIndex >= 0 && columnIndex < providerTable.getColumnCount() && columnIndex >= 0) {
                Object value = providerTable.getValueAt(rowIndex, columnIndex);

                if (JButton.class.isInstance(value)) {
                    String skuText = (String) providerTable.getValueAt(rowIndex, 5);

                    Configuration configuration = loadApplicationConfiguration();

                    AmazonService amazonService = new AmazonService(configuration);
                    ListInventorySupplyResponse fulfillmentOrder = amazonService.createFulfillmentOrder(skuText);

                    JOptionPane.showMessageDialog(ProviderFrame.this, "Success request. Request Id is " + fulfillmentOrder.getResponseMetadata().getRequestId());
                }
            }
        }
    }
}
