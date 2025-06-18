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

import com.mycompany.mavenproject3.Mavenproject3.BannerPanel;

import java.awt.*;

public class Mavenproject3 extends JFrame implements Runnable {
    private String text;
    private int x;
    private int width;
    private BannerPanel bannerPanel;
    private JButton addProductButton;
    private JButton sellingButton;
    private JButton customerButton;
    private String currentUser;

    public Mavenproject3() {
        initUI();
        showLoginDialog();
    }

    public Mavenproject3(String username) {
        this();
        onLoginSuccess(username);
    }

    private void initUI() {
        ProductForm form = new ProductForm();
        this.text = form.getProductBannerText();

        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel teks berjalan
        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);

        // Tombol "Kelola Produk"
        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        customerButton = new JButton("Kelola Customer");
        sellingButton = new JButton("Penjualan");

        bottomPanel.add(addProductButton);
        bottomPanel.add(customerButton);
        bottomPanel.add(sellingButton);

        addProductButton.setVisible(false);
        customerButton.setVisible(false);
        sellingButton.setVisible(false);

        add(bottomPanel, BorderLayout.SOUTH);

        form.setProductChangeListener(() -> {
            SwingUtilities.invokeLater(() -> {
                updateBannerText(form.getProductBannerText());
            });
        });
    
        addProductButton.addActionListener(e -> {
        // form.setUsername(currentUser);
        form.setVisible(true);
        updateBannerText(form.getProductBannerText());
        });

        customerButton.addActionListener(e -> {
        CustomerForm customerForm = new CustomerForm();
        // customerForm.setUsername(currentUser);
        customerForm.setVisible(true);
        });

        sellingButton.addActionListener(e -> {
        SellingForm sellingForm = new SellingForm(form);
        // sellingForm.setUsername(currentUser);
        sellingForm.setVisible(true);
        });

        // showLoginDialog();

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
    }

    private void showLoginDialog() {
        LoginForm login = new LoginForm(this);
        login.setVisible(true);
    }

    public void onLoginSuccess(String username) {
        this.currentUser = username;

        addProductButton.setVisible(true);
        customerButton.setVisible(true);
        sellingButton.setVisible(true);
    }

    class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(text, x, getHeight() / 2);
        }
    }

    @Override
    public void run() {
        width = getWidth();
        x = width;
        while (true) {
            x -= 5;
            if (x + bannerPanel.getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text) < 0) {
                x = width;
            }
            bannerPanel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void updateBannerText(String newText) {
        this.text = newText;
        x = -bannerPanel.getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(newText);
    }

    public static void main(String[] args) {
        new Mavenproject3();
    }
}
