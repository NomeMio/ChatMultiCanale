package utils;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class PrinterCostum {
    public  static  void printFileResource(String file){
        BufferedReader in = null;
        try {
            in =new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while(line != null)
            {
                System.out.println(line);
                line = in.readLine();
            }
            in.close();
        }catch (IOException e) {
            CostumLogger.getInstance().logError(e);
        }
    }
    public static void clearConsole(int delay)
    {
        try {
            TimeUnit.SECONDS.sleep(delay);
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (InterruptedException e) {
            CostumLogger.getInstance().logError(e);
            return;
        }
    }

    public static String getString(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s="";

        try {
            s=reader.readLine();
           // reader.close();
        } catch (IOException e) {
            CostumLogger.getInstance().logString("dd");
        }
        return s;
    }

}
