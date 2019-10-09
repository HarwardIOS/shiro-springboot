package com.springboot.shiro.shiro2spboot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(columnDefinition = "enum('menu','button')")
    private String resourceType;//资源类型,[menu|button]
    private String url;//资源路径
    private String permissionStr;//权限字符串
    private Long parentId;//父编号
    private String parentIds;//父编号列表
    private Boolean available = Boolean.FALSE;//是否可用

    @JSONField(serialize = false)//不序列化该属性
    @ManyToMany(fetch = FetchType.LAZY)//配置反向延迟加载
    @JoinTable(name = "RolePermission", joinColumns = {@JoinColumn(name = "permissionId")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<Role> roles;
}
