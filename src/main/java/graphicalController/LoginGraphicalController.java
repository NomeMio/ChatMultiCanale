package graphicalController;

import Exceptions.LoginFallitoException;
import beans.CredentialBean;
import beans.UserBean;
import controllers.SessionManager;
import models.Utente;
import utils.PrinterCostum;

import java.sql.SQLException;

public class LoginGraphicalController implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("              Login");
            System.out.print("Username:");
            String username = PrinterCostum.getString();
            System.out.print("Password:");
            String password = PrinterCostum.getString();
            SessionManager controller = new SessionManager();
            UserBean utente;
            try {
                utente=controller.login(new CredentialBean(password, username));
                PrinterCostum.clearConsole(0);
                System.out.println("Hai eseguito l'acesso come "+utente.getRuolo()+" : "+utente.getNome()+" "+utente.getCognome());
                PrinterCostum.clearConsole(2);
                switch (controller.getRole()){
                    case Amministratore -> {
                        new Thread(new AmministratoreGraphicalController()).start();
                    }
                    case Lavoratore -> {
                        new Thread(new LavoratoreGraphicalController()).start();
                    }
                    default -> {
                        return;
                    }
                }
                return;
            } catch (LoginFallitoException e) {
                PrinterCostum.clearConsole(0);
                System.out.println("Login fallito, ceredenziali errate.");
            } catch (SQLException e) {
                System.out.println("Errore con il database.");
                return;
            }

        }
    }
}
