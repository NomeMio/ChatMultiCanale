package graphicalController;

import Exceptions.TabellaFormattataMaleException;
import beans.CanaleBean;
import beans.CandidatiCapoProgettoBean;
import beans.ProgettoBean;
import utils.PrinterCostum;

import java.util.Objects;

public class TablePrinter {
    public static void proggettiPrinter(ProgettoBean[] progetti, boolean conCapi){
        try {
            PrinterCostum.stampaTabella(convertiToMatrixProgetti(progetti,conCapi));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }

    public static void  candidatiPrinter(CandidatiCapoProgettoBean[] candidati){
        try {
            PrinterCostum.stampaTabella(convertiToMatrixCandidati(candidati));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }

    private static String[][] convertiToMatrixCandidati(CandidatiCapoProgettoBean[] candidati){
        int dim=candidati.length+1;
        String[] nomiL,cognomeL,indice,numeroProgetti,cfL;
        nomiL=new String[dim];cognomeL=new String[dim];numeroProgetti=new String[dim];cfL=new String[dim];
        indice=new String[dim];
        nomiL[0]="Nome";
        cognomeL[0]="Cognome";
        cfL[0]="cf";
        indice[0]="indice";
        numeroProgetti[0]="Numero progetti gestiti";
        for(int i=0;i<dim-1;i++){
            indice[i+1]=String.valueOf(i+1);
            System.out.println(candidati[i].getName());
            nomiL[i+1]=candidati[i].getName();
            cognomeL[i+1]=candidati[i].getCognome();
            cfL[i+1]=candidati[i].getCf();
            numeroProgetti[i+1]=candidati[i].getNumeroPromozioni();
        }
        return new String[][]{indice,cfL,nomiL,cognomeL,numeroProgetti};

    }
    private static String[][] convertiToMatrixProgetti(ProgettoBean[] progetti, boolean conCapi){
        int dim=progetti.length+1;
        String[] nomiP,dataP,cfC,nomeC,cognomeC,indice;
        nomiP=new String[dim];dataP=new String[dim];cfC=new String[dim];nomeC=new String[dim];cognomeC=new String[dim];
        indice=new String[dim];
        System.out.println(progetti[0].getNome());
        nomiP[0]="Nome Progetto";
        dataP[0]="Data di creazione";
        cfC[0]="Cf Capo progetto";
        nomeC[0]="Nome Capo progetto";
        cognomeC[0]="Cognome Capo Progetto";
        indice[0]="indice";
        for(int i=0;i<dim-1;i++){
            indice[i+1]=String.valueOf(i+1);
            nomiP[i+1]=progetti[i].getNome();
            dataP[i+1]=progetti[i].getData();
            nomeC[i+1]=progetti[i].getNomeCapo();
            cfC[i+1]=progetti[i].getCfCapo();
            cognomeC[i+1]=progetti[i].getCognomeCapo();
        }

        if(conCapi)return new String[][]{indice,nomiP, dataP, cfC, nomeC, cognomeC};
        return new String[][]{indice,nomiP,dataP};
    }


    public static void  progettiLavoratoriPrinter(ProgettoBean[] progetti,String cfL){
        try {
            PrinterCostum.stampaTabella(convertiToMatrixProgettiLavoratori(progetti,cfL));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }
    private static String[][] convertiToMatrixProgettiLavoratori(ProgettoBean[] progetti,String cfL){
        int dim=progetti.length+1;
        String[] nomiP,dataP,seiCapo,indice;
        nomiP=new String[dim];dataP=new String[dim];seiCapo=new String[dim];
        indice=new String[dim];
        nomiP[0]="Nome Progetto";
        dataP[0]="Data di creazione";
        seiCapo[0]="Sei capo progetto di questo progetto?";
        indice[0]="indice";
        for(int i=0;i<dim-1;i++){
            indice[i+1]=String.valueOf(i+1);
            nomiP[i+1]=progetti[i].getNome();
            dataP[i+1]=progetti[i].getData();
            seiCapo[i+1]= Objects.equals(progetti[i].getCfCapo(), cfL) ?"Si":"No";
        }
        return new String[][]{indice,nomiP,dataP,seiCapo};
    }

    public static void canaliPrinter(CanaleBean[] beans){
        try {
            PrinterCostum.stampaTabella(convertiToMatricCanali(beans,false));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }

    private static String[][] convertiToMatricCanali(CanaleBean[] beans, boolean conPartecipazioen){
        int dim=beans.length+1;
        String[] nomiCa,dataCa,tipo,indice,partecipazione;
        nomiCa=new String[dim];dataCa=new String[dim];tipo=new String[dim];
        indice=new String[dim];partecipazione=new String[dim];
        nomiCa[0]="Nome Canale";
        dataCa[0]="Data di creazione";
        tipo[0]="Tipo del canale";
        indice[0]="indice";
        partecipazione[0]="Partecipi al canale?";
        for(int i=0;i<dim-1;i++){
            indice[i+1]=String.valueOf(i+1);
            nomiCa[i+1]=beans[i].getNome();
            dataCa[i+1]=beans[i].getData();
            tipo[i+1]= beans[i].getTipo();
            partecipazione[i]=beans[i].isPartecipazione()?"si":"no";
        }
        if(conPartecipazioen)return new String[][]{indice,nomiCa,dataCa};
        return new String[][]{indice,nomiCa,dataCa,partecipazione};
    }
}
