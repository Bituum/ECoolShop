package bteam.example.ecoolshop.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("no user found by provided username");
    }
}
