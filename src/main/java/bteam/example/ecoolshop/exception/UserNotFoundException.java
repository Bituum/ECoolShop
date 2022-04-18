package bteam.example.ecoolshop.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("not user found by provided username");
    }
}
