package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUpload {

    private static final Logger log = LoggerFactory.getLogger(FileUpload.class);

    private Path filesPath;

    @PostConstruct
    public void init() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        this.filesPath = Paths.get(tmpDir, "afterburner-uploads");
        log.info("Creating upload directory [{}].", filesPath);
        try {
            Files.createDirectories(filesPath);
        } catch (IOException e) {
            throw new AfterburnerException(String.format("Could not initialize storage for [%s].", filesPath), e);
        }
    }

    @RequestMapping(value = "/files/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<String> upload(@RequestParam("upload") MultipartFile file) {
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        log.info("Upload file: [{}] of size [{}] bytes.", filename, size);
        saveFile(file);
        return ResponseEntity.ok().body(String.format("File upload succeeded [%s] of size [%d] bytes.", filename, size));
    }

    private void saveFile(final MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new AfterburnerException(String.format("Cannot upload an empty file [%s].", filename));
        }

        securityCheck(filename);

        try {
            File destFile = filesPath.resolve(filename).toFile();
            log.info("Saving {}.", destFile.getAbsolutePath());
            file.transferTo(destFile);
        } catch (IOException | IllegalStateException e) {
            throw new AfterburnerException(String.format("Error saving file [%s].", filename), e);
        }
    }

    private void securityCheck(final String filename) {
        if (filename.contains("..")) {
            throw new AfterburnerException(String.format("Relative paths are not allowed [%s].", filename));
        }
    }

    @GetMapping("/files/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = loadAsResource(filename);
        long sizeBytes;
        try {
            sizeBytes = file.contentLength();
        } catch (IOException e) {
            log.warn("Unable to determine size for file [{}].", file.getFilename());
            sizeBytes = -1;
        }
        log.info("Download file [{}] of size [{}] bytes.", filename, sizeBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", file.getFilename()))
                .body(file);
    }

    private Resource loadAsResource(String filename) {
        securityCheck(filename);
        try {

            Path file = filesPath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new AfterburnerException(String.format("Could not read file [%s].", filename));
            }
        } catch (MalformedURLException e) {
            throw new AfterburnerException(String.format("Could not read file [%s].", filename), e);
        }
    }
}
