CREATE TABLE IF NOT EXISTS branch_facility_images (
    facility_id VARCHAR(36) NOT NULL,
    image_url TEXT NOT NULL,
    PRIMARY KEY (facility_id, image_url),
    FOREIGN KEY (facility_id) REFERENCES branch_facility(facility_id) ON DELETE CASCADE
    );
