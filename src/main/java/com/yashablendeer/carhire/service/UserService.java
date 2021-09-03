package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Role;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.RoleRepository;
import com.yashablendeer.carhire.repo.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * User service
 *
 * @author yaroslava
 * @version 1.0
 */

@Log4j2
@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User findUserByUserId(int userId) {
        return userRepository.findUserById(userId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAllUsersPageable(PageRequest page) {
        return userRepository.findAll(page);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    /**
     * Sets new user active status, depending on the previous one
     * If user was banned, his status changes to active
     * If user was active, his status changes to banned
     *
     * @param userId ID of user, which status will be changed
     */

    public User banHandler(int userId) {
        User user = findUserByUserId(userId);
        if (user.getActive()) {
            user.setActive(false);
            log.info("User active status was changed to banned");

        } else {
            user.setActive(true);
            log.info("User active status was changed to active");

        }
        return userRepository.save(user);
    }

    /**
     * Sets a new role "MANAGER" or "USER" for user depending on the previous one
     * If user has a role "MANAGER", his role changes to "USER"
     * If user has a role "USER", his role changes to "MANAGER"
     *
     * @param id ID of user, which role will be changed
     */
    public User setManagerHandler(int id) {
        User user = findUserByUserId(id);
        Role managerRole = roleRepository.findByRole("MANAGER");
        boolean isManager = user.getRoles().contains(managerRole);
        Role userRole;
        if(isManager) {
            userRole = roleRepository.findByRole("USER");
        } else {
            userRole = roleRepository.findByRole("MANAGER");
        }
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        log.info("User role was changed to {}", userRole);

        return userRepository.save(user);
    }
}