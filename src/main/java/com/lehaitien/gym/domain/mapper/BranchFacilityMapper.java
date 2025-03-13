package com.lehaitien.gym.domain.mapper;


import com.lehaitien.gym.application.dto.request.Branch.BranchFacilityRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchFacilityResponse;
import com.lehaitien.gym.domain.constant.ImageType;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.Branch.BranchFacilityImage;
import com.lehaitien.gym.domain.model.Resource.Image;
import com.lehaitien.gym.domain.repository.ImageRepository;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BranchFacilityMapper {

    BranchFacilityMapper INSTANCE = Mappers.getMapper(BranchFacilityMapper.class);

    @Mapping(source = "branch.branchId", target = "branchId")
    @Mapping(target = "imageUrls", expression = "java(mapImages(facility))")
    BranchFacilityResponse toBranchFacilityResponse(BranchFacility facility);

@Mapping(target = "facilityId", ignore = true)
    BranchFacility toBranchFacility(BranchFacilityRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBranchFacility(@MappingTarget BranchFacility facility, BranchFacilityRequest request);

    default List<String> mapImages(BranchFacility facility) {
        if (facility.getImageUrls() == null) return List.of();
        return facility.getImageUrls().stream()
                .map(BranchFacilityImage::getImageUrl)
                .collect(Collectors.toList());
    }



}
