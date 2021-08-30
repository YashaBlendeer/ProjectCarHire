package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Role;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.RoleRepository;
import com.yashablendeer.carhire.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public  List<String> findAllUsersByName() {
        return userRepository.findAll().stream().map(x -> x.getName()).collect(Collectors.toList());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    //changes are here {Sort}
//    public Page<User> findAllUsers(Pageable pageable) {
//        return userRepository.findAll(pageable);
//    }

    public Page<User> findAllUsersPageable(PageRequest page) {
        return userRepository.findAll(page);
    }

    //TODO understand this method
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("USER"); //???
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    //TODO refactor
    public User banHandler(int userId) {
        User user = findUserByUserId(userId);
        if (user.getActive()) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }

        return userRepository.save(user);
    }

    //TODO refactor
    public User managerUpgrade(int id) {
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
        return userRepository.save(user);
    }


}