package com.thetylermckay.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.views.UserViews;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class User {

  @CreationTimestamp
  @JsonView(UserViews.Index.class)
  @Getter @Setter private ZonedDateTime createdAt;

  @Column(unique = true, columnDefinition = "varchar(256)")
  @JsonView(UserViews.Index.class)
  @Getter @Setter private String email;

  @Column(columnDefinition = "TINYINT default '0'")
  @Getter @Setter private Integer failedAttempts;

  @Column(columnDefinition = "TINYINT default '0'")
  @Getter @Setter private Integer failedVerificationAttempts;

  @Column(columnDefinition = "varchar(64)")
  @JsonView(UserViews.Index.class)
  @Getter @Setter private String firstName;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(UserViews.Index.class)
  @Getter @Setter private Long id;

  @JsonView(UserViews.Index.class)
  @Getter @Setter private String imagePath;

  @Column(columnDefinition = "TINYINT default '0'")
  @JsonView(UserViews.Index.class)
  @Getter @Setter private Boolean isActive;

  // Whether the user has verified who they are.
  @Column(columnDefinition = "TINYINT default '0'")
  @JsonView(UserViews.Index.class)
  @Getter @Setter private Boolean isVerified;

  @Column(columnDefinition = "varchar(15)")
  @Getter @Setter private String lastLoginIp;

  @Column(columnDefinition = "varchar(64)")
  @JsonView(UserViews.Index.class)
  @Getter @Setter private String lastName;

  @Column(columnDefinition = "varchar(256)")
  @Getter @Setter private String password;
  
  @ManyToMany
  @JoinTable(
      name = "user_roles", 
      joinColumns = @JoinColumn(
        name = "user_id", referencedColumnName = "id"), 
      inverseJoinColumns = @JoinColumn(
        name = "role_id", referencedColumnName = "id"))
  @Getter @Setter private Collection<Role> roles;
  
  @OneToMany(mappedBy = "user")
  @Getter @Setter private Set<Token> tokens;

  @UpdateTimestamp
  @Getter @Setter private ZonedDateTime updatedAt;
  
  @OneToMany(mappedBy = "user")
  @JsonView(UserViews.Index.class)
  @Getter @Setter private Set<UserVerification> userVerifications;

  @JsonView(UserViews.Index.class)
  public String name() {
    return this.getFirstName() + " " + this.getLastName();
  }

  @JsonView(UserViews.Index.class)
  public List<String> userRoles() {
    return roles.stream().map(r -> r.getName()).collect(Collectors.toList());
  }

  @JsonView(UserViews.Index.class)
  public List<Long> roleIds() {
    return roles.stream().map(r -> r.getId()).collect(Collectors.toList());
  }
}
