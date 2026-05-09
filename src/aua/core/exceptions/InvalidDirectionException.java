package aua.core.exceptions;

public class InvalidDirectionException extends GameActionException {
    public InvalidDirectionException(){
        super("Wrong direction");
    }

    public InvalidDirectionException(String message){
        super(message);
    }
}
