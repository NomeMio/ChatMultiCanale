package graphicalController;

import Exceptions.DbProblemEception;
import Exceptions.TabellaFormattataMaleException;
import beans.ProgettoBean;
import com.acidmanic.consoletools.drawing.AsciiBorders;
import com.acidmanic.consoletools.table.Cell;
import com.acidmanic.consoletools.table.Row;
import com.acidmanic.consoletools.table.Table;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import controllers.AmministratoreController;
import controllers.SessionManager;
import utils.PrinterCostum;

import java.lang.invoke.SwitchPoint;

public class AmministratoreGraphicalController implements Runnable {

    @Override
    public void run() {
        AmministratoreController controller = new AmministratoreController();
        while(true) {
            renderMenu();
            System.out.print("Scegliere operazione: ");
            String scelta = PrinterCostum.getString();
            switch (scelta) {
                case "3":
                    SessionManager controllerSessione=new SessionManager();
                    controllerSessione.logOut();
                    PrinterCostum.clearConsole(0);
                    System.out.println("Arrivederci!");
                    PrinterCostum.clearConsole(2);
                    return;
                case "1":
                    mostraProgettiLoop();
                    break;
                default:
                    continue;

            }
        }

    }
    //protected--> @test
    protected void mostraProgettiLoop(){
        PrinterCostum.clearConsole(0);
        AmministratoreController controller = new AmministratoreController();
        try {
            ProgettoBean[] progetti= controller.getProgetti();
            try {
                PrinterCostum.stampaTabella(convertiToMatrix(progetti));
            } catch (TabellaFormattataMaleException e) {
                System.out.println(e.getMessage());

            }
        } catch (DbProblemEception e) {
            System.out.println("Errore di connessione alla base di dati");
            return;
        }
        System.out.println("Premi invio per tornare al menu");
        PrinterCostum.getString();


    }

    private String[][] convertiToMatrix(ProgettoBean[] progetti){
            int dim=progetti.length;
            String[] nomiP,dataP,cfC,nomeC,cognomeC;
            nomiP=new String[dim];dataP=new String[dim];cfC=new String[dim];nomeC=new String[dim];cognomeC=new String[dim];
            System.out.println(progetti[0].getNome());
            nomiP[0]="Nome Progetto";
            dataP[0]="Data di creazione";
            cfC[0]="Cf Capo progetto";
            nomeC[0]="Nome Capo progetto";
            cognomeC[0]="Cognome Capo Progetto";
            for(int i=0;i<dim;i++){
                nomiP[i+1]=progetti[i].getNome();
                dataP[i+1]=progetti[i].getData();
                nomeC[i+1]=progetti[i].getNomeCapo();
                cfC[i+1]=progetti[i].getCfCapo();
                cognomeC[i+1]=progetti[i].getCognomeCapo();
                System.out.println(progetti[i].getNome()+" "+progetti[i].getData()+" "+progetti[i].getCfCapo()+" "+progetti[i].getNomeCapo());
            }

            return new String[][]{nomiP, dataP, cfC, nomeC, cognomeC};
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
