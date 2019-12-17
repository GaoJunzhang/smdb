package com.zgj.mps.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update user set is_delete=1 where id=?")
@Where(clause = "is_delete=0")
public class User extends BaseEntity implements java.io.Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private String account;
    private String password;
    private String salt;
    private String code;
    @Enumerated
    private UserEnum type;
    @Enumerated
    private UserStatusEnum status;
    private Date validStartDate;
    private Date validEndDate;
    private Short isDelete;
    private String loginIp;
    private Timestamp loginTime;
    private String mobile;
    private String avatar;
}