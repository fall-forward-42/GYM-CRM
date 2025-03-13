package com.lehaitien.gym.presentation.controller;


import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioController {

    private final MinioService minioService;

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = minioService.uploadFile(file);
        return ApiResponse.<String>builder()
                .result(fileUrl)
                .message("File uploaded successfully!")
                .build();
    }
    @PostMapping("/upload-multiple")
    public ApiResponse<List<String>> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> fileUrls = minioService.uploadMultipleFiles(files);

        return ApiResponse.<List<String>>builder()
                .result(fileUrls)
                .message("Files uploaded successfully!")
                .build();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        InputStream fileStream = minioService.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream));
    }

    @DeleteMapping("/delete/{fileName}")
    public ApiResponse<String> deleteFile(@PathVariable String fileName) {
        minioService.deleteFile(fileName);
        return ApiResponse.<String>builder()
                .result(fileName)
                .message("File deleted successfully!")
                .build();
    }
}
