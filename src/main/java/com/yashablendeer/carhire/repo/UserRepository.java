package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to access database
 *
 * @author yaroslava
 * @version 1.0
 * @see com.yashablendeer.carhire.service.UserService
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserName(String userName);
    User findUserById(int id);
    Page<User> findAll(Pageable pageable);
    List<User> findAll();
}