package com.example.onlinechat.service;

import com.example.onlinechat.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {
    final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String save(byte[] content, String extension) throws Exception {
        return fileRepository.save(content, extension);
    }

    public void delete(String location) throws Exception {
        fileRepository.delete(location);
    }

    public Optional<Resource> find(String location) {
        return fileRepository.find(location);
    }
}
