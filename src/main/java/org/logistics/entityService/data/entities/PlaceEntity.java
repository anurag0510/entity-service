package org.logistics.entityService.data.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.logistics.entityService.model.data.PlaceAttributes;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "places", uniqueConstraints={
        @UniqueConstraint( name = "unique_short_code",  columnNames ={"short_code"})
})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@NoArgsConstructor
public class PlaceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pid", nullable = false, unique = true)
    @Pattern(regexp = "PLC-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match place id pattern.")
    private String pid;

    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    @Column(name = "label", nullable = false, unique = true)
    private String label;

    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "latitude", nullable = false, unique = true)
    private Float latitude;

    @Column(name = "longitude", nullable = false, unique = true)
    private Float longitude;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "google_place_id", nullable = false, unique = true)
    private String googlePlaceId;

    @Column(name = "geometry", nullable = false, unique = true, columnDefinition="geometry")
    private Point geometry;

    @Column(name = "shape", nullable = false, unique = true, columnDefinition="geometry")
    private Polygon shape;

    @Type(type = "jsonb")
    @Column(name = "attributes", nullable = false, unique = true, columnDefinition = "jsonb")
    private PlaceAttributes attributes;

    @Column(name = "formatted_address", nullable = false, unique = true)
    private String formattedAddress;

    @Column(name = "region", nullable = false, unique = true)
    private String region;

    @Column(name = "locality", nullable = false, unique = true)
    private String locality;

    @Column(name = "country", nullable = false, unique = true)
    private String country;

    @Column(name = "postal_code", nullable = false, unique = true)
    private String postalCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition="boolean default true")
    private Boolean isActive;

    @Column(name = "is_deleted", nullable = false, columnDefinition="boolean default false")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "created_by")
    @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match user id pattern.")
    private String createdBy;

    @Column(name = "updated_by")
    @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match user id pattern.")
    private String updatedBy;
}
