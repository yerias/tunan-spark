package com.tunan.web.dao;

import com.tunan.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tunan
 */
public interface UserRepository extends JpaRepository<User,Integer> {
}
