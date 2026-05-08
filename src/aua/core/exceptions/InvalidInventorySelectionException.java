package aua.core.exceptions;

public class InvalidInventorySelectionException extends GameActionException {

    public InvalidInventorySelectionException(){
        super("No such item in the inventory");
    }

    public InvalidInventorySelectionException(String message){
        super(message);
    }
}
