import beans.ProgettoBean;
import com.acidmanic.consoletools.drawing.AsciiBorder;
import com.acidmanic.consoletools.drawing.AsciiBorders;
import com.acidmanic.consoletools.rendering.componentfeatures.Renderable;
import com.acidmanic.consoletools.table.Table;
import com.acidmanic.consoletools.table.TableRenderable;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import dao.AmministratorDao;
import dao.dbInteraction.ConnectionSIngleton;
import dao.dbInteraction.PermessiEnum;
import graphicalController.AmministratoreGraphicalController;
import org.junit.Test;
import utils.textArt.BordersAsciTableNEw;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

public class TestStoredProceduresAmministratore extends AmministratoreGraphicalController{
    @Test
    public void printListProgetti() throws SQLException {
        ConnectionSIngleton.changePermissionLevel(PermessiEnum.AMMINISTRATORE);
        this.mostraProgettiLoop();
    }
    @Test
    public void printCandidates() throws SQLException {
        ConnectionSIngleton.changePermissionLevel(PermessiEnum.AMMINISTRATORE);
        AmministratorDao dao=new AmministratorDao();
        System.out.println(dao.listaCandidatiPerCapoProgetto(new ProgettoBean("Bomboclat","","","","")).get(1).getName());
    }
}
