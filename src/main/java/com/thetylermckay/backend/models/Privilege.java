package com.thetylermckay.backend.models;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Privilege {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter private Long id;

  @Getter @Setter private String name;

  @ManyToMany(mappedBy = "privileges")
  @Getter @Setter private Collection<Role> roles;
}
