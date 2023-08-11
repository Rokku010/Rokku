package presentation.comand;

import java.util.ArrayList;
import java.util.List;

import DOMAIN.model.HoaDon;
import DOMAIN.model.HoaDonObserver;
import persistence.HoaDonPersistenceService;

public class HoaDonServiceImpl implements HoaDonService {
    private final HoaDonPersistenceService persistenceService;
    // observer
    private List<HoaDonObserver> observers = new ArrayList<>();

    public HoaDonServiceImpl(HoaDonPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void addHoaDon(HoaDon hoadon) {
        persistenceService.saveHoaDon(hoadon);
        notifyObservers(getAllHoaDons());
    }

    @Override
    public void removeHoaDon(int id) {
        persistenceService.deleteHoaDon(id);
        notifyObservers(getAllHoaDons());
    }

    @Override
    public void editHoaDon(HoaDon hoadon) {
        persistenceService.updateHoaDon(hoadon);
        notifyObservers(getAllHoaDons());
    }

    @Override
    public HoaDon findHoaDon(int id) {
        return persistenceService.getHoaDonById(id);
    }

    @Override
    public List<HoaDon> getAllHoaDons() {
        return persistenceService.getAllHoaDons();
    }

    // obsever
    public void addObserver(HoaDonObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(HoaDonObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(List<HoaDon> updatedHoaDons) {
        for (HoaDonObserver observer : observers) {
            observer.updateHoaDons(updatedHoaDons);
        }
    }
}
