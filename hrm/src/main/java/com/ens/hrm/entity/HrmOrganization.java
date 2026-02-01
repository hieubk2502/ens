package com.ens.hrm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "hrm_organization", schema = "hrm",
        uniqueConstraints =
                {@UniqueConstraint(name = "uk_organization_code", columnNames = "code")})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HrmOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "parent_id")
    Long parentId;

    @Column(name = "prev_id")
    Long prevId;

    @Column(nullable = false, length = 100)
    String code;

    @Column(nullable = false, length = 255)
    String name;
}
