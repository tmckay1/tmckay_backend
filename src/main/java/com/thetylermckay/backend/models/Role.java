package com.thetylermckay.backend.models;

import java.util.Collection;
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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter private Long id;

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
}
