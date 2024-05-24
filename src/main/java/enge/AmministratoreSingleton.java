package enge;

import models.Amministratore;

public class AmministratoreSingleton{
    private static AmministratoreSingleton sing;
    private Amministratore me;
    private AmministratoreSingleton(Amministratore a){
        me=a;
    }
    public static void createSingleton(Amministratore a){
        if(sing==null) sing=new AmministratoreSingleton(a);
    }
    public static Amministratore getAmministrator(){
        return sing==null?null:sing.me;
    }
    public static void deleteSingelton(){
        sing=null;
    }
}
