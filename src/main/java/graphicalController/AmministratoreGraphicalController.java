package graphicalController;

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
    AmministratoreController controller;

    @Override
    public void run() {
        controller = new AmministratoreController();
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
                    mostraProgetti();
                    break;
                default:
                    continue;

            }
        }

    }

    private void mostraProgetti(){
        Table outerTable = new Table();

        outerTable.setBorder(AsciiBorders.DOUBLELINE);

        /* Adding a table as a cell */
        Table innerTable = new Table();
        innerTable.getRows().add(new Row());
        innerTable.getRows().add(new Row());
        innerTable.getRows().get(0).getCells().add(new Cell("Inner00"));
        innerTable.getRows().get(0).getCells().add(new Cell("Inner01"));
        innerTable.getRows().get(1).getCells().add(new Cell("Inner10"));
        innerTable.getRows().get(1).getCells().add(new Cell("Inner11"));

        innerTable.setCellsBorders(AsciiBorders.SOLID);
        innerTable.setBorder(AsciiBorders.SOLID);



        System.out.println(innerTable.render());

        Row row = new Row();

        /* Add a simple cell first */
        row.getCells().add(new Cell("A Cell At Left"));

        /* Add innerTable as another cell beside the former */
        row.getCells().add(innerTable);

        outerTable.getRows().add(row);


        System.out.println(outerTable.render());
    }

    private void renderMenu() {
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
