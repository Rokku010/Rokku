package presentation.comand;

import java.time.LocalDate;

import presentation.HoaDonController;

public class AddHoaDonCommand implements Command {
    private final HoaDonController controller;
    private int maHoaDon;
    private LocalDate ngayHoaDon;
    private String tenKH;
    private String doiTuong;
    private String quocTich;
    private int soLuong;
    private double donGia;
    private double dinhMuc;

    public AddHoaDonCommand(HoaDonController controller, int maHoaDon, LocalDate ngayHoaDon, String tenKH,
            String doiTuong, String quocTich, int soLuong, double donGia, double dinhMuc) {
        this.controller = controller;
        this.maHoaDon = maHoaDon;
        this.ngayHoaDon = ngayHoaDon;
        this.tenKH = tenKH;
        this.doiTuong = doiTuong;
        this.quocTich = quocTich;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.dinhMuc = dinhMuc;
    }

    @Override
    public void execute() {
        controller.addHoaDon(maHoaDon, ngayHoaDon, tenKH, doiTuong, quocTich, soLuong, donGia, dinhMuc);
    }
}
