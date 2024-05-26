import com.acidmanic.consoletools.drawing.AsciiBorder;
import com.acidmanic.consoletools.drawing.AsciiBorders;
import com.acidmanic.consoletools.table.Table;
import com.acidmanic.consoletools.table.TableRenderable;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import dao.AmministratorDao;
import dao.dbInteraction.ConnectionSIngleton;
import dao.dbInteraction.PermessiEnum;
import org.junit.Test;
import utils.textArt.BordersAsciTableNEw;

import java.sql.SQLException;

public class TestStoredProceduresAmministratore {
    @Test
    public void printListProgetti() throws SQLException {
        ConnectionSIngleton.changePermissionLevel(PermessiEnum.AMMINISTRATORE);
        AmministratorDao.getDao().vediProgetti();
    }
    @Test
    public void testTableWithTableConstructor(){
        Table table = new TableBuilder()
                .cell( (TableBuilder builder) -> builder.row().cell("Nomi di cose").border(BordersAsciTableNEw.ONLYBOt).row().cell("kk").row().cell("dd")).border(BordersAsciTableNEw.OnlyLati)
                .cell( (TableBuilder builder) -> builder.row().cell("Nomiffffffffffffff di cose").border(BordersAsciTableNEw.ONLYBOt).row().cell((TableBuilder builder2) -> builder2.row().cell("testss").row().cell("asss")).border(BordersAsciTableNEw.OnlyLati))
                .build();
        System.out.println(table.render());
    }
}
