package controllers;

import enge.AmministratoreSingleton;
import models.Amministratore;

public class AmministratoreController {
        public String getAmCf(){
            Amministratore am=AmministratoreSingleton.getAmministrator();
            return am!=null?am.getCf():null;
        }
}
