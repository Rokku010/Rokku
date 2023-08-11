package persistence;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import DOMAIN.model.HoaDon;

public class HoaDonPersistenceServiceImpl implements HoaDonPersistenceService {
    private final String filePath;

    public HoaDonPersistenceServiceImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveHoaDon(HoaDon hoadon) {
        List<HoaDon> invoices = getAllHoaDons();
        invoices.add(hoadon);
        saveHoaDonToFile(invoices);
    }

    @Override
    public void deleteHoaDon(int id) {
        List<HoaDon> invoices = getAllHoaDons();
        invoices.removeIf(invoice -> invoice.getMaHoaDon() == id);
        saveHoaDonToFile(invoices);
    }

    @Override
    public void updateHoaDon(HoaDon hoadon) {
        List<HoaDon> invoices = getAllHoaDons();
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getMaHoaDon() == hoadon.getMaHoaDon()) {
                invoices.set(i, hoadon);
                break;
            }
        }
        saveHoaDonToFile(invoices);
    }

    @Override
    public HoaDon getHoaDonById(int id) {
        List<HoaDon> hoadons = getAllHoaDons();
        for (HoaDon hoadon : hoadons) {
            if (hoadon.getMaHoaDon() == id) {
                return hoadon;
            }
        }
        return null;
    }

    @Override
    public List<HoaDon> getAllHoaDons() {
        List<HoaDon> hoadons = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                HoaDon hoadon = (HoaDon) inputStream.readObject();
                hoadons.add(hoadon);
            }
        } catch (EOFException e) {
            // End of file, do nothing
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hoadons;
    }

    private void saveHoaDonToFile(List<HoaDon> hoadons) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (HoaDon hoadon : hoadons) {
                outputStream.writeObject(hoadon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
