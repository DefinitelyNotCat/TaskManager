package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.Files;
import com.example.TaskManager.DataModels.TaskMessage;
import com.example.TaskManager.Repositories.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    private final FileDBRepository fileDBRepository;

    @Autowired
    public FileStorageService(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    public Files store(MultipartFile file, TaskMessage taskMessage) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Files files = new Files(fileName, file.getContentType(), file.getBytes(), taskMessage);

        return fileDBRepository.save(files);
    }

    public Files getFileById(Long id) {
        return fileDBRepository.findById(id);
    }

    public Stream<Files> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    public List<Files> getAllFilesByTaskMessage(TaskMessage taskMessage) {
        return fileDBRepository.findAllByTaskMessage(taskMessage);
    }
}
