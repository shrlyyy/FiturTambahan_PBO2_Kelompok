/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ASUS
 */

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReservationForm extends JFrame {
    private JComboBox<String> phoneComboBox;
    private JTextField idField;
    private JTextField nameField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField numberOfPeopleField;
    private JButton saveButton;
    private JButton deleteButton;

    private JTable reservationTable;
    private DefaultTableModel tableModel;

    private ArrayList<Customer> customers;
    private static ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationForm(ArrayList<Customer> customers) {
        this.customers = customers;

        setTitle("WK. Cuan | Form Reservasi");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel form dengan GridLayout
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Nomor Telepon:"));
        phoneComboBox = new JComboBox<>();
        phoneComboBox.setEditable(true);
        // Isi combo box dengan semua nomor telepon customer
        for (Customer c : customers) {
            phoneComboBox.addItem(c.getPhoneNumber().toString());
        }
        formPanel.add(phoneComboBox);

        formPanel.add(new JLabel("ID Customer:"));
        idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField);

        formPanel.add(new JLabel("Nama Customer:"));
        nameField = new JTextField();
        nameField.setEditable(false);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Tanggal Reservasi (yyyy-MM-dd):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Jam Reservasi (HH:mm):"));
        timeField = new JTextField();
        formPanel.add(timeField);

        formPanel.add(new JLabel("Jumlah Orang:"));
        numberOfPeopleField = new JTextField();
        formPanel.add(numberOfPeopleField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Simpan");
        deleteButton = new JButton("Hapus");
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        // Tabel reservasi
        tableModel = new DefaultTableModel(new String[]{"ID Reservasi", "ID Customer", "Nama", "Tanggal", "Jam", "Jumlah Orang"}, 0);
        reservationTable = new JTable(tableModel);
        getContentPane().add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        // Listener untuk input realtime di phoneComboBox editable
        JTextField editor = (JTextField) phoneComboBox.getEditor().getEditorComponent();
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCustomerInfo();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCustomerInfo();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCustomerInfo();
            }
        });

        saveButton.addActionListener(e -> saveReservation());
        deleteButton.addActionListener(e -> deleteReservation());

        refreshTable();
    }

    private void updateCustomerInfo() {
        String input = ((JTextField) phoneComboBox.getEditor().getEditorComponent()).getText().trim();
        if (input.isEmpty()) {
            idField.setText("");
            nameField.setText("");
            return;
        }

        Customer matchedCustomer = null;
        for (Customer c : customers) {
            if (c.getPhoneNumber().toString().startsWith(input)) {
                matchedCustomer = c;
                break;
            }
        }

        if (matchedCustomer != null) {
            idField.setText(matchedCustomer.getId());
            nameField.setText(matchedCustomer.getName());
        } else {
            idField.setText("");
            nameField.setText("");
        }
    }

    private void saveReservation() {
        String idCust = idField.getText();
        String nameCust = nameField.getText();
        String dateStr = dateField.getText().trim();
        String timeStr = timeField.getText().trim();
        String numberStr = numberOfPeopleField.getText().trim();

        if (idCust.isEmpty() || nameCust.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cari dan pilih customer terlebih dahulu.");
            return;
        }
        if (dateStr.isEmpty() || timeStr.isEmpty() || numberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);
            LocalTime time = LocalTime.parse(timeStr);
            int numPeople = Integer.parseInt(numberStr);

            int reservationId = reservations.size() + 1;
            int customerId = Integer.parseInt(idCust.replaceAll("\\D+", ""));

            Reservation newRes = new Reservation(
                    reservationId,
                    customerId,
                    date,
                    time,
                    "Meja 1",
                    numPeople,
                    "admin", null, null
            );

            reservations.add(newRes);
            JOptionPane.showMessageDialog(this, "Reservasi berhasil disimpan.");
            clearFields();
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Format input tidak valid: " + ex.getMessage());
        }
    }

    private void deleteReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow != -1) {
            int resId = (int) tableModel.getValueAt(selectedRow, 0);
            Reservation toRemove = null;
            for (Reservation r : reservations) {
                if (r.getReservationId() == resId) {
                    toRemove = r;
                    break;
                }
            }
            if (toRemove != null) {
                reservations.remove(toRemove);
                JOptionPane.showMessageDialog(this, "Reservasi berhasil dihapus.");
                refreshTable();
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih reservasi yang ingin dihapus.");
        }
    }

    private void clearFields() {
        ((JTextField) phoneComboBox.getEditor().getEditorComponent()).setText("");
        idField.setText("");
        nameField.setText("");
        dateField.setText("");
        timeField.setText("");
        numberOfPeopleField.setText("");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Reservation r : reservations) {
            String custName = "";
            for (Customer c : customers) {
                if (c.getId().equals("cust" + String.format("%03d", r.getCustomerId()))) {
                    custName = c.getName();
                    break;
                }
            }
            tableModel.addRow(new Object[]{
                    r.getReservationId(),
                    "cust" + String.format("%03d", r.getCustomerId()),
                    custName,
                    r.getReservationDate(),
                    r.getReservationTime(),
                    r.getNumberOfPeople()
            });
        }
    }
}
