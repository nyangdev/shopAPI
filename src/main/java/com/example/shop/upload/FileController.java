package com.example.shop.upload;

import com.example.shop.upload.exception.EmptyFileException;
import com.example.shop.upload.exception.UploadNotSupportedException;
import com.example.shop.upload.util.UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final UploadUtil uploadUtil;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("files") MultipartFile[] files) {

        checkFileEmpty(files);

        log.info("upload file....");

        for(MultipartFile file : files) {

            if(file.isEmpty()) {
                throw new EmptyFileException("Empty file");
            }

            log.info("--------------------------");
            log.info("name: " + file.getOriginalFilename());
            checkFileType(file.getOriginalFilename());
        }

        List<String> result = uploadUtil.upload(files);

        return ResponseEntity.ok(result);

    }

    private void checkFileType(String fileName) throws UploadNotSupportedException {

        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);

        String regExp = "^(jpg|jpeg|JPG|JPEG|png|PNG|gif|GIF|bmp|BMP)";

        if(!suffix.matches(regExp)) {
            throw new UploadNotSupportedException("File type not supported: " + suffix);
        }
    }

    private void checkFileEmpty(MultipartFile[] files) throws EmptyFileException {

        log.info("check file empty....");

        if(files == null || files.length == 0) {
            throw new EmptyFileException("No files to upload");
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable(name = "fileName") String fileName) {
        log.info("delete file: " + fileName);

        uploadUtil.deleteFile(fileName);

        return ResponseEntity.ok().build();
    }
}
