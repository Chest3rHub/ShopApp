package Exceptions;

public class NotEnoughProductsException extends Exception{

    public NotEnoughProductsException (String errorMessage){
        super(errorMessage);
    }

}
