package com.lehaitien.gym.application.service;

import com.lehaitien.gym.domain.constant.ImageType;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.model.Resource.Image;
import com.lehaitien.gym.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public Image uploadImage(String entityId, ImageType entityType, String imageUrl, boolean isThumbnail) {
        Image image = Image.builder()
                .entityId(entityId)
                .entityType(entityType)
                .imageUrl(imageUrl)
                .isThumbnail(isThumbnail)
                .build();
        return imageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public List<Image> getImagesByEntity(String entityId, ImageType entityType) {
        return imageRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Transactional
    public void deleteImage(Long imageId) {
        if (!imageRepository.existsById(imageId)) {
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }
        imageRepository.deleteById(imageId);
    }
}
