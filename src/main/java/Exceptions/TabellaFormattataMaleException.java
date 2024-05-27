package Exceptions;

public class TabellaFormattataMaleException extends Exception {

    public TabellaFormattataMaleException(){
        super("Tabelle formattate male, controllare dimensioni e simmetria tra le colonne");
    }
}
