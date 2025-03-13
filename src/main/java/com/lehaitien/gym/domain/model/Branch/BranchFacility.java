package com.lehaitien.gym.domain.model.Branch;

import com.lehaitien.gym.domain.constant.FacilityStatus;
import com.lehaitien.gym.domain.constant.ImageType;
import com.lehaitien.gym.domain.model.Resource.Image;
import com.lehaitien.gym.domain.repository.ImageRepository;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "branch_facility", indexes = {
        @Index(name = "idx_facility_name", columnList = "facility_name")
})
public class BranchFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "facility_id", columnDefinition = "varchar(36)")
    String facilityId;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    Branch branch;

    @Column(name = "facility_name", nullable = false, unique = true)
    String facilityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    FacilityStatus status;

    @Column(name = "capacity")
    Integer capacity;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "thumbnail_url")
    String thumbnailUrl;

    @OneToMany(mappedBy = "branchFacility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<BranchFacilityImage> imageUrls = new ArrayList<>();


}

