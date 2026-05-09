package aua.core.exceptions;

public class InvalidGameActionException extends GameActionException {

    public InvalidGameActionException(){
        super("Invalid Game Action");
    }

    public InvalidGameActionException(String message){
        super(message);
    }
}
