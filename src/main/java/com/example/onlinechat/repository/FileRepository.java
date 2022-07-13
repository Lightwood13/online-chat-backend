package com.example.onlinechat.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileRepository {
    @Value("${chat.storage-dir}")
    private String STORAGE_DIR;

    public String save(byte[] content, String extension) throws Exception {
        final String localPath = UUID.randomUUID() + "." + extension;
        Files.write(Paths.get(STORAGE_DIR, localPath), content);
        return localPath;
    }

    public Optional<Resource> find(String location) {
        final Path filePath = Paths.get(STORAGE_DIR, location);
        return Optional.of((Resource) new FileSystemResource(filePath))
                .filter(r -> Files.exists(filePath));
    }

    public void delete(String location) throws IOException {
        Files.deleteIfExists(Paths.get(STORAGE_DIR, location));
    }
}
