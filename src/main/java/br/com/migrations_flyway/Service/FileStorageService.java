package br.com.migrations_flyway.Service;

import br.com.migrations_flyway.config.FileStorageConfig;
import br.com.migrations_flyway.exceptions.FileStorageException;
import br.com.migrations_flyway.exceptions.MyFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUpload_dir())
                .toAbsolutePath().normalize();
        this.fileStorageLocation = path;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored!", e);
        }
    }
    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (filename.contains("..")) throw new FileStorageException("Sorry! your file contains invalid path sequence: " + filename);
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (Exception e) {
            throw new FileStorageException("Could not storage file " + filename + ". Try again!");
        }
    }
    public Resource loadFileAsResources(String filename) {
        try {
            Path path = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) return resource;
            else throw new MyFileNotFoundException("File not found.");
        } catch (Exception e) {
            throw new MyFileNotFoundException("File not found" + filename, e.getCause());
        }
    }
}
