package com.lovemesomecoding.pizzaria.entity.user;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lovemesomecoding.pizzaria.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.pizzaria.dto.EntityDTOMapper;
import com.lovemesomecoding.pizzaria.dto.SignUpDTO;
import com.lovemesomecoding.pizzaria.dto.UserDTO;
import com.lovemesomecoding.pizzaria.dto.UserUpdateDTO;
import com.lovemesomecoding.pizzaria.entity.user.role.Authority;
import com.lovemesomecoding.pizzaria.entity.user.role.Role;
import com.lovemesomecoding.pizzaria.security.AuthenticationService;
import com.lovemesomecoding.pizzaria.utils.FileUtils;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;
import com.lovemesomecoding.pizzaria.utils.PasswordUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDAO               userDAO;

    @Autowired
    private EntityDTOMapper       entityMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public AuthenticationResponseDTO signUp(SignUpDTO signUpDTO) {
        log.debug("signUp={}", ObjMapperUtils.toJson(signUpDTO));
        UserUtils.validateSignUp(signUpDTO, userDAO);

        User user = entityMapper.mapSignUpDTOToUser(signUpDTO);

        // save email as lowercase
        user.setEmail(signUpDTO.getEmail().toLowerCase());
        user.setPasswordExpirationDate(LocalDate.now());
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.addRole(new Role(Authority.USER));

        user = userDAO.save(user);

        AuthenticationResponseDTO userAuthenticationSuccessDTO = authenticationService.authenticate(user);

        return userAuthenticationSuccessDTO;

    }

    @Override
    public UserDTO getByUuid(String uuid) {
        // TODO Auto-generated method stub
        User user = userDAO.getByUuid(uuid);

        UserDTO userDTO = entityMapper.mapUserToUserDTO(user);

        return userDTO;
    }

    @Override
    public UserDTO updateProfileImage(String uuid, MultipartFile multipartFile) {
        // TODO Auto-generated method stub
        File file = null;
        try {
            file = FileUtils.convertMultipartFileToFile(multipartFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // String s3Key = AwsS3Folder.PROFILE_IMG.getFolderName() + "/" +
        // RandomGeneratorUtils.getAwsS3FileKey(file.getName());
        //
        // String fileS3Url = awsS3Service.uploadPublicFile(s3Key, file);

        User user = userDAO.getByUuid(uuid);

        // user.setProfileImageUrl(fileS3Url);

        user = userDAO.save(user);

        UserDTO userDTO = entityMapper.mapUserToUserDTO(user);

        return userDTO;
    }

    @Override
    public UserDTO updateProfile(UserUpdateDTO userUpdateDTO) {
        // TODO Auto-generated method stub
        User user = userDAO.getByUuid(userUpdateDTO.getUuid());

        user = entityMapper.patchUpdateUser(userUpdateDTO, user);

        user = userDAO.save(user);

        UserDTO userDTO = entityMapper.mapUserToUserDTO(user);

        return userDTO;
    }

    @Override
    public UserDTO updateCoverImage(String uuid, MultipartFile multipartFile) {
        File file = null;
        try {
            file = FileUtils.convertMultipartFileToFile(multipartFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // String s3Key = AwsS3Folder.COVER_IMG.getFolderName() + "/" +
        // RandomGeneratorUtils.getAwsS3FileKey(file.getName());
        //
        // String fileS3Url = awsS3Service.uploadPublicFile(s3Key, file);

        User user = userDAO.getByUuid(uuid);

        // user.setCoverImageUrl(fileS3Url);

        user = userDAO.save(user);

        UserDTO userDTO = entityMapper.mapUserToUserDTO(user);

        return userDTO;
    }

    @Override
    public AuthenticationResponseDTO freshToken(String refreshToken) {
        // TODO Auto-generated method stub
        return authenticationService.refreshAuthToken(refreshToken);
    }

}
