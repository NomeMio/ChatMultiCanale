package graphicalController;

import Exceptions.TabellaFormattataMaleException;
import beans.*;
import com.acidmanic.consoletools.drawing.AsciiBorders;
import com.acidmanic.consoletools.table.Cell;
import com.acidmanic.consoletools.table.builders.TableBuilder;
import utils.PrinterCostum;
import utils.textArt.BordersAsciTableNEw;

import java.util.Objects;

public class TablePrinter {
    public static void proggettiPrinter(ProgettoBean[] progetti, boolean conCapi) {
        try {
            PrinterCostum.stampaTabella(convertiToMatrixProgetti(progetti, conCapi));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }

    public static void candidatiPrinter(CandidatiCapoProgettoBean[] candidati) {
        try {
            PrinterCostum.stampaTabella(convertiToMatrixCandidati(candidati));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }

    private static String[][] convertiToMatrixCandidati(CandidatiCapoProgettoBean[] candidati) {
        int dim = candidati.length + 1;
        String[] nomiL, cognomeL, indice, numeroProgetti, cfL;
        nomiL = new String[dim];
        cognomeL = new String[dim];
        numeroProgetti = new String[dim];
        cfL = new String[dim];
        indice = new String[dim];
        nomiL[0] = "Nome";
        cognomeL[0] = "Cognome";
        cfL[0] = "cf";
        indice[0] = "indice";
        numeroProgetti[0] = "Numero progetti gestiti";
        for (int i = 0; i < dim - 1; i++) {
            indice[i + 1] = String.valueOf(i + 1);
            nomiL[i + 1] = candidati[i].getName();
            cognomeL[i + 1] = candidati[i].getCognome();
            cfL[i + 1] = candidati[i].getCf();
            numeroProgetti[i + 1] = candidati[i].getNumeroPromozioni();
        }
        return new String[][]{indice, cfL, nomiL, cognomeL, numeroProgetti};

    }

    private static String[][] convertiToMatrixProgetti(ProgettoBean[] progetti, boolean conCapi) {
        int dim = progetti.length + 1;
        String[] nomiP, dataP, cfC, nomeC, cognomeC, indice;
        nomiP = new String[dim];
        dataP = new String[dim];
        cfC = new String[dim];
        nomeC = new String[dim];
        cognomeC = new String[dim];
        indice = new String[dim];
        System.out.println(progetti[0].getNome());
        nomiP[0] = "Nome Progetto";
        dataP[0] = "Data di creazione";
        cfC[0] = "Cf Capo progetto";
        nomeC[0] = "Nome Capo progetto";
        cognomeC[0] = "Cognome Capo Progetto";
        indice[0] = "indice";
        for (int i = 0; i < dim - 1; i++) {
            indice[i + 1] = String.valueOf(i + 1);
            nomiP[i + 1] = progetti[i].getNome();
            dataP[i + 1] = progetti[i].getData();
            nomeC[i + 1] = progetti[i].getNomeCapo();
            cfC[i + 1] = progetti[i].getCfCapo();
            cognomeC[i + 1] = progetti[i].getCognomeCapo();
        }

        if (conCapi) return new String[][]{indice, nomiP, dataP, cfC, nomeC, cognomeC};
        return new String[][]{indice, nomiP, dataP};
    }


    public static void progettiLavoratoriPrinter(ProgettoBean[] progetti, String cfL) {
        try {
            PrinterCostum.stampaTabella(convertiToMatrixProgettiLavoratori(progetti, cfL));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }


    private static String[][] convertiToMatrixProgettiLavoratori(ProgettoBean[] progetti, String cfL) {
        int dim = progetti.length + 1;
        String[] nomiP, dataP, seiCapo, indice;
        nomiP = new String[dim];
        dataP = new String[dim];
        seiCapo = new String[dim];
        indice = new String[dim];
        nomiP[0] = "Nome Progetto";
        dataP[0] = "Data di creazione";
        seiCapo[0] = "Sei capo progetto di questo progetto?";
        indice[0] = "indice";
        for (int i = 0; i < dim - 1; i++) {
            indice[i + 1] = String.valueOf(i + 1);
            nomiP[i + 1] = progetti[i].getNome();
            dataP[i + 1] = progetti[i].getData();
            seiCapo[i + 1] = Objects.equals(progetti[i].getCfCapo(), cfL) ? "Si" : "No";
        }
        return new String[][]{indice, nomiP, dataP, seiCapo};
    }

    public static void canaliPrinter(CanaleBean[] beans) {
        try {
            PrinterCostum.stampaTabella(convertiToMatricCanali(beans, false));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }

    public static void messaggiPrinter(MessaggioBean[] messaggioBeans) {
        int i = 1;
        for (MessaggioBean messaggio : messaggioBeans) {

            TableBuilder tableBuilder = prepareMessageTable(messaggio);
            System.out.println((i++) + ")");
            System.out.println(tableBuilder.build().render());

        }
    }

    public static TableBuilder prepareMessageTable(MessaggioBean messaggio) {
        Cell cella=new Cell(messaggio.getTesto());
        cella.setMaximumWidth(35);
        TableBuilder table = new TableBuilderCostum().tableBorder(AsciiBorders.SOLID).cell(
                        (TableBuilder builder) -> builder.cell("Autore : " + messaggio.getNomeAutore() + " " + messaggio.getCognomeAutore())
                                .cell("Id messaggio: " + messaggio.getIdMess()).padding(3, 0)
                                .cell(messaggio.getTimestamp()).padding(3, 0)
                                .tableBorder(BordersAsciTableNEw.ONLYBOTREAL)
                ).row().cell("Testo: ")
                .row().cell(cella);
        if (messaggio.getCitato() != null) {
            Cell altraCella=new Cell(messaggio.getCitato().getTesto());
            altraCella.setMaximumWidth(35);
            table.row().cell(
                    (TableBuilder risposta) -> risposta.tableBorder(AsciiBorders.SOLID).cell("Messaggio citato").row().cell(
                                    (TableBuilder builder) -> builder.cell("Autore : " + messaggio.getCitato().getNomeAutore() + " " + messaggio.getCitato().getCognomeAutore())
                                            .cell("Id messaggio: " + messaggio.getCitato().getIdMess()).padding(3, 0)
                                            .cell(messaggio.getCitato().getTimestamp()).padding(3, 0)
                                            .tableBorder(BordersAsciTableNEw.ONLYBOTREAL)
                            ).row().cell("Testo :")
                            .row().cell(altraCella));
        }
        return table;
    }

    public static void stampaCanaliPrivati(CanalePrivatoBean[] beans){
                int contatore=1;
                for(CanalePrivatoBean canale:beans){
                    System.out.println((contatore++)+")");
                    TableBuilder table = new TableBuilder().tableBorder(AsciiBorders.DOUBLELINE)
                            .row().cell("Canale privato").row()
                            .cell("Partecipanti: "+canale.getPartecipanti()[0].getNome()+" "+canale.getPartecipanti()[0].getCognome()+" | "+canale.getPartecipanti()[1].getNome()+" "+canale.getPartecipanti()[1].getCognome())
                            .row().cell("Creato in data: "+canale.getData())
                            .row().cell("Messaggio di riferimento ").row().cell(prepareMessageTable(canale.getInizializante()).build());
                    System.out.println(table.build().render());
                }
    }


    private static String[][] convertiToMatricCanali(CanaleBean[] beans, boolean conPartecipazioen) {
        int dim = beans.length + 1;
        String[] nomiCa, dataCa, tipo, indice, partecipazione;
        nomiCa = new String[dim];
        dataCa = new String[dim];
        tipo = new String[dim];
        indice = new String[dim];
        partecipazione = new String[dim];
        nomiCa[0] = "Nome Canale";
        dataCa[0] = "Data di creazione";
        tipo[0] = "Tipo del canale";
        indice[0] = "indice";
        partecipazione[0] = "Partecipi al canale?";
        for (int i = 0; i < dim - 1; i++) {
            indice[i + 1] = String.valueOf(i + 1);
            nomiCa[i + 1] = beans[i].getNome();
            dataCa[i + 1] = beans[i].getData();
            tipo[i + 1] = beans[i].getTipo();
            partecipazione[i + 1] = beans[i].isPartecipazione() ? "si" : "no";
        }
        if (!conPartecipazioen) return new String[][]{indice, nomiCa, dataCa};
        return new String[][]{indice, nomiCa, dataCa, partecipazione};
    }

    public static void printerCandidatiCanale(CandidatiCanale[] beans) {
        try {
            PrinterCostum.stampaTabella(convertiToMatrixCandidatiCanale(beans));
        } catch (TabellaFormattataMaleException e) {
            System.out.println(e.getMessage());

        }
    }

    private static String[][] convertiToMatrixCandidatiCanale(CandidatiCanale[] candidatiCanales) {
        int dim = candidatiCanales.length + 1;
        String[] nomi, cognomi, email,indice;
        nomi = new String[dim];
        cognomi = new String[dim];
        email = new String[dim];
        indice = new String[dim];
        nomi[0] = "Nome";
        cognomi[0] = "Cognome";
        email[0] = "Email";
        indice[0] = "indice";
        for (int i = 0; i < dim - 1; i++) {
            indice[i + 1] = String.valueOf(i + 1);
            nomi[i + 1] =candidatiCanales[i].nome();
            cognomi[i + 1] = candidatiCanales[i].cognome();
            email[i + 1] =candidatiCanales[i].email();
        }
        return new String[][]{indice, nomi, cognomi, email};
    }

}
