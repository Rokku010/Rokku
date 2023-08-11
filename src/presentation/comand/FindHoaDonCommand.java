package presentation.comand;

import java.time.LocalDate;

import presentation.HoaDonController;

public class FindHoaDonCommand implements Command {
    private final HoaDonController controller;
    private int maHoaDon;
    private LocalDate ngayHoaDon;
    private String tenKH;
    private String doiTuong;
    private String quocTich;
    private double soLuong;
    private double donGia;
    private int dinhMuc;

    public FindHoaDonCommand(HoaDonController controller, int maHoaDon, LocalDate ngayHoaDon, String tenKH,
            String doiTuong, String quocTich, double soLuong, double donGia, int dinhMuc) {
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
