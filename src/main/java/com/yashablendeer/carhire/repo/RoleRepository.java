package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access database
 *
 * @author yaroslava
 * @version 1.0
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);

}