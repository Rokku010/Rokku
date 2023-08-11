package persistence;

import java.util.List;
import DOMAIN.model.HoaDon;

public interface HoaDonPersistenceService {
   void saveHoaDon(HoaDon var1);

   void deleteHoaDon(int var1);

   void updateHoaDon(HoaDon var1);

   // Trả về đối tượng HoaDon dựa trên mã hóa đơn cung cấp.
   HoaDon getHoaDonById(int var1);

   List<HoaDon> getAllHoaDons();
}
