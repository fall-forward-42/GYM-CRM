package com.lehaitien.gym.application.service;

import com.lehaitien.gym.application.dto.request.Branch.BranchFacilityRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchFacilityResponse;
import com.lehaitien.gym.domain.constant.ImageType;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.BranchFacilityMapper;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.Branch.BranchFacilityImage;
import com.lehaitien.gym.domain.model.Resource.Image;
import com.lehaitien.gym.domain.repository.BranchFacilityImageRepository;
import com.lehaitien.gym.domain.repository.BranchFacilityRepository;
import com.lehaitien.gym.domain.repository.BranchRepository;
import com.lehaitien.gym.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchFacilityService {

    private final BranchFacilityRepository facilityRepository;
    private final BranchRepository branchRepository;
    private final BranchFacilityMapper facilityMapper;
    private final ImageService imageService;
    private final BranchFacilityImageRepository imageRepository;
    private final  MinioService minioService;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BranchFacilityResponse createFacility(String branchId, BranchFacilityRequest request) {
        log.info("Creating facility for branch ID: {} with request: {}", branchId, request);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        if (facilityRepository.existsByFacilityName(request.getFacilityName())) {
            throw new AppException(ErrorCode.FACILITY_EXISTED);
        }

        BranchFacility facility = facilityMapper.toBranchFacility(request);
        facility.setBranch(branch);

        // Lưu facility trước để lấy ID
        facility = facilityRepository.save(facility);


        // Lưu ảnh thumbnail nếu có
        if (request.getThumbnailFile() != null) {
            facility.setThumbnailUrl(request.getThumbnailFile());
        }

        // Lưu danh sách ảnh nếu có
        if (request.getImageFiles() != null && !request.getImageFiles().isEmpty()) {
            BranchFacility finalFacility = facility;
            request.getImageFiles().forEach(imageUrl -> {
                BranchFacilityImage image = BranchFacilityImage.builder()
                        .branchFacility(finalFacility)
                        .imageUrl(imageUrl)
                        .build();
                imageRepository.save(image);
            });
        }


        // Lưu lại facility sau khi cập nhật ảnh
        facility = facilityRepository.save(facility);

        log.info("Facility created successfully: {}", facility.getFacilityId());

        return facilityMapper.toBranchFacilityResponse(facility);
    }

    @Transactional(readOnly = true)
    public BranchFacilityResponse getFacilityById(String facilityId) {
        BranchFacility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITY_NOT_FOUND));


        BranchFacilityResponse response = facilityMapper.toBranchFacilityResponse(facility);
        return response;
    }

    @Transactional(readOnly = true)
    public List<BranchFacilityResponse> getFacilitiesByBranch(String branchId) {
        return facilityRepository.findByBranch_BranchId(branchId).stream()
                .map(facilityMapper::toBranchFacilityResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BranchFacilityResponse> getFacilities() {
        return facilityRepository.findAll().stream()
                .map(facilityMapper::toBranchFacilityResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BranchFacilityResponse updateFacility(String facilityId, BranchFacilityRequest request) {
        BranchFacility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new AppException(ErrorCode.FACILITY_NOT_FOUND));

        facilityMapper.updateBranchFacility(facility, request);
        facility = facilityRepository.save(facility);

        //  **Cập nhật ảnh thumbnail**
        if (request.getThumbnailFile() != null) {
            facility.setThumbnailUrl(request.getThumbnailFile());
        }

        // **Cập nhật danh sách ảnh**
        updateFacilityImages(facility, request.getImageFiles());

        facility = facilityRepository.save(facility);
        log.info(" Facility updated successfully: {}", facility.getFacilityId());


        return facilityMapper.toBranchFacilityResponse(facility);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFacility(String facilityId) {
        if (!facilityRepository.existsById(facilityId)) {
            throw new AppException(ErrorCode.FACILITY_NOT_FOUND);
        }

        // Xóa ảnh trước khi xóa facility
        imageRepository.deleteByBranchFacility_FacilityId(facilityId);

        facilityRepository.deleteById(facilityId);

        log.info("Facility deleted: {}", facilityId);
    }

    // **Lưu danh sách ảnh**
    private void saveFacilityImages(BranchFacility facility, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return;

        List<BranchFacilityImage> images = imageUrls.stream()
                .map(url -> BranchFacilityImage.builder()
                        .branchFacility(facility)
                        .imageUrl(url)
                        .build())
                .toList();

        imageRepository.saveAll(images);
    }

    //**Cập nhật danh sách ảnh**
    private void updateFacilityImages(BranchFacility facility, List<String> newImageUrls) {
        if (newImageUrls == null || newImageUrls.isEmpty()) return;

        // Xóa ảnh cũ
        imageRepository.deleteByBranchFacility_FacilityId(facility.getFacilityId());

        // Lưu ảnh mới
        saveFacilityImages(facility, newImageUrls);
    }
}
