package com.lehaitien.gym.domain.model.Resource;


import com.lehaitien.gym.domain.constant.ImageType;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    Long id;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    ImageType entityType;

    @Column(name = "entity_id")
    String entityId;

    @Column(name = "is_thumbnail")
    boolean isThumbnail;


}

