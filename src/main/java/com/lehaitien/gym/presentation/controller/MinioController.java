package com.lehaitien.gym.presentation.controller;


import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
@Tag(name = "MinIO File Management", description = "Upload, download and delete files using MinIO storage")
public class MinioController {

    private final MinioService minioService;

    @RequestMapping(
            path = "/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ApiResponse<String> uploadFile(
            @RequestPart("file")  MultipartFile file
    ) {
        String fileUrl = minioService.uploadFile(file);
        return ApiResponse.<String>builder()
                .result(fileUrl)
                .message("File uploaded successfully!")
                .build();
    }


//    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(
//            summary = "Upload multiple files to MinIO",
//            description = "Uploads multiple files to MinIO and returns a list of uploaded URLs."
//    )
//    public ApiResponse<List<String>> uploadMultipleFiles(
//            @Parameter(
//                    description = "Multiple files to upload",
//                    required = true,
//                    content = @Content(
//                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
//                            array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))
//                    )
//            )
//            @RequestParam("files") List<MultipartFile> files
//    ) {
//        List<String> fileUrls = minioService.uploadMultipleFiles(files);
//        return ApiResponse.<List<String>>builder()
//                .result(fileUrls)
//                .message("Files uploaded successfully!")
//                .build();
//    }


    @GetMapping("/download/{fileName}")
    @Operation(
            summary = "Download a file from MinIO by filename",
            description = "Downloads a file from the MinIO bucket and returns it as a binary stream."
    )
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        InputStream fileStream = minioService.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream));
    }

    @DeleteMapping("/delete/{fileName}")
    @Operation(
            summary = "Delete a file from MinIO by filename",
            description = "Deletes the specified file from MinIO storage."
    )
    public ApiResponse<String> deleteFile(@PathVariable String fileName) {
        minioService.deleteFile(fileName);
        return ApiResponse.<String>builder()
                .result(fileName)
                .message("File deleted successfully!")
                .build();
    }
}
