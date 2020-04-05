package com.thetylermckay.backend.models;

import com.thetylermckay.backend.enums.Role;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class User {

  @CreationTimestamp
  private ZonedDateTime createdAt;

  @Column(unique = true, columnDefinition = "varchar(256)")
  private String email;

  @Column(columnDefinition = "varchar(64)")
  private String firstName;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(columnDefinition = "varchar(64)")
  private String lastName;

  @Column(columnDefinition = "varchar(256)")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "varchar(16)")
  private Role role;

  @UpdateTimestamp
  private ZonedDateTime updatedAt;

  /**
   * Before persisting to the database, set default values for some fields.
   */
  @PrePersist
  public void prePersist() {
    if (role == null) {
      role = Role.USER;
    }
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public String name() {
    return this.getFirstName() + this.getLastName();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
