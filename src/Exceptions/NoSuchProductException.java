package Exceptions;

public class NoSuchProductException extends Exception{
    public NoSuchProductException(String errorMessage){
        super(errorMessage);
    }

}
