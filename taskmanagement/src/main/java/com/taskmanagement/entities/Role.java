package com.taskmanagement.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {

    @Id
    int id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    String name;

    @Column(name = "description")
    String description;
}
