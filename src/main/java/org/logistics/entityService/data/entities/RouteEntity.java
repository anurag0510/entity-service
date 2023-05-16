package org.logistics.entityService.data.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.logistics.entityService.model.data.RouteAttributes;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "routes")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@NoArgsConstructor
public class RouteEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rid", nullable = false, unique = true)
    @Pattern(regexp = "RTE-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match route id pattern.")
    private String rid;

    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    @Column(name = "label", nullable = false, unique = true)
    private String label;

    @Column(name = "origin_pid", nullable = false, unique = true)
    @Pattern(regexp = "PLC-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match place id pattern.")
    private String originPid;

    @Column(name = "destination_pid", nullable = false, unique = true)
    @Pattern(regexp = "PLC-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match place id pattern.")
    private String destinationPid;

    @Column(name = "parent_id")
    private String parentId;

    @Type(type = "jsonb")
    @Column(name = "attributes", nullable = false, unique = true, columnDefinition = "jsonb")
    private RouteAttributes attributes;

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
