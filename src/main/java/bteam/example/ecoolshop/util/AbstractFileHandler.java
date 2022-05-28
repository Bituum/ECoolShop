package bteam.example.ecoolshop.util;

import java.io.IOException;

public abstract class AbstractFileHandler<T> {
    public abstract String handle(T file, String username, String path);

    abstract boolean createDir(String path, String username);

    abstract boolean createFile(T file, String username) throws IOException;
}
