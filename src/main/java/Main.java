import graphicalController.LoginGraphicalController;
import utils.PrinterCostum;
import utils.textArt.Arts;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        PrinterCostum.clearConsole(0);
        System.out.print(Arts.benvenuto);
        System.out.print(Arts.alla);
        System.out.print(Arts.chat);
        PrinterCostum.clearConsole(3);
        new Thread(new LoginGraphicalController()).start();
    }
}
