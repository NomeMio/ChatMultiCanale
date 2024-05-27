package utils;

import Exceptions.TabellaFormattataMaleException;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import utils.textArt.BordersAsciTableNEw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PrinterCostum {
    public static void printFileResource(String file) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }
            in.close();
        } catch (IOException e) {
            CostumLogger.getInstance().logError(e);
        }
    }

    public static void clearConsole(int delay) {
        try {
            TimeUnit.SECONDS.sleep(delay);
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (InterruptedException e) {
            CostumLogger.getInstance().logError(e);
            return;
        }
    }

    public static String getString() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s = "";

        try {
            s = reader.readLine();
            // reader.close();
        } catch (IOException e) {
            CostumLogger.getInstance().logString("dd");
        }
        return s;
    }

    public static void stampaTabella(String[][] tabella) throws TabellaFormattataMaleException {
        TableBuilder table = new TableBuilder();
        int lenght=tabella[0].length;
        for (String[] colonna:tabella){
            if(colonna.length!=lenght)throw new TabellaFormattataMaleException();
            Iterator<String> it= Arrays.stream(colonna).iterator();
            TableBuilder test=new TableBuilder().row().cell(it.next()).border(BordersAsciTableNEw.ONLYBOt);
            while(it.hasNext()){
                test.row().cell(it.next());
            }
            table.cell(test.build()).border(BordersAsciTableNEw.OnlyLati);
        }
        System.out.println(table.build().render());
    }

}
