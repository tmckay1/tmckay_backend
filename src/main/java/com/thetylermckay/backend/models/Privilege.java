package com.thetylermckay.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.views.RoleViews;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Privilege {

  @Column(columnDefinition = "varchar(100)")
  @JsonView(RoleViews.Index.class)
  @Getter @Setter private String description;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(RoleViews.Index.class)
  @Getter @Setter private Long id;

  @JsonView(RoleViews.Index.class)
  @Getter @Setter private String name;

  @ManyToMany(mappedBy = "privileges")
  @Getter @Setter private Collection<Role> roles;
}
