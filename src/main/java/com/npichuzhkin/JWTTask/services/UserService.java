package com.npichuzhkin.JWTTask.services;

import com.npichuzhkin.JWTTask.dto.AuthRequestDTO;
import com.npichuzhkin.JWTTask.dto.UserDTO;
import com.npichuzhkin.JWTTask.models.User;
import com.npichuzhkin.JWTTask.repositories.UserRepository;
import com.npichuzhkin.JWTTask.security.UserDetailsImpl;
import com.npichuzhkin.JWTTask.utils.LoggingFilter;
import com.npichuzhkin.JWTTask.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll(){
        return userRepository.findAll().stream().map(userMapper::mapToDTO).toList();
    }

    @Transactional(readOnly = true)
    public UserDTO findByUserName(String username){
        return userMapper.mapToDTO(userRepository.findById(username).orElseThrow(() ->
                new EntityNotFoundException("User with this username was not found")));
    }

    @Transactional
    public void save(UserDTO newUser){
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        newUser.setPassword(bc.encode(newUser.getPassword()));
        newUser.setNonLocked(true);
        userRepository.save(userMapper.mapToModel(newUser));
    }

    @Transactional
    public void update(String username, UserDTO userDTO){
        userDTO.setUsername(username);
        userRepository.save(userMapper.mapToModel(userDTO));
    }

    @Transactional
    public void delete(String username){
        userRepository.deleteById(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    public void increaseFailedAttempts(AuthRequestDTO authRequestDTO) {
        UserDTO userDTO = findByUserName(authRequestDTO.getUsername());
        User user = userMapper.mapToModel(userDTO);
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        userRepository.save(user);
    }

    public void resetFailedAttempts(UserDTO userDTO) {
        User user = userMapper.mapToModel(userDTO);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    public void lockUser(AuthRequestDTO authRequestDTO) {
        UserDTO userDTO = findByUserName(authRequestDTO.getUsername());
        User user = userMapper.mapToModel(userDTO);
        user.setNonLocked(false);
        LoggingFilter.logAccountLocked(userDTO.getUsername());
        userRepository.save(user);
    }

    public void unlockUserAccount(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setNonLocked(true);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }
}
