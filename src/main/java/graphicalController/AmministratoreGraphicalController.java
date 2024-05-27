package graphicalController;

import Exceptions.DbProblemEception;
import beans.CandidatiCapoProgettoBean;
import beans.ProgettoBean;
import com.acidmanic.consoletools.drawing.AsciiBorders;
import com.acidmanic.consoletools.table.Table;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import controllers.AmministratoreController;
import controllers.SessionManager;
import utils.PrinterCostum;

import java.sql.SQLException;

public class AmministratoreGraphicalController implements Runnable {

    @Override
    public void run() {
        AmministratoreController controller = new AmministratoreController();
        while(true) {
            renderMenu();
            System.out.print("Scegliere operazione: ");
            String scelta = PrinterCostum.getString();
            switch (scelta) {
                //Caso uscita dal applicazione
                case "3":
                    SessionManager controllerSessione=new SessionManager();
                    controllerSessione.logOut();
                    PrinterCostum.clearConsole(0);
                    System.out.println("Arrivederci!");
                    PrinterCostum.clearConsole(2);
                    return;
                    //Caso stama lista progetti
                case "1":
                    mostraProgettiLoop();
                    break;
                case "2":
                    selezionaCapiProgettoLoop();
                    break;
                default:
                    continue;

            }
            PrinterCostum.clearConsole(0);

        }

    }
    //protected--> @test
    protected void mostraProgettiLoop(){
        PrinterCostum.clearConsole(0);
        AmministratoreController controller = new AmministratoreController();
        ProgettoBean[] progetti;
        try {
             progetti= controller.getProgetti();

        } catch (DbProblemEception e) {
            System.out.println("Errore di connessione alla base di dati");
            return;
        }
        TablePrinter.proggettiPrinter(progetti,true);
        System.out.println("Premi invio per tornare al menu");
        PrinterCostum.getString();
        PrinterCostum.clearConsole(0);


    }

    protected void selezionaCapiProgettoLoop() {
        PrinterCostum.clearConsole(0);
        AmministratoreController controller = new AmministratoreController();
        ProgettoBean[] progetti;
        try {
            progetti = controller.getProgettiSenzaCapo();

        } catch (DbProblemEception e) {
            System.out.println("Errore di connessione alla base di dati");
            return;
        }
        int dim = progetti.length;
        TablePrinter.proggettiPrinter(progetti, false);
        int scelta;
        while (true) {
            scelta = -1;
            System.out.println("Inserire l'indice del progetto che in cui si vuole inserire un nuovo capo progetto:");
            scelta = Integer.parseInt(PrinterCostum.getString());
            if (scelta > dim || scelta <= 0) continue;
            else break;
        }
        PrinterCostum.clearConsole(0);
        CandidatiCapoProgettoBean[] candidati;
        try {
            candidati = controller.getCandidati(progetti[scelta]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TablePrinter.candidatiPrinter(candidati);

    }


    private void renderMenu() {
        AmministratoreController controller = new AmministratoreController();

        Table table = new TableBuilder()
                .tableBorder(AsciiBorders.DOUBLELINE)
                .cell(
                        (TableBuilder builder) -> builder.cell("Panello amministratore")
                                .cell("ID: " + controller.getAmCf()).padding(3, 0)
                ).border(AsciiBorders.BOLD)
                .row().cell((TableBuilder builder) -> builder
                            .row().cell("Opearzioni Disponibili:")
                            .row().cell("   1. Visualizza lista progetti")
                            .row().cell("   2. Assegna Capo Progetto a progetto scoperto")
                            .row().cell("   3. Esci")

                ).border()
                .build();
        System.out.println(table.render());
    }


}
