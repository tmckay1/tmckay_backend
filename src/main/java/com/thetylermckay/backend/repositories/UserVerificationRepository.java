package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.UserVerification;
import org.springframework.data.repository.CrudRepository;

public interface UserVerificationRepository extends CrudRepository<UserVerification, Long> {

}
