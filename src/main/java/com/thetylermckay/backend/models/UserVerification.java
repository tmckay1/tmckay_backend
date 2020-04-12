package com.thetylermckay.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.views.UserVerificationViews;
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
public class UserVerification {

  @CreationTimestamp
  @Getter @Setter private ZonedDateTime createdAt;

  @Id
  @JsonView(UserVerificationViews.Index.class)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter private Long id;

  @UpdateTimestamp
  @Getter @Setter private ZonedDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @Getter @Setter private User user;

  @Column(columnDefinition = "varchar(40)")
  @Getter @Setter private String verificationAnswer;

  @JsonView(UserVerificationViews.Index.class)
  @Column(columnDefinition = "varchar(100)")
  @Getter @Setter private String verificationQuestion;
}
