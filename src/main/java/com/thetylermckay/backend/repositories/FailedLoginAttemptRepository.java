package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.FailedLoginAttempt;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedLoginAttemptRepository
    extends PagingAndSortingRepository<FailedLoginAttempt, Long> {

}
