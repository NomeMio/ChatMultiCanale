package enge;

import models.Amministratore;
import models.Lavoratore;

public class LavoratoreSIngleton {
    private static LavoratoreSIngleton sing;
    private Lavoratore me;
    private LavoratoreSIngleton(Lavoratore a){
        me=a;
    }
    public static void createSingleton(Lavoratore a){
        if(sing==null) sing=new LavoratoreSIngleton(a);
    }
    public static Lavoratore getLavoratore(){
        return sing==null?null:sing.me;
    }
}
