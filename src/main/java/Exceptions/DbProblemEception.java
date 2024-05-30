package Exceptions;

public class DbProblemEception extends Exception{
    public DbProblemEception(String a){
        super(a);
    }
    public DbProblemEception(){
        super("Rileavto problema con il DB");
    }
}
