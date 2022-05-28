package bteam.example.ecoolshop.util;

import bteam.example.ecoolshop.exception.FileCreationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class PhotoHandler extends AbstractFileHandler<MultipartFile> {
    private File photoFile;
    private String dirname;

    @Override
    public String handle(MultipartFile file, String username, String path) {
        createDir(path, username);

        try {
            if (!createFile(file, username)) {
                throw new FileCreationException();
            }

            file.transferTo(photoFile);
        } catch (IOException e) {
            throw new FileCreationException();
        }

        return photoFile.getAbsolutePath();
    }

    @Override
    boolean createDir(String path, String username) {
        String dirSuffix = "_photoDir";
        dirname = path + username + dirSuffix;
        File dir = new File(dirname);

        if (!dir.exists()) {
            return dir.mkdir();
        }

        return false;
    }

    //username1000000000.png
    @Override
    boolean createFile(MultipartFile file, String username) throws IOException {
        String filename;
        int randInt = randomPhotoNumberGenerator();

        String contentType = "." + Objects.requireNonNull(file.getContentType()).substring(6);

        filename = username + randInt + contentType;
        File photo = new File(dirname, filename);

        if (photo.exists()) {
            return false;
        }

        photoFile = photo;

        return photo.createNewFile();
    }

    private int randomPhotoNumberGenerator() {
        Random random = new Random();
        int bound = 1000000000;

        return random.nextInt(bound);
    }
}
