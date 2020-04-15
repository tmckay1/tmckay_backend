package com.thetylermckay.backend.models;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Token {

  @CreationTimestamp
  @Getter @Setter private ZonedDateTime createdAt;

  @Getter @Setter private ZonedDateTime expiresAt;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter private Long id;

  @Column(columnDefinition = "TINYINT default '1'")
  @Getter @Setter private boolean isActive;

  @Column(columnDefinition = "varchar(32)")
  @Getter @Setter private String token;

  @UpdateTimestamp
  @Getter @Setter private ZonedDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @Getter @Setter private User user;
}
