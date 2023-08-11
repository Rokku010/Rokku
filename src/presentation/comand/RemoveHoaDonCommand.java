package presentation.comand;

import presentation.HoaDonController;

public class RemoveHoaDonCommand implements Command {
    private final HoaDonController controller;
    private final int maHoaDon;

    public RemoveHoaDonCommand(HoaDonController controller, int maHoaDon) {
        this.controller = controller;
        this.maHoaDon = maHoaDon;
    }

    @Override
    public void execute() {
        controller.removeHoaDon(maHoaDon);
    }
}
