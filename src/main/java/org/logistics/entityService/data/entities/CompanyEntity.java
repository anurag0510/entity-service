package org.logistics.entityService.data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.logistics.entityService.data.type.EntityArrayType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@TypeDefs({
        @TypeDef(
                name = "entity-array",
                typeClass = EntityArrayType.class
        )
})

@Data
@Entity
@Table(name = "company", uniqueConstraints={
        @UniqueConstraint( name = "idx_short_code",  columnNames ={"short_code"})
})
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cid", nullable = false, unique = true)
    @Pattern(regexp = "COM-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match company id pattern.")
    private String cid;

    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gstin", nullable = false)
    private String gstin;

    @Column(name = "tin", nullable = false)
    private String tin;

    @Column(name = "tan", nullable = false)
    private String tan;

    @Column(name = "cin", nullable = false)
    private String cin;

    @Column(name = "pan", nullable = false)
    private String pan;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "contact_user_id", nullable = false)
    private String contactUserId;

    @Column(name = "place_id", nullable = false)
    private String placeId;

    @Column(name = "head_office_id", nullable = false)
    private String headOfficeId;

    @Type( type = "entity-array")
    @Column(name = "types")
    private String[] types;

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
