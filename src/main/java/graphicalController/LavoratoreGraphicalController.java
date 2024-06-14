package graphicalController;

import Exceptions.DbProblemEception;
import beans.CanaleBean;
import beans.CanalePrivatoBean;
import beans.MessaggioBean;
import beans.ProgettoBean;
import com.acidmanic.consoletools.drawing.AsciiBorders;
import com.acidmanic.consoletools.table.Table;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import controllers.LavoratoreController;
import dao.LavoratoreDao;
import enge.LavoratoreSIngleton;
import models.Canale;
import models.CanalePrivato;
import models.Lavoratore;
import utils.PrinterCostum;

import java.sql.SQLException;
import java.util.Objects;

public class LavoratoreGraphicalController implements Runnable{
    @Override
    public void run() {
        LavoratoreController controller = new LavoratoreController();
        ProgettoBean[] beansProgetti;
        while(true){
            try {
                beansProgetti = controller.getProgetti();
            } catch (DbProblemEception e) {
                System.out.println(e.getMessage());
                return;
            }
            int dim= beansProgetti.length;
            if(dim==0){
                System.out.println("Non partecipi a nessun progetto");
                PrinterCostum.clearConsole(2);
                return;
            }
            TablePrinter.progettiLavoratoriPrinter(beansProgetti, LavoratoreSIngleton.getLavoratore().getCf());
            int scelta;
            while(true){
                System.out.println("Inserire l'indice del progetto che si vuole consultrare(-1 per uscire):");
                scelta = Integer.parseInt(PrinterCostum.getString());
                if(scelta==-1)return;
                else if (scelta<-1 || scelta>dim || scelta==0)continue;
                break;
            }
            progettoGuiSelector(beansProgetti[scelta-1]);
        }
    }

    private void progettoGuiSelector(ProgettoBean bean){
        if(Objects.equals(bean.getCfCapo(), LavoratoreSIngleton.getLavoratore().getCf())){
            capoProgettoGUI(bean);
        }else{
            lavoratoreGUi(bean);
        }
    }


    private void lavoratoreGUi(ProgettoBean bean){
        PrinterCostum.clearConsole(0);
        LavoratoreController controller=new LavoratoreController();
        while (true){
            CanaleBean[] canali;
            try {
                canali = controller.getCanaliPubliciLavoratore(bean);
            } catch (DbProblemEception e) {
                System.out.println(e.getMessage());
                return;
            }
            int dim=canali.length;
            if(dim==0){
                System.out.println("Non partecipi a nessun Canale");
                PrinterCostum.clearConsole(2);
                return;
            }
            TablePrinter.canaliPrinter(canali);
            int scelta;
            while(true){
                System.out.println("Inserire l'indice del canale di cui si vogliono leggere i messaggi(-1 per uscire):");
                scelta = Integer.parseInt(PrinterCostum.getString());
                if(scelta==-1)return;
                else if (scelta<-1 || scelta>dim || scelta==0)continue;
                menuCanaleLavoratore(canali[scelta-1]);
                break;
            }
        }
    }

    private void renderCanaliPrivatiLavoratore(CanaleBean canaleBean)   {
        PrinterCostum.clearConsole(0);
        assert LavoratoreSIngleton.getLavoratore() != null;
        String cf=LavoratoreSIngleton.getLavoratore().getCf();
        LavoratoreController controller=new LavoratoreController();
        CanalePrivatoBean[] beans;
        try {
            beans= controller.getCanaliPrivati(canaleBean,cf);
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            PrinterCostum.clearConsole(1);
            return;
        }
        TablePrinter.stampaCanaliPrivati(beans);
        System.out.println("Scegliere il canale che si vuole consultare oppure digitare 0 per usicre");
        int scelta=PrinterCostum.getSceltaInRange(0,beans.length);
        if(scelta==0) return;
        CanalePrivatoBean scleto=beans[scelta-1];
        while(true){
            PrinterCostum.clearConsole(0);
            renderCanalePrivatoMenu(scleto);
            scelta=PrinterCostum.getSceltaInRange(1,3);
            switch (scelta){
                case 1:
                    renderMenuMessaggi(scleto);
                case 2:
                    break;
                case 3:
                    PrinterCostum.clearConsole(0);
                    return;
                default:
                    continue;
            }
        }


    }

    private void menuCanaleLavoratore(CanaleBean canaleBean){
        PrinterCostum.clearConsole(0);
            while(true){
                renderCanaleMenu(canaleBean);
                int scelta;
                do {
                    scelta = Integer.parseInt(PrinterCostum.getString());
                }while (scelta<=0 || scelta >4);
                switch (scelta){
                    case 1:
                        renderMenuMessaggi(canaleBean);
                        break;
                    case 2:
                        renderCanaliPrivatiLavoratore(canaleBean);
                        break;
                    case 3:
                        break;
                    case 4:
                        PrinterCostum.clearConsole(0);
                        return;
                    default:continue;
                }
            }
    }
    private void renderMenuMessaggi(CanaleBean canaleBean){
        Table table = new TableBuilder()
                .tableBorder(AsciiBorders.DOUBLELINE)
                .cell((TableBuilder builder) -> builder
                        .row().cell("Opearzioni Disponibili:")
                        .row().cell("   1. Leggi Messaggi successivi")
                        .row().cell("   2. Rispondi ad un messaggio")
                        .row().cell("   3. Esci")


                ).border()
                .build();

        MessaggioBean[] messaggi;

        try {
            messaggi=leggiPrimiMessaggi(canaleBean);
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            PrinterCostum.clearConsole(1);
            return;
        }

        while (true){
            if (messaggi.length==0){
                System.out.println("Non ci sono altri messaggi");
                PrinterCostum.clearConsole(2);
                return;
            }
            PrinterCostum.clearConsole(0);
            TablePrinter.messaggiPrinter(messaggi);
            System.out.println(table.render());
            int scelta=PrinterCostum.getSceltaInRange(1,3);
            switch (scelta){
                case 1:
                    try {
                        messaggi=leggiMessaggiSuccessivi(messaggi[messaggi.length-1]);
                    } catch (DbProblemEception e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    break;
                case 2:
                    if(Objects.equals(canaleBean.getTipo(), "privato")){
                        System.out.println("non puoi rispondere a messaggi di canali privati");
                        continue;
                    }
                    break;
                case 3:
                    PrinterCostum.clearConsole(0);
                    return;
                default:
                    continue;
            }
        }


    }
    private MessaggioBean[] leggiMessaggiSuccessivi(MessaggioBean ultimoMessaggio) throws DbProblemEception {
        LavoratoreController controller =new LavoratoreController();
        return controller.getMessaggiPrecedenti(ultimoMessaggio.getIdPrecedente());

    }
    private MessaggioBean[] leggiPrimiMessaggi(CanaleBean bean) throws DbProblemEception {
        LavoratoreController controller=new LavoratoreController();
            return controller.getMessaggi(bean);

    }
    private void renderCanaleMenu(CanaleBean bean){

            Table table = new TableBuilder()
                    .tableBorder(AsciiBorders.DOUBLELINE)
                    .cell(
                            (TableBuilder builder) -> builder.cell("Canale "+bean.getNome()+" del progetto "+bean.getProgetto())).border(AsciiBorders.BOLD)
                    .row().cell((TableBuilder builder) -> builder
                            .row().cell("Opearzioni Disponibili:")
                            .row().cell("   1. Leggi Messaggi al interno del canale")
                            .row().cell("   2. Consulta canali privati nati al interno di questo canale")
                            .row().cell("   3. Scrivi un messaggio")
                            .row().cell("   4. Esci")


                    ).border()
                    .build();
            System.out.println(table.render());
             System.out.println("Scelta:");
    }

    private void renderCanalePrivatoMenu(CanalePrivatoBean bean){
        Table table = new TableBuilder()
                .tableBorder(AsciiBorders.DOUBLELINE)
                .cell(
                        (TableBuilder builder) -> builder.cell("Canale privato "+"Partecipanti: "+bean.getPartecipanti()[0].getNome()+" "+bean.getPartecipanti()[0].getCognome()+" | "+bean.getPartecipanti()[1].getNome()+" "+bean.getPartecipanti()[1].getCognome())).border(AsciiBorders.BOLD)
                .row().cell((TableBuilder builder) -> builder
                        .row().cell("Opearzioni Disponibili:")
                        .row().cell("   1. Leggi Messaggi al interno del canale")
                        .row().cell("   2. Scrivi un messaggio")
                        .row().cell("   3. Esci")


                ).border()
                .build();
        System.out.println(table.render());
    }

    private void capoProgettoGUI(ProgettoBean bean){
            System.out.println("sei scemo");
    }

}
