package presentation.comand;

import java.util.List;

import DOMAIN.model.HoaDon;
import DOMAIN.model.HoaDonObserver;

public interface HoaDonService {
    void addHoaDon(HoaDon hoadon);

    void removeHoaDon(int id);

    void editHoaDon(HoaDon hoadon);

    HoaDon findHoaDon(int id);

    List<HoaDon> getAllHoaDons();

    void addObserver(HoaDonObserver observer); // Add this method

    void removeObserver(HoaDonObserver observer); // Add this method
}
