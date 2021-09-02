package com.yashablendeer.carhire.service;

import com.yashablendeer.carhire.model.Role;
import com.yashablendeer.carhire.model.User;
import com.yashablendeer.carhire.repo.RoleRepository;
import com.yashablendeer.carhire.repo.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void initUseCase() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findUserByUserId() {
        User user = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(new HashSet<Role>(Arrays.asList(new Role(2, "USER"))))
                .build();

        when(userRepository.findUserById(anyInt())).thenReturn(user);
        User foundUser = userService.findUserByUserId(113);
        assertNotNull(foundUser);
        assertEquals("johnny", foundUser.getUserName());
    }

    @Test
    public void findUserByEmail() {
        Role userRole = roleRepository.findByRole("USER");
        Set<Role> roles = new HashSet<Role>();
        roles.add(userRole);
        User user = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(roles)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        User foundUser = userService.findUserByEmail("nice@gmail.com");
        assertNotNull(foundUser);
        assertEquals("Levaska", foundUser.getLastName());
    }

    @Test
    public void findUserByUserName() {
        Role userRole = roleRepository.findByRole("USER");
        Set<Role> roles = new HashSet<Role>();
        roles.add(userRole);
        User user = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(roles)
                .build();

        when(userRepository.findByUserName(anyString())).thenReturn(user);

        User foundUser = userService.findUserByUserName("johnny");
        assertNotNull(foundUser);
        assertEquals("Levaska", foundUser.getLastName());
    }

    @Test
    public void findAllUsers() {
        Role userRole = roleRepository.findByRole("USER");
        Set<Role> roles = new HashSet<Role>();
        roles.add(userRole);
        User user1 = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(roles)
                .build();

        User user2 = User.builder()
                .id(5)
                .active(false)
                .email("karpaccio@gmail.com")
                .name("Mika")
                .lastName("Gokanto")
                .userName("mokoli")
                .password(bCryptPasswordEncoder.encode("mokoli"))
                .roles(roles)
                .build();
        List list = new ArrayList();
        Collections.addAll(list, user1, user2);
        when(userRepository.findAll()).thenReturn(list);

        List result = userService.findAllUsers();

        verify(userRepository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void saveUser() {
        Role userRole = roleRepository.findByRole("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = User.builder()
                .id(113)
                .active(true)
                .email("nice@gmail.com")
                .name("John")
                .lastName("Levaska")
                .userName("johnny")
                .password(bCryptPasswordEncoder.encode("johnny"))
                .roles(roles)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        User foundUser = userService.saveUser(user);
        assertEquals(113, foundUser.getId());
        verify(userRepository, times(1)).save(user);
    }
}