package Exceptions;

public class UnavailableException extends  Exception{
    public UnavailableException (String errorMessage){
        super(errorMessage);
    }
}
