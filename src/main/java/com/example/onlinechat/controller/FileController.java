package com.example.onlinechat.controller;

import com.example.onlinechat.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/photo/{photo-id}")
    public Resource downloadPhoto(@PathVariable("photo-id") String photoId) {
        return fileService.find(photoId).orElse(null);
    }
}
