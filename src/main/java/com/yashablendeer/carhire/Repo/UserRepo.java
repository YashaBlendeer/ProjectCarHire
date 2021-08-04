package com.yashablendeer.carhire.Repo;

import com.yashablendeer.carhire.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}