package presentation;

import persistence.HoaDonPersistenceService;
import persistence.HoaDonPersistenceServiceImpl;
import presentation.comand.HoaDonService;
import presentation.comand.HoaDonServiceImpl;

public class Main {

    public static void main(String[] args) {
        HoaDonPersistenceService invoicePersistenceService = new HoaDonPersistenceServiceImpl("dataHoaDon.db");
        HoaDonService invoiceService = new HoaDonServiceImpl(invoicePersistenceService);
        HoaDonManagementUI ui = new HoaDonManagementUI(invoiceService);
    }
}
