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

@Entity
public class FailedLoginAttempt {

  @CreationTimestamp
  @Getter @Setter private ZonedDateTime createdAt;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter private Long id;

  @Column(columnDefinition = "varchar(15)")
  @Getter @Setter private String ipAddress;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @Getter @Setter private User user;

  @Column(columnDefinition = "varchar(250)")
  @Getter @Setter private String userAgent;
}
