package com.zgj.mps.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity implements java.io.Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private String role;
    private String description;
    private Short isDelete;
}