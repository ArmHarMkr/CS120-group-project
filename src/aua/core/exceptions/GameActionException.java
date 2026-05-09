package aua.core.exceptions;

public class GameActionException extends Exception {

    public GameActionException(){
        super("Wrong action");
    }

    public GameActionException(String message){
        super(message);
    }


}
