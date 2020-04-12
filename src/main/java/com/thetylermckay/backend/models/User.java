package com.thetylermckay.backend.models;

import com.thetylermckay.backend.enums.Role;
import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class User {

  @CreationTimestamp
  @Getter @Setter private ZonedDateTime createdAt;

  @Column(unique = true, columnDefinition = "varchar(256)")
  @Getter @Setter private String email;

  @Column(columnDefinition = "TINYINT default '0'")
  @Getter @Setter private Integer failedAttempts;

  @Column(columnDefinition = "varchar(64)")
  @Getter @Setter private String firstName;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter private Long id;

  @Column(columnDefinition = "TINYINT default '0'")
  @Getter @Setter private Boolean isActive;

  @Column(columnDefinition = "TINYINT default '0'")

  // Whether the user has verified who they are.
  @Getter @Setter private Boolean isVerified;

  @Column(columnDefinition = "varchar(15)")
  @Getter @Setter private String lastLoginIp;

  @Column(columnDefinition = "varchar(64)")
  @Getter @Setter private String lastName;

  @Column(columnDefinition = "varchar(256)")
  @Getter @Setter private String password;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "varchar(16)")
  @Getter @Setter private Role role;
  
  @OneToMany(mappedBy = "user")
  @Getter @Setter private Set<Token> tokens;

  @UpdateTimestamp
  @Getter @Setter private ZonedDateTime updatedAt;
  
  @OneToMany(mappedBy = "user")
  @Getter @Setter private Set<UserVerification> userVerifications;

  /**
   * Before persisting to the database, set default values for some fields.
   */
  @PrePersist
  public void prePersist() {
    if (role == null) {
      role = Role.USER;
    }
  }

  public String name() {
    return this.getFirstName() + " " + this.getLastName();
  }
}
