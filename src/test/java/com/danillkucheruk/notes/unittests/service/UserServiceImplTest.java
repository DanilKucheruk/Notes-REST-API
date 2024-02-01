package com.danillkucheruk.notes.unittests.service;

import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.dto.UserDto;
import com.danillkucheruk.notes.mapper.RegistrationUserMapper;
import com.danillkucheruk.notes.mapper.UserMapper;
import com.danillkucheruk.notes.model.User;
import com.danillkucheruk.notes.repository.UserRepository;
import com.danillkucheruk.notes.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RegistrationUserMapper registrationUserMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userMapper, registrationUserMapper);
    }

    @Test
    public void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // given
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = userService.loadUserByUsername(username);

        // then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    public void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        // given
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when, then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void create_ShouldReturnUser_WhenUserDtoIsValid() {
        // given
        RegistrationUserDto userDto = new RegistrationUserDto("newUser", "password");
        User user = new User();
        user.setUsername(userDto.getUsername());

        when(registrationUserMapper.map(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // when
        User createdUser = userService.create(userDto);

        // then
        assertNotNull(createdUser);
        assertEquals(userDto.getUsername(), createdUser.getUsername());

        verify(registrationUserMapper, times(1)).map(userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void findAll_ShouldReturnListOfUserDto_WhenUsersExist() {
        // given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> userList = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.map(user1)).thenReturn(new UserDto(user1.getId(), user1.getUsername(), Collections.emptyList()));
        when(userMapper.map(user2)).thenReturn(new UserDto(user2.getId(), user2.getUsername(), Collections.emptyList()));

        // when
        List<UserDto> userDtoList = userService.findAll();

        // then
        assertNotNull(userDtoList);
        assertEquals(2, userDtoList.size());

        UserDto userDto1 = userDtoList.get(0);
        assertEquals(user1.getId(), userDto1.getId());
        assertEquals(user1.getUsername(), userDto1.getUsername());

        UserDto userDto2 = userDtoList.get(1);
        assertEquals(user2.getId(), userDto2.getId());
        assertEquals(user2.getUsername(), userDto2.getUsername());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).map(user1);
        verify(userMapper, times(1)).map(user2);
    }

    @Test
    public void findById_ShouldReturnUserDto_WhenUserExists() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(new UserDto(user.getId(), user.getUsername(), Collections.emptyList()));

        // when
        Optional<UserDto> userDtoOptional = userService.findById(userId);

        // then
        assertTrue(userDtoOptional.isPresent());

        UserDto userDto = userDtoOptional.get();
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).map(user);
    }

    @Test
    public void findById_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        // given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Optional<UserDto> userDtoOptional = userService.findById(userId);

        // then
        assertFalse(userDtoOptional.isPresent());

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void delete_ShouldReturnTrue_WhenUserExists() {
        // given
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        boolean result = userService.delete(userId);

        // then
        assertTrue(result);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
        verify(userRepository, times(1)).flush();
    }

    @Test
    public void delete_ShouldReturnFalse_WhenUserDoesNotExist() {
        // given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        boolean result = userService.delete(userId);

        // then
        assertFalse(result);

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findByUsername_ShouldReturnUser_WhenUserExists() {
        // given
        String username = "existingUser";
        User user = new User();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // when
        Optional<User> userOptional = userService.findByUsername(username);

        // then
        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void findByUsername_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        // given
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when
        Optional<User> userOptional = userService.findByUsername(username);

        // then
        assertFalse(userOptional.isPresent());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void update_ShouldReturnUpdatedUserDto_WhenUserExists() {
        // given
        Long userId = 1L;
        RegistrationUserDto userDto = new RegistrationUserDto("user1", "newPassword");
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(user)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(new UserDto(user.getId(), user.getUsername(), Collections.emptyList()));

        // when
        Optional<UserDto> updatedUserDtoOptional = userService.update(userId, userDto);

        // then
        assertTrue(updatedUserDtoOptional.isPresent());

        UserDto updatedUserDto = updatedUserDtoOptional.get();
        assertEquals(user.getId(), updatedUserDto.getId());


        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).saveAndFlush(user);
        verify(userMapper, times(1)).map(user);
    }

    @Test
    public void update_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        // given
        Long userId = 1L;
        RegistrationUserDto userDto = new RegistrationUserDto("updatedUser", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Optional<UserDto> updatedUserDtoOptional = userService.update(userId, userDto);

        // then
        assertFalse(updatedUserDtoOptional.isPresent());

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }
}
