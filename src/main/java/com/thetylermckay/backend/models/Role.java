package com.thetylermckay.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.views.RoleViews;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Role {

  @JsonView(RoleViews.Index.class)
  @Column(columnDefinition = "varchar(100)")
  @Getter @Setter private String description;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(RoleViews.Index.class)
  @Getter @Setter private Long id;

  @JsonView(RoleViews.Index.class)
  @Getter @Setter private String name;

  @ManyToMany(mappedBy = "roles")
  @Getter @Setter private Collection<User> users;

  @ManyToMany
  @JoinTable(
      name = "roles_privileges", 
      joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
      inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
  @Getter @Setter private Collection<Privilege> privileges;

  @JsonView(RoleViews.Index.class)
  public List<Long> privilegeIds() {
    return privileges.stream().map(p -> p.getId()).collect(Collectors.toList());
  }

  @JsonView(RoleViews.Index.class)
  public List<String> rolePrivileges() {
    return privileges.stream().map(p -> p.getName()).collect(Collectors.toList());
  }
}
