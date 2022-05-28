package bteam.example.ecoolshop.exception;

public class FileCreationException extends RuntimeException {
    public FileCreationException() {
        super("Error while creation file or dir");
    }
}
