package com.sochoeun.config;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class FileUploadService {

    private String path;
    private String filename;
    private MultipartFile file;
    private String urlPath;

    public final String generateUrl(String path, String filename, MultipartFile file,String urlPath) {
        this.path = path;
        this.filename = filename;
        this.file = file;
        this.urlPath=urlPath;
        return photoFunction.apply(filename, file);

    }

    private final Function<String,String> fileExtension =
            filename -> Optional.of(filename)
                    .filter(name -> name.contains("."))
                    .map(name
                            -> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    public  BiFunction<String, MultipartFile,String> photoFunction = (filename, file) ->{

        try{
            Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(
                    file.getInputStream(),
                    fileStorageLocation.resolve(filename + fileExtension.apply(file.getOriginalFilename())), // filename
                    REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath() // localhost:8080
                    .path(urlPath + filename + fileExtension.apply(file.getOriginalFilename())).toUriString();
        }catch (Exception e){
            throw new RuntimeException("Unable to save image");
        }
    };
}
