package com.waffiyyi.onlinefoodordering.repository;

import com.waffiyyi.onlinefoodordering.model.ResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetRequest, Long> {
  Optional<ResetRequest> findByResetToken(String resetToken);
}
