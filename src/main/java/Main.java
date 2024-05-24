
import graphicalController.LoginGraphicalController;
import utils.CostumLogger;
import utils.PrinterCostum;
import utils.textArt.Arts;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        PrinterCostum.clearConsole(0);
        System.out.print(Arts.benvenuto);
        System.out.print(Arts.alla);
        System.out.print(Arts.chat);
        PrinterCostum.clearConsole(3);
        new Thread(new LoginGraphicalController()).run();
    }
}
