    package com.lehaitien.gym.domain.mapper;

    import com.lehaitien.gym.application.dto.request.User.UserCreationRequest;
    import com.lehaitien.gym.application.dto.request.User.UserUpdateRequest;
    import com.lehaitien.gym.application.dto.response.User.UserResponse;
    import com.lehaitien.gym.domain.model.Authentication.Permission;
    import com.lehaitien.gym.domain.model.Authentication.Role;
    import com.lehaitien.gym.domain.model.Branch.Branch;
    import com.lehaitien.gym.domain.model.User.Coach;
    import com.lehaitien.gym.domain.model.User.User;
    import org.mapstruct.*;
    import org.mapstruct.factory.Mappers;

    import java.util.Collections;
    import java.util.Set;
    import java.util.stream.Collectors;


    @Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public interface UserMapper {

        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

        // Chuyển đổi từ CreationRequest sang Entity, ignore các trường xử lý riêng
        @Mapping(target = "userId", ignore = true)
        @Mapping(target = "password", ignore = true)
        @Mapping(target = "roles", ignore = true)
        @Mapping(target = "coach", ignore = true)
        @Mapping(target = "branch", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        User toUser(UserCreationRequest request);

        // Mapping từ User sang UserResponse (để trả về client)
        @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
        @Mapping(target = "coachId", source = "coach", qualifiedByName = "mapCoachId")
        @Mapping(target = "branchId", source = "branch", qualifiedByName = "mapBranchId")
        @Mapping(source = "coach.salary", target = "salary")
        @Mapping(source = "coach.specialization", target = "specialization")
        @Mapping(source = "coach.experienceYears", target = "experienceYears")
        @Mapping(source = "coach.certifications", target = "certifications")
        UserResponse toUserResponse(User user);

        // Cập nhật entity từ UpdateRequest, bỏ qua trường xử lý đặc biệt
        @Mapping(target = "password", ignore = true)
        @Mapping(target = "roles", ignore = true)
        @Mapping(target = "coach", ignore = true)
        @Mapping(target = "branch", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        void updateUser(@MappingTarget User user, UserUpdateRequest request);

        // Helper method để map roles Set<Role> -> Set<String>
        @Named("rolesToStrings")
        default Set<String> rolesToStrings(Set<Role> roles) {
            return roles == null ? Set.of() : roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
        }

        // 🔥 **Sửa kiểu tham số từ User → Coach**
        @Named("mapCoachId")
        default String mapCoachId(Coach coach) {
            return coach == null ? null : coach.getCoachId();
        }

        @Named("mapBranchId")
        default String mapBranchId(Branch branch) {
            return branch == null ? null : branch.getBranchId();
        }
    }