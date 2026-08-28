package com.labanta.servidorlocal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String dirUploads = "uploads/images";

    public FileStorageService(){
        try {
            Files.createDirectories(Path.of(dirUploads));
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu erro ao criar pasta" + ex);
        }
    }

    public String StoreImages(MultipartFile file){
        try {

            String uniqueName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();

            String outputPath = Paths.get(dirUploads).resolve(uniqueName).toString();

            Files.copy(file.getInputStream(),Paths.get(outputPath));

            return  uniqueName;

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao carregar ficheiro"+ex);
        }
    }

}