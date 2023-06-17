package com.alibou.security.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class Helper {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    private static final Path STATIC_PATH = Paths.get("static");

    public static String upload(MultipartFile image, String folder) throws IOException {
        String randomString = UUID.randomUUID().toString();
        Path imagePath = Paths.get(folder);

        if (!Files.exists(CURRENT_FOLDER.resolve(STATIC_PATH).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(STATIC_PATH).resolve(imagePath));
        }
        int hashCode = Objects.requireNonNull(image.getOriginalFilename()).hashCode();
        String fileName = hashCode + "." + randomString + image.getOriginalFilename();

        Path file = CURRENT_FOLDER.resolve(STATIC_PATH)
                .resolve(imagePath).resolve(fileName);
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }

        return imagePath.resolve(fileName).toString();
    }

    public static void delete(String urlImage) {
        Path fileToDelete = CURRENT_FOLDER.resolve(STATIC_PATH).resolve(urlImage);
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
