package presentation;

import DOMAIN.model.HoaDon;
import presentation.comand.HoaDonService;

import java.time.LocalDate;
import java.util.List;

public class HoaDonController {
    private final HoaDonService hoaDonService;

    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    public void addHoaDon(int maHoaDon, LocalDate ngayHoaDon, String tenKH, String doiTuong, String quocTich,
            double soLuong, double donGia, double dinhMuc) {
        HoaDon hoadon = new HoaDon(maHoaDon, ngayHoaDon, tenKH, doiTuong, quocTich, soLuong, donGia, dinhMuc);

        hoaDonService.addHoaDon(hoadon);
    }

    public void removeHoaDon(int maHoaDon) {
        hoaDonService.removeHoaDon(maHoaDon);
    }

    public void editHoaDon(int maHoaDon, LocalDate ngayHoaDon, String tenKH, String doiTuong, String quocTich,
            double soLuong,
            double donGia, int dinhMuc) {
        HoaDon hoadon = new HoaDon(maHoaDon, ngayHoaDon, tenKH, doiTuong, quocTich, soLuong, donGia, dinhMuc);
        hoaDonService.editHoaDon(hoadon);

    }

    public HoaDon findHoaDon(int maHoaDon) {
        return hoaDonService.findHoaDon(maHoaDon);
    }

    public List<HoaDon> getAllHoaDons() {
        return hoaDonService.getAllHoaDons();
    }
}
