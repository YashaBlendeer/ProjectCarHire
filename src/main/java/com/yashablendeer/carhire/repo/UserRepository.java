package com.yashablendeer.carhire.repo;

import com.yashablendeer.carhire.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserName(String userName);
    List<User> findAll();

//    public static final String FIND_PROJECTS = "SELECT projectId, projectName FROM projects";
//
//    @Query(value = FIND_PROJECTS, nativeQuery = true)
//    public List<Object[]> findProjects();
}