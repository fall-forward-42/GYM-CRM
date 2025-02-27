package com.lehaitien.gym.domain.mapper;

import com.lehaitien.gym.application.dto.request.Branch.BranchCreationRequest;
import com.lehaitien.gym.application.dto.request.Branch.BranchUpdateRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchResponse;
import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.User.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BranchMapper {

    BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);

    @Mapping(source = "users", target = "users", qualifiedByName = "mapUsers")
    BranchResponse toBranchResponse(Branch branch);

    Branch toBranch(BranchCreationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBranch(@MappingTarget Branch branch, BranchUpdateRequest request);

    @Named("mapUsers")
    default List<UserResponse> mapUsers(Set<User> users) {
        return users == null ? List.of() : users.stream()
                .map(UserMapper.INSTANCE::toUserResponse)
                .collect(Collectors.toList());
    }
}


