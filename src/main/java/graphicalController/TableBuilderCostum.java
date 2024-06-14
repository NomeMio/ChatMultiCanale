package graphicalController;

import com.acidmanic.consoletools.table.Cell;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import com.acidmanic.consoletools.table.builders.TableBuilderConsumer;

public class TableBuilderCostum extends TableBuilder {
    public TableBuilderCostum() {
        super();
    }
    private static int maxChar=20;
    public static void setMaxChar(int a){
        maxChar=a;
    }
    @Override
    public TableBuilder cell(String text) {
        Cell cella=new Cell(text);
        cella.setMaximumWidth(maxChar);
        return cell(cella);
    }
    @Override
    public TableBuilder cell(TableBuilderConsumer build) {
        TableBuilderCostum builder = new TableBuilderCostum();
        build.accept(builder);
        cell(builder.build());
        return this;
    }
}
