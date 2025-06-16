/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author ASUS
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.util.ArrayList;
import java.util.List;

public class SellingForm extends JFrame {
    private JComboBox<String> productComboBox;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField qtyField;
    private JButton processButton;
    private List<Product> products;
    private ProductForm productForm;

    public SellingForm(ProductForm productForm) {
        this.productForm = productForm;
        this.products = productForm.getProducts();

        setTitle("WK. Cuan | Form Penjualan");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formPanel.add(new JLabel("Pilih Produk:"));
        productComboBox = new JComboBox<>();
        for (Product product : products) {
            productComboBox.addItem(product.getName());
        }
        formPanel.add(productComboBox);

        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField();
        priceField.setEditable(false);
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField();
        stockField.setEditable(false);
        formPanel.add(stockField);

        formPanel.add(new JLabel("Qty:"));
        qtyField = new JTextField();
        formPanel.add(qtyField);

        processButton = new JButton("Proses");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(processButton);

        // Tambahkan panel ke JFrame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Tampilkan detail produk saat dipilih
        productComboBox.addActionListener(e -> updateProductInfo());

        // Proses penjualan
        processButton.addActionListener(e -> processSale());

        updateProductInfo(); // tampilkan produk pertama saat form dibuka
    }

    private void updateProductInfo() {
        int index = productComboBox.getSelectedIndex();
        if (index != -1) {
            Product selected = products.get(index);
            priceField.setText(String.valueOf(selected.getPrice()));
            stockField.setText(String.valueOf(selected.getStock()));
        }
    }

    private void processSale() {
        int index = productComboBox.getSelectedIndex();
        if (index == -1) return;

        try {
            int qty = Integer.parseInt(qtyField.getText());
            Product selected = products.get(index);

            if (qty > selected.getStock()) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double total = selected.getPrice() * qty;
            selected.setStock(selected.getStock() - qty);

            productForm.loadProductData(productForm.getProducts());

            JOptionPane.showMessageDialog(this,
                "Penjualan berhasil!\n" +
                "Produk: " + selected.getName() + "\n" +
                "Jumlah: " + qty + "\n" +
                "Total: Rp " + total);

            updateProductInfo(); // update stok di tampilan
            qtyField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Qty tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}