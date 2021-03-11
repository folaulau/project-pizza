package com.lovemesomecoding.pizzaria.entity.user;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.lovemesomecoding.pizzaria.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.pizzaria.dto.SignUpDTO;
import com.lovemesomecoding.pizzaria.dto.UserDTO;
import com.lovemesomecoding.pizzaria.dto.UserUpdateDTO;

public interface UserService {

    AuthenticationResponseDTO signUp(SignUpDTO signUpDTO);

    UserDTO getByUuid(String uuid);

    UserDTO updateProfileImage(String uuid, MultipartFile file);

    UserDTO updateProfile(UserUpdateDTO userUpdateDTO);

    UserDTO updateCoverImage(String uuid, MultipartFile file);

    AuthenticationResponseDTO freshToken(String refreshToken);

}
