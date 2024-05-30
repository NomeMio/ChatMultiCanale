package graphicalController;

import Exceptions.DbProblemEception;
import beans.CanaleBean;
import beans.ProgettoBean;
import controllers.LavoratoreController;
import enge.LavoratoreSIngleton;
import utils.PrinterCostum;

import java.util.Objects;

public class LavoratoreGraphicalController implements Runnable{
    @Override
    public void run() {
        LavoratoreController controller = new LavoratoreController();
        ProgettoBean[] beansProgetti;
        while(true){
            try {
                beansProgetti = controller.getProgetti();
            } catch (DbProblemEception e) {
                System.out.println(e.getMessage());
                return;
            }
            int dim= beansProgetti.length;
            TablePrinter.progettiLavoratoriPrinter(beansProgetti, LavoratoreSIngleton.getLavoratore().getCf());
            int scelta;
            while(true){
                System.out.println("Inserire l'indice del progetto che si vuole consultrare(-1 per uscire):");
                scelta = Integer.parseInt(PrinterCostum.getString());
                if(scelta==-1)return;
                else if (scelta<-1 || scelta>dim || scelta==0)continue;
                break;
            }
            progettoGuiSelector(beansProgetti[scelta-1]);
            return;
        }
    }

    private void progettoGuiSelector(ProgettoBean bean){
        if(Objects.equals(bean.getCfCapo(), LavoratoreSIngleton.getLavoratore().getCf())){
            capoProgettoGUI(bean);
        }else{
            lavoratoreGUi(bean);
        }
    }


    private void lavoratoreGUi(ProgettoBean bean){
        LavoratoreController controller=new LavoratoreController();
        while (true){
            CanaleBean[] canali;
            try {
                canali = controller.getCanaliLavoratore(bean);
            } catch (DbProblemEception e) {
                System.out.println(e.getMessage());
                return;
            }
            int dim=canali.length;
            TablePrinter.canaliPrinter(canali);
            int scelta;
            while(true){
                System.out.println("Inserire l'indice del canale di cui si vogliono leggere i messaggi(-1 per uscire):");
                scelta = Integer.parseInt(PrinterCostum.getString());
                if(scelta==-1)return;
                else if (scelta<-1 || scelta>dim || scelta==0)continue;
                break;
            }
        }
    }
    private void capoProgettoGUI(ProgettoBean bean){
            System.out.println("sei scemo");
    }

}
