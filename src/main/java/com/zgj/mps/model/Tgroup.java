package com.zgj.mps.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tgroup")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Tgroup extends BaseEntity implements java.io.Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tgroup_id")
    private Tgroup tgroup;
    private String name;
    private Double lat;
    private Double lng;
    private Integer lev;
    private String code;
    private Short isDelete;
    private Timestamp deleteTime;
}