package DOMAIN.model;

import java.io.Serializable;
import java.time.LocalDate;

public class HoaDon implements Serializable {
    private int maHoaDon;
    private LocalDate ngayHoaDon;
    private String tenKH;
    private String doiTuong;
    private String quocTich;
    private double soLuong;
    private double donGia;
    private double dinhMuc;

    public HoaDon(int maHoaDon, LocalDate ngayHoaDon, String tenKH, String doiTuong, String quocTich, double soLuong,
            double donGia, double dinhMuc) {
        this.maHoaDon = maHoaDon;
        this.ngayHoaDon = ngayHoaDon;
        this.tenKH = tenKH;
        this.doiTuong = doiTuong;
        this.quocTich = quocTich;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.dinhMuc = dinhMuc;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDate getNgayHoaDon() {
        return ngayHoaDon;
    }

    public void setNgayHoaDon(LocalDate ngayHoaDon) {
        this.ngayHoaDon = ngayHoaDon;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getDoiTuong() {
        return doiTuong;
    }

    public void setDoiTuong(String doiTuong) {
        this.doiTuong = doiTuong;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public double getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(double soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getDinhMuc() {
        return dinhMuc;
    }

    public void setDinhMuc(double dinhMuc) {
        this.dinhMuc = dinhMuc;
    }

    public double tongTien() {
        if (this.quocTich == "vn") {
            if (this.soLuong > this.dinhMuc) {
                return this.dinhMuc * this.donGia + (this.soLuong - this.dinhMuc) * this.donGia * 2.5;
            } else {
                return this.soLuong * this.donGia;
            }
        } else {
            return this.soLuong * this.donGia;
        }
    }
}
