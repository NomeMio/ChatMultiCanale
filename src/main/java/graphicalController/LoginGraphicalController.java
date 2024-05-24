package graphicalController;

import Exceptions.LoginFallitoException;
import beans.CredentialBean;
import controllers.SessionManager;
import utils.PrinterCostum;

import java.sql.SQLException;

public class LoginGraphicalController implements Runnable{
    @Override
    public void run() {
        System.out.println("              Login");
        System.out.print("Username:");
        String username=PrinterCostum.getString();
        System.out.print("Password:");
        String password=PrinterCostum.getString();
        SessionManager controller=new SessionManager();
        try {
            controller.login(new CredentialBean(password,username));
        } catch (LoginFallitoException e) {
            System.out.println("Login fallito, ceredenziali errate.");
        } catch (SQLException e) {
            System.out.println("Errore con il database.");
        }
    }
}
