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

    public String saveOrUpdate(byte[] content, String extension, String previousLocation) throws Exception {
        if (previousLocation != null) {
            fileRepository.delete(previousLocation);
        }
        return fileRepository.save(content, extension);
    }

    public Optional<Resource> find(String location) {
        return fileRepository.find(location);
    }
}
