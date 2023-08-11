package presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import DOMAIN.model.HoaDon;
import DOMAIN.model.HoaDonObserver;
import presentation.comand.HoaDonService;
import presentation.comand.CommandProcessor;
import presentation.comand.Command;
import presentation.comand.AddHoaDonCommand;
import presentation.comand.EditHoaDonCommand;
import presentation.comand.RemoveHoaDonCommand;

public class HoaDonManagementUI extends JFrame implements HoaDonObserver {
    private final HoaDonService hoaDonService;
    private final DefaultTableModel tableModel;
    private final JTable hoaDonTable;
    private final JLabel maLabel, ngayLabel, tenLabel, loaiKhachLabel, quocTichLabel, soLuongLabel, donGiaLabel,
            dinhMucLabel;
    private final JTextField maField, ngayField, tenField, loaiKhachField, quocTichField, soLuongField, donGiaField,
            dinhMucField;
    private final JButton addButton, removeButton, editButton, findButton;
    private final HoaDonController hoaDonController;
    private final CommandProcessor commandProcessor;

    private Set<Integer> uniqueHoaDonIds = new HashSet<>();

    public HoaDonManagementUI(HoaDonService hoadonService) {
        this.hoaDonService = hoadonService;
        this.hoaDonController = new HoaDonController(hoadonService);
        commandProcessor = new CommandProcessor();

        // observer
        hoadonService.addObserver(this);

        maLabel = new JLabel("Mã Hoá đơn:");
        ngayLabel = new JLabel("Ngày Hoá Đơn :");
        tenLabel = new JLabel("Tên Khách Hàng:");
        loaiKhachLabel = new JLabel("Loại Khách Hàng:");
        quocTichLabel = new JLabel("Quốc Tịch:");
        soLuongLabel = new JLabel("Số Lượng KW:");
        donGiaLabel = new JLabel("Đơn Giá:");
        dinhMucLabel = new JLabel("Dịnh Mức:");

        maField = new JTextField(10);
        ngayField = new JTextField(10);
        tenField = new JTextField(20);
        loaiKhachField = new JTextField(20);
        quocTichField = new JTextField(10);
        soLuongField = new JTextField(10);
        donGiaField = new JTextField(10);
        dinhMucField = new JTextField(5);

        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        editButton = new JButton("Edit");
        findButton = new JButton("Find");
        // tính thuê phòng và tính trung bình tháng
        JButton calculateKWTypeCustomerButton = new JButton("Tính số lượng KW cho từng loại khách hàng");

        calculateKWTypeCustomerButton.addActionListener(e -> calculateKWTypeCustomer());

        JButton calculateAveragePriceButton = new JButton("Tính trung bình thành tiền người nước ngoài");
        calculateAveragePriceButton.addActionListener(e -> calculateAndDisplayAveragePrice());

        String[] columnNames = { "MÃ HOÁ ĐƠN", "NGÀY HOÁ ĐƠN", "TÊN KHÁCH HÀNG", "LOẠI KHÁCH HÀNG", "QUỐC TỊCH",
                "SỐ LƯỢNG", "ĐƠN GIÁ", "ĐỊNH MỨC", "TỔNG TIỀN" };
        tableModel = new DefaultTableModel(columnNames, 0);
        hoaDonTable = new JTable(tableModel);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public void setValue(Object value) {
                if (value instanceof LocalDate) {
                    value = ((LocalDate) value).format(formatter);
                }
                super.setValue(value);
            }
        };
        hoaDonTable.getColumnModel().getColumn(1).setCellRenderer(renderer);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        hoaDonTable.setRowSorter(sorter);
        Comparator<Integer> idComparator = Comparator.naturalOrder();
        sorter.setComparator(0, idComparator);
        Comparator<Integer> idReverseComparator = Comparator.reverseOrder();
        sorter.setComparator(0, idReverseComparator);

        refreshHoaDonTable();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int maHD = Integer.parseInt(maField.getText());
                    LocalDate ngayHD = parseHoaDonDate(ngayField.getText());
                    String tenKH = tenField.getText();
                    String loaiKhach = loaiKhachField.getText();
                    String quocTich = quocTichField.getText();
                    int soLuong = Integer.parseInt(soLuongField.getText());
                    double donGia = Double.parseDouble(donGiaField.getText());
                    double dinhMuc = Double.parseDouble(dinhMucField.getText());

                    hoaDonController.addHoaDon(maHD, ngayHD, tenKH, loaiKhach, quocTich, soLuong, donGia, dinhMuc);

                    clearInputFields();
                    refreshHoaDonTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HoaDonManagementUI.this,
                            "Invalid input for values. Please enter valid numbers.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(HoaDonManagementUI.this, ex.getMessage());
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = hoaDonTable.getSelectedRow();

                if (selectedRow != -1) {
                    int maHD = (int) hoaDonTable.getValueAt(selectedRow, 0);
                    hoaDonController.removeHoaDon(maHD);
                    uniqueHoaDonIds.remove(maHD);
                    refreshHoaDonTable();
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(HoaDonManagementUI.this, "Please select an recipe to remove.");
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = hoaDonTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        int maHD = Integer.parseInt(maField.getText());
                        LocalDate ngayHD = parseHoaDonDate(ngayField.getText());
                        String tenKH = tenField.getText();
                        String loaiKhach = loaiKhachField.getText();
                        String quocTich = quocTichField.getText();
                        int soLuong = Integer.parseInt(soLuongField.getText());
                        double donGia = Double.parseDouble(donGiaField.getText());
                        double dinhMuc = Double.parseDouble(dinhMucField.getText());

                        hoaDonController.editHoaDon(maHD, ngayHD, tenKH, loaiKhach, quocTich, soLuong, donGia,
                                selectedRow);

                        refreshHoaDonTable();
                        clearInputFields();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(HoaDonManagementUI.this,
                                "Invalid input for values. Please enter valid numbers.");
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(HoaDonManagementUI.this, ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(HoaDonManagementUI.this, "Please select an invoice to edit.");
                }
            }
        });

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog(HoaDonManagementUI.this, "Enter the recipe ID to find:");
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        int maHD = Integer.parseInt(idStr);
                        HoaDon hoadon = hoaDonController.findHoaDon(maHD);
                        if (hoadon != null) {
                            populateInputFields(hoadon);

                            // Find the row index of the found invoice in the table
                            int rowIndex = findRowIndexByHoaDonId(maHD);

                            // Select the corresponding row in the table
                            if (rowIndex != -1) {
                                hoaDonTable.setRowSelectionInterval(rowIndex, rowIndex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(HoaDonManagementUI.this, "Recipe not found with ID: " + maHD);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(HoaDonManagementUI.this,
                                "Invalid input for ID. Please enter a valid number.");
                    }
                }
            }
        });

        hoaDonTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedHoaDonInfo();
            }
        });

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(maLabel, gbc);
        gbc.gridx++;
        inputPanel.add(maField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(ngayLabel, gbc);
        gbc.gridx++;
        inputPanel.add(ngayField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(tenLabel, gbc);
        gbc.gridx++;
        inputPanel.add(tenField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(loaiKhachLabel, gbc);
        gbc.gridx++;
        inputPanel.add(loaiKhachField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(quocTichLabel, gbc);
        gbc.gridx++;
        inputPanel.add(quocTichField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(soLuongLabel, gbc);
        gbc.gridx++;
        inputPanel.add(soLuongField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(donGiaLabel, gbc);
        gbc.gridx++;
        inputPanel.add(donGiaField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(dinhMucLabel, gbc);
        gbc.gridx++;
        inputPanel.add(dinhMucField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(findButton);
        buttonPanel.add(calculateKWTypeCustomerButton);
        buttonPanel.add(calculateAveragePriceButton);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(hoaDonTable), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setTitle("Recipe Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 400);
        this.add(mainPanel);
        this.setVisible(true);
    }

    public void addHoaDon(int maHoaDon, LocalDate ngayHoaDon, String tenKH, String doiTuong, String quocTich,
            int soLuong, double donGia, double dinhMuc) {
        if (maHoaDon < 0 || doiTuong != "vn" || doiTuong != "nn" || soLuong < 0 ||
                donGia < 0 || dinhMuc < 0) {
            throw new IllegalArgumentException("Invalid hours/days value.");

        }

        Command addCommand = new AddHoaDonCommand(hoaDonController, maHoaDon, ngayHoaDon, tenKH, doiTuong, quocTich,
                soLuong, donGia, dinhMuc);
        commandProcessor.executeCommand(addCommand);
    }

    private LocalDate parseHoaDonDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format. Please enter a date in the format DD-MM-YYYY.");
        }

    }

    public void removeHoaDon(int maHoaDon) {
        Command removeCommand = new RemoveHoaDonCommand(hoaDonController, maHoaDon);
        commandProcessor.executeCommand(removeCommand);
    }

    public void editHoaDon(int maHoaDon, LocalDate ngayHoaDon, String tenKH, String doiTuong, String quocTich,
            int soLuong, double donGia, double dinhMuc) {
        HoaDon hoadon = hoaDonService.findHoaDon(maHoaDon);
        if (hoadon != null) {
            if (maHoaDon < 0 || doiTuong != "vn" || doiTuong != "nn" || soLuong < 0 ||
                    donGia < 0 || dinhMuc < 0) {
                throw new IllegalArgumentException("Invalid hours/days value.");

            }
            hoadon.setMaHoaDon(maHoaDon);
            hoadon.setTenKH(tenKH);
            hoadon.setNgayHoaDon(ngayHoaDon);
            hoadon.setDoiTuong(doiTuong);
            hoadon.setQuocTich(quocTich);
            hoadon.setDonGia(donGia);
            hoadon.setSoLuong(soLuong);
            hoadon.setDinhMuc(dinhMuc);

            Command editCommand = new EditHoaDonCommand(hoaDonController, maHoaDon, ngayHoaDon, tenKH, doiTuong,
                    quocTich, soLuong, donGia, dinhMuc);
            commandProcessor.executeCommand(editCommand);
        }
    }

    public HoaDon findHoaDon(int maHoaDon) {
        return hoaDonService.findHoaDon(maHoaDon);
    }

    public List<HoaDon> getAllHoaDons() {
        return hoaDonService.getAllHoaDons();
    }

    private int findRowIndexByHoaDonId(int maHoaDon) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int rowHoaDonId = (int) tableModel.getValueAt(i, 0);
            if (rowHoaDonId == maHoaDon) {
                return i;
            }
        }
        return -1;
    }

    private void showSelectedHoaDonInfo() {
        int selectedRow = hoaDonTable.getSelectedRow();
        if (selectedRow != -1) {
            int hoaDonId = (int) hoaDonTable.getValueAt(selectedRow, 0);
            HoaDon hoadon = hoaDonService.findHoaDon(hoaDonId);
            if (hoadon != null) {
                populateInputFields(hoadon);
            }
        }
    }

    private void populateInputFields(HoaDon hoadon) {
        maField.setText(String.valueOf(hoadon.getMaHoaDon()));
        ngayField.setText(hoadon.getNgayHoaDon().toString());
        ;
        tenField.setText(hoadon.getTenKH());
        loaiKhachField.setText(hoadon.getDoiTuong());
        quocTichField.setText(hoadon.getQuocTich());
        soLuongField.setText(String.valueOf(hoadon.getSoLuong()));
        donGiaField.setText(String.valueOf(hoadon.getDonGia()));
        dinhMucField.setText(String.valueOf(hoadon.getDinhMuc()));

    }

    private void clearInputFields() {
        maField.setText("");
        ngayField.setText("");
        tenField.setText("");
        loaiKhachField.setText("");
        quocTichField.setText("");
        soLuongField.setText("");
        donGiaField.setText("");
        dinhMucField.setText("");
    }

    private void refreshHoaDonTable() {
        tableModel.setRowCount(0);

        List<HoaDon> hoadons = hoaDonService.getAllHoaDons();
        for (HoaDon hoadon : hoadons) {
            double totalMoney = hoadon.tongTien();
            Object[] rowData = { hoadon.getMaHoaDon(), hoadon.getNgayHoaDon(), hoadon.getTenKH(),
                    hoadon.getDoiTuong(), hoadon.getQuocTich(), hoadon.getSoLuong(),
                    hoadon.getDonGia(), hoadon.getDinhMuc(), totalMoney };
            tableModel.addRow(rowData);
        }
    }

    private void calculateKWTypeCustomer() {
        int tong = 0;
        int count = 0;

        List<HoaDon> hoadons = hoaDonService.getAllHoaDons();
        for (HoaDon hoaDon : hoadons) {
            tong += hoaDon.tongTien();
            count++;
        }

        JOptionPane.showMessageDialog(this,
                "Trung bình Thành tiền người nước ngoài:  " + tong / count);
    }

    // observer
    public void updateHoaDon(List<HoaDon> updateHoaDons) {
        // Cập nhật giao diện UI của bạn với danh sách hóa đơn mới
        // Ví dụ: bạn có thể làm mới bảng bằng dữ liệu mới
        refreshHoaDonTable();
    }

    private void calculateAndDisplayAveragePrice() {
        String monthStr = JOptionPane.showInputDialog(this, "Nhập tháng (MM) để tính trung bình tổng tiền:");
        if (monthStr != null && !monthStr.isEmpty()) {
            tableModel.setRowCount(0);

            List<HoaDon> hoadons = hoaDonService.getAllHoaDons();

            for (HoaDon hoadon : hoadons) {
                LocalDate date = hoadon.getNgayHoaDon();
                if (date.getMonthValue() == Integer.parseInt(monthStr)) {
                    double totalMoney = hoadon.tongTien();
                    Object[] rowData = { hoadon.getMaHoaDon(), hoadon.getNgayHoaDon(), hoadon.getTenKH(),
                            hoadon.getDoiTuong(), hoadon.getQuocTich(), hoadon.getSoLuong(),
                            hoadon.getDonGia(), hoadon.getDinhMuc(), totalMoney };
                    tableModel.addRow(rowData);
                }
            }
        }

    }

    @Override
    public void updateHoaDons(List<HoaDon> updatedHoaDons) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateHoaDons'");
    }
}
