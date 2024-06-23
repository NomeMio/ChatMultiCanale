package graphicalController;

import Exceptions.DbProblemEception;
import beans.*;
import com.acidmanic.consoletools.drawing.AsciiBorders;
import com.acidmanic.consoletools.table.Table;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import controllers.CapoProgettoController;
import controllers.LavoratoreController;

import utils.PrinterCostum;
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
            TablePrinter.progettiLavoratoriPrinter(beansProgetti, controller.getLavCf());
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
        if(Objects.equals(bean.getCfCapo(),new LavoratoreController().getLavCf())){
            capoProgettoGUI(bean);
        }else{
            lavoratoreGUi(bean);
        }
    }


    private CanaleBean[] printCanaliPublici(ProgettoBean bean) throws DbProblemEception {
        LavoratoreController controller=new LavoratoreController();
        CanaleBean[] canali;
        canali = controller.getCanaliPubliciLavoratore(bean);
        int dim=canali.length;
        TablePrinter.canaliPrinter(canali);
        return canali;
    }

    private CanaleBean scegliCanalePublico(CanaleBean[] bean){
        int size=bean.length;
        System.out.println("Inserire il numero del canale che si vuole consultrare oppure 0 per uscire");
        int scelta=PrinterCostum.getSceltaInRange(0,size);
        return scelta==0?null:bean[scelta-1];
    }

    private void lavoratoreGUi(ProgettoBean bean){
        PrinterCostum.clearConsole(0);

        while (true){
            CanaleBean[] canali= null;
            try {
                canali = printCanaliPublici(bean);
            } catch (DbProblemEception e) {
                System.out.println(e.getMessage());
                return;
            }
            if(canali.length==0){
                System.out.println("Non partecipi a nessun canale");
                return;
            }
            CanaleBean canale=scegliCanalePublico(canali);
            if(canale==null)return;
            menuCanaleLavoratore(canale);
        }
    }

    private void inserisciMessaggioMenu(CanaleBean canale,MessaggioBean messaggioRisposta){
        PrinterCostum.clearConsole(0);
        System.out.println("Inserisci il messaggio da inserire: ");
        String testo=PrinterCostum.getString();
        LavoratoreController controller=new LavoratoreController();
        try {
            if(messaggioRisposta==null) {
                controller.inserisciMessaggio(new MessaggioBean(testo, canale.getNome(), canale.getProgetto()));
            }else{
                controller.inserisciMessaggio(new MessaggioBean(testo, canale.getNome(), canale.getProgetto()),messaggioRisposta);
            }
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Messaggio inserito con successo");
        PrinterCostum.clearConsole(1);
        return;

    }

    private void renderCanaliPrivatiLavoratore(CanaleBean canaleBean)   {
        PrinterCostum.clearConsole(0);

        LavoratoreController controller=new LavoratoreController();
        CanalePrivatoBean[] beans;
        try {
            beans= controller.getCanaliPrivati(canaleBean, controller.getLavCf());
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            PrinterCostum.clearConsole(1);
            return;
        }
        renderCanaliPrivati(beans);


    }


    private void renderCanaliPrivati(CanalePrivatoBean[] beans){
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
                    break;
                case 2:
                    inserisciMessaggioMenu(scleto,null);
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
                        inserisciMessaggioMenu(canaleBean,null);
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
            PrinterCostum.clearConsole(1);
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
                    renderRispostaMenu(canaleBean,messaggi);
                    try {
                        messaggi=leggiPrimiMessaggi(canaleBean);
                    } catch (DbProblemEception e) {
                        System.out.println(e.getMessage());
                        PrinterCostum.clearConsole(1);
                        return;
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
        return controller.getMessaggiPrecedenti(ultimoMessaggio);

    }
    private MessaggioBean[] leggiPrimiMessaggi(CanaleBean bean) throws DbProblemEception {
        LavoratoreController controller=new LavoratoreController();
            return controller.getMessaggi(bean);

    }

    private void renderRispostaMenu(CanaleBean canale,MessaggioBean[] messaggioBeans){
        System.out.println("Scegliere uno dei messaggi presenti nel elenco precedente:");
        int scelta=PrinterCostum.getSceltaInRange(1,messaggioBeans.length);
        MessaggioBean messaggioScelto=messaggioBeans[scelta-1];

        Table table = new TableBuilder()
                .tableBorder(AsciiBorders.DOUBLELINE)
                .cell((TableBuilder builder) -> builder
                        .row().cell("Opearzioni Disponibili:")
                        .row().cell("   1. Rispondi publicamente con un messaggio")
                        .row().cell("   2. Crea un canale Privato per questo messaggio")
                        .row().cell("   3. Esci")


                ).border()
                .build();
        System.out.println(table.render());
        scelta=PrinterCostum.getSceltaInRange(1,3);
        switch (scelta){
            case 1:
                inserisciMessaggioMenu(canale,messaggioScelto);
                break;
            case 2:
                if(Objects.equals(canale.getTipo(), "privato")){
                    System.out.println("non puoi rispondere a messaggi di canali privati");
                    return;
                }
                creaCanalePrivato(messaggioScelto);
                break;
            case 3:
                return;
            default:
                return;
        }
    }
    private void creaCanalePrivato(MessaggioBean bean){
        try{
            LavoratoreController controller=new LavoratoreController();
            controller.creaNuovoCanalePrivato(bean);
        }catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Canale Privato creato con successo");
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

    /// Sezione capo progetto

    private void renderCanaleMenuCapo(CanaleBean bean){

        Table table = new TableBuilder()
                .tableBorder(AsciiBorders.DOUBLELINE)
                .cell(
                        (TableBuilder builder) -> builder.cell("Canale "+bean.getNome()+" del progetto "+bean.getProgetto())).border(AsciiBorders.BOLD)
                .row().cell((TableBuilder builder) -> builder
                        .row().cell("Opearzioni Disponibili:")
                        .row().cell("   1. Leggi Messaggi al interno del canale")
                        .row().cell("   2. Consulta canali privati nati al interno di questo canale")
                        .row().cell("   3. Scrivi un messaggio")
                        .row().cell("   4. Aggiungi lavoratore al canale")
                        .row().cell("   5. Esci")


                ).border()
                .build();
        System.out.println(table.render());
    }

    private void renderProgettoMenuCapo(ProgettoBean bean){
        Table table = new TableBuilder()
                .tableBorder(AsciiBorders.DOUBLELINE)
                .cell(
                        (TableBuilder builder) -> builder.cell("Progetto "+bean.getNome())).border(AsciiBorders.BOLD)
                .row().cell((TableBuilder builder) -> builder
                        .row().cell("Opearzioni Disponibili:")
                        .row().cell("   1. Visualizza canali")
                        .row().cell("   2. Crea nuovo canale")
                        .row().cell("   3. Esci")


                ).border()
                .build();
        System.out.println(table.render());
    }


    private void renderCanaliPubliciCapo(ProgettoBean bean){
        PrinterCostum.clearConsole(0);
        while (true){
            CanaleBean[] canali;
            try {
                canali = printCanaliPublici(bean);
            } catch (DbProblemEception e) {
                System.out.println(e.getMessage());
                return;
            }
            if(canali.length==0){
                System.out.println("Non partecipi a nessun canale");
                return;
            }
            CanaleBean canale=scegliCanalePublico(canali);
            if(canale==null)return;
            menuCanaleCAPO(bean,canale);
        }
    }


    private void menuAggiungiLavoratoreAlCanale(ProgettoBean progettoBean,CanaleBean canaleBean){
            PrinterCostum.clearConsole(0);
            CandidatiCanale[] candidatiCanales;
            CapoProgettoController controller;
            try{
                controller = new CapoProgettoController(progettoBean);
                candidatiCanales=controller.getCanditaiPerCanali(canaleBean);
            } catch (DbProblemEception e) {
                System.out.println(e.getMessage());
                return;
            }
            TablePrinter.printerCandidatiCanale(candidatiCanales);
            System.out.println("Scelgliere un lacoratore( oppure 0 per uscire)");
            int scelta=PrinterCostum.getSceltaInRange(0,candidatiCanales.length);
        try {
            controller.inserisciLavoratoreNelCanale(canaleBean,candidatiCanales[scelta]);
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Inserito con successo");
    }

    private void menuCanaleCAPO(ProgettoBean progettoBean, CanaleBean canaleBean) {
        while (true){
            PrinterCostum.clearConsole(1);
            renderCanaleMenuCapo(canaleBean);
            int scelta=PrinterCostum.getSceltaInRange(1,4);
            switch (scelta){
                case 1:
                    renderMenuMessaggi(canaleBean);
                    break;
                case 2:
                    renderMenuCanaliPrivatiCapo(progettoBean,canaleBean);
                    break;
                case 3:
                    inserisciMessaggioMenu(canaleBean,null);
                    break;
                case 4:
                    menuAggiungiLavoratoreAlCanale(progettoBean,canaleBean);
                    return;
                case 5:
                    return;
            }
        }
    }

    private void renderMenuCanaliPrivatiCapo(ProgettoBean progettoBean, CanaleBean canaleBean){
        PrinterCostum.clearConsole(0);
        String cf=new LavoratoreController().getLavCf();
        CapoProgettoController controller= null;
        try {
            controller = new CapoProgettoController(progettoBean);
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            return;
        }
        CanalePrivatoBean[] beans;
        try {
            beans= controller.getCanaliPrivati(canaleBean,cf);
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            PrinterCostum.clearConsole(1);
            return;
        }
        renderCanaliPrivati(beans);

    }

    private void inserisciNuovoCanalePublico(ProgettoBean bean){
        PrinterCostum.clearConsole(0);
        System.out.println("Inserisci il nome del canale che desideri creare :");
        String nome=PrinterCostum.getString();
        CanaleBean canale=new CanaleBean(nome, bean.getNome(), null,null);
        try {
            CapoProgettoController controller=new CapoProgettoController(bean);
            controller.inserisciNuovoCanale(canale);
        } catch (DbProblemEception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Canale creato con successo");

    }

    private void capoProgettoGUI(ProgettoBean bean){
            while (true){
                PrinterCostum.clearConsole(1);
                renderProgettoMenuCapo(bean);
                int scelta=PrinterCostum.getSceltaInRange(1,3);
                switch (scelta){
                    case 1:
                            renderCanaliPubliciCapo(bean);
                        break;
                    case 2:
                        inserisciNuovoCanalePublico(bean);
                        break;
                    case 3:
                        return;
                    default:
                }
            }
    }

}
