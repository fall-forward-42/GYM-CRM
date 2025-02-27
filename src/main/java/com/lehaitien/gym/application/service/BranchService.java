package com.lehaitien.gym.application.service;


import com.lehaitien.gym.application.dto.request.Branch.BranchCreationRequest;
import com.lehaitien.gym.application.dto.request.Branch.BranchUpdateRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchResponse;
import com.lehaitien.gym.domain.constant.BranchStatus;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.BranchMapper;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.BranchRepository;
import com.lehaitien.gym.domain.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BranchService {

    BranchRepository branchRepository;
    BranchMapper branchMapper;
    UserRepository userRepository;


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public BranchResponse addUserToBranch(String branchId, String userId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (branch.getUsers().contains(user)) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_BRANCH);
        }
        user.setBranch(branch);
        branch.getUsers().add(user);
        log.info("User branch before save: {}", user.getBranch().getBranchId());
        userRepository.save(user);
        branchRepository.save(branch);
        log.info("User branch after save: {}", userRepository.findById(user.getUserId()).get().getBranch().getBranchId());

        return branchMapper.toBranchResponse(branch);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public BranchResponse removeUserFromBranch(String branchId, String userId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!branch.equals(user.getBranch())) {
            throw new AppException(ErrorCode.USER_NOT_IN_BRANCH);
        }

        user.setBranch(null);
        //log.info("User branch before save: {}", user.getBranch().getBranchId());
        userRepository.save(user);
        //log.info("User branch after save: {}", userRepository.findById(user.getUserId()).get().getBranch().getBranchId());

        return branchMapper.toBranchResponse(branch);
    }



    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BranchResponse createBranch(BranchCreationRequest request) {

        if (branchRepository.existsByBranchName(request.branchName())) {
            throw new AppException(ErrorCode.BRANCH_EXISTED);
        }

        Branch branch = branchMapper.toBranch(request);
        branch.setStatus(BranchStatus.ACTIVE);

        return branchMapper.toBranchResponse(branchRepository.save(branch));
    }

    @Transactional(readOnly = true)
    public List<BranchResponse> getAllBranches() {
        log.info("Fetching all branches...");
        return branchRepository.findAll().stream()
                .map(branchMapper::toBranchResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BranchResponse getBranchById(String branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
        return branchMapper.toBranchResponse(branch);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BranchResponse updateBranch(String branchId, BranchUpdateRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        branchMapper.updateBranch(branch, request);

        if (request.status() != null) {
            branch.setStatus(request.status());
        }

        return branchMapper.toBranchResponse(branchRepository.save(branch));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBranch(String branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new AppException(ErrorCode.BRANCH_NOT_FOUND);
        }
        branchRepository.deleteById(branchId);
    }
}
