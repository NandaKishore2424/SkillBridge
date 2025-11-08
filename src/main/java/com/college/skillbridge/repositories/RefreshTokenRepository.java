package com.college.skillbridge.repositories;

import com.college.skillbridge.models.RefreshToken;
import com.college.skillbridge.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}

