package com.lovemesomecoding.pizzaria.entity.user;

import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lovemesomecoding.pizzaria.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.pizzaria.dto.CustomPage;
import com.lovemesomecoding.pizzaria.dto.EntityDTOMapper;
import com.lovemesomecoding.pizzaria.dto.SignUpDTO;
import com.lovemesomecoding.pizzaria.dto.UserDTO;
import com.lovemesomecoding.pizzaria.dto.UserSessionDTO;
import com.lovemesomecoding.pizzaria.dto.UserUpdateDTO;
import com.lovemesomecoding.pizzaria.entity.user.session.UserSession;
import com.lovemesomecoding.pizzaria.entity.user.session.UserSessionService;
import com.lovemesomecoding.pizzaria.exception.ApiException;
import com.lovemesomecoding.pizzaria.security.AuthenticationType;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Api(value = "users", tags = "Users")
@Slf4j
@RestController
public class UserRestController {

    @Autowired
    private UserService        userService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private EntityDTOMapper    entityDTOMapper;

    /**
     * sign up
     */
    @ApiOperation(value = "Sign Up", notes = "${user.rest.controller.signup}")
    @PostMapping(value = "/users/signup")
    public ResponseEntity<AuthenticationResponseDTO> signUp(@RequestHeader(name = "client-id", required = true) String clientId,
            @ApiParam(name = "user", value = "user sign up", required = true) @RequestBody SignUpDTO signUpDTO) {
        log.info("sign up {}", ObjMapperUtils.toJson(signUpDTO));

        AuthenticationResponseDTO userAuthenticationSuccessDTO = userService.signUp(signUpDTO);

        return new ResponseEntity<>(userAuthenticationSuccessDTO, OK);
    }

    /**
     * log in
     */
    @ApiOperation(value = "Log In")
    @PostMapping(value = "/users/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestHeader(name = "client-id", required = true) String clientId, @RequestParam(name = "type", required = true) String loginType,
            @RequestHeader(name = "Authorization", required = true) String authorization) {
        log.info("login authorization={}", ObjMapperUtils.toJson(authorization));
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Refresh auth token")
    @PostMapping(value = "/users/token/refresh")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestHeader(name = "client-id", required = true) String clientId,
            @RequestParam(name = "refreshToken", required = true) String refreshToken) {
        log.info("refreshToken={}", ObjMapperUtils.toJson(refreshToken));
        AuthenticationResponseDTO newAuth = userService.freshToken(refreshToken);
        log.info("newAuth={}", ObjMapperUtils.toJson(newAuth));
        return new ResponseEntity<>(newAuth, OK);
    }

    @ApiOperation(value = "Get User")
    @GetMapping(value = "/users/{uuid}")
    public ResponseEntity<UserDTO> getByUuid(@RequestHeader(name = "token", required = true) String token, @ApiParam(name = "uuid", value = "user uuid", required = true) @PathVariable String uuid) {
        log.info("getByUuid({})", uuid);

        UserDTO userDTO = userService.getByUuid(uuid);

        log.info("userDTO={}", ObjMapperUtils.toJson(userDTO));

        if (userDTO == null) {
            throw new ApiException("User not found", "User not found by uuid=" + uuid);
        }

        return new ResponseEntity<>(userDTO, OK);
    }

    @ApiOperation(value = "Update Profile")
    @PutMapping(value = "/users/profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestHeader(name = "token", required = true) String token,
            @ApiParam(name = "user", value = "user profile dto", required = true) @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("updateProfile {}", ObjMapperUtils.toJson(userUpdateDTO));

        UserDTO userDTO = userService.updateProfile(userUpdateDTO);

        return new ResponseEntity<>(userDTO, OK);
    }

    @ApiOperation(value = "Update Profile Image")
    @PutMapping(value = "/users/profile-image")
    public ResponseEntity<UserDTO> updateProfileImage(@RequestHeader(name = "token", required = true) String token,
            @ApiParam(name = "uuid", value = "user uuid", required = true) @RequestParam("uuid") String uuid,
            @ApiParam(name = "file", required = true, value = "profile image") @RequestPart(value = "file") MultipartFile file) {
        log.info("updateProfileImg {}", uuid);

        UserDTO userDTO = userService.updateProfileImage(uuid, file);

        return new ResponseEntity<>(userDTO, OK);
    }

    @ApiOperation(value = "Update Cover Image")
    @PutMapping(value = "/users/cover-image")
    public ResponseEntity<UserDTO> updateCoverImage(@RequestHeader(name = "token", required = true) String token,
            @ApiParam(name = "uuid", value = "user uuid", required = true) @RequestParam("uuid") String uuid,
            @ApiParam(name = "file", required = true, value = "cover image") @RequestPart(value = "file") MultipartFile file) {
        log.info("updateProfileImg {}", uuid);

        UserDTO userDTO = userService.updateCoverImage(uuid, file);

        return new ResponseEntity<>(userDTO, OK);
    }

    @ApiOperation(value = "Log out")
    @PutMapping(value = "/users/logout")
    public ResponseEntity<Boolean> logOut(@RequestParam("token") String token) {
        log.info("logOut {}", token);

        return new ResponseEntity<>(new Boolean(true), OK);
    }

    @ApiOperation(value = "Get user active sessions")
    @GetMapping(value = "/users/{uuid}/sessions/active")
    public ResponseEntity<CustomPage<UserSessionDTO>> getUserActiveSessions(@RequestHeader("token") String token,
            @ApiParam(name = "page", value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer pageNumber,
            @ApiParam(name = "size", value = "size", defaultValue = "20") @RequestParam(required = false, name = "size", defaultValue = "20") Integer pageSize, @PathVariable String uuid) {
        log.info("getUserActiveSessions, uuid={}", uuid);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<UserSession> page = userSessionService.getActiveSessionsByUserUuid(uuid, pageable);

        List<UserSessionDTO> ssnDtos = null;

        if (page != null && page.hasContent()) {
            ssnDtos = entityDTOMapper.mapUserSessionsToUserSessionDTOs(page.getContent());
        } else {
            ssnDtos = new ArrayList<>();
        }

        CustomPage<UserSessionDTO> result = new CustomPage<UserSessionDTO>(new PageImpl<UserSessionDTO>(ssnDtos, pageable, page.getTotalElements()));

        return new ResponseEntity<>(result, OK);
    }

    @ApiOperation(value = "Get user sessions")
    @GetMapping(value = "/users/{uuid}/sessions")
    public ResponseEntity<CustomPage<UserSessionDTO>> getUserSessions(@RequestHeader("token") String token,
            @ApiParam(name = "page", value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer pageNumber,
            @ApiParam(name = "size", value = "size", defaultValue = "20") @RequestParam(required = false, name = "size", defaultValue = "20") Integer pageSize, @PathVariable String uuid) {
        log.info("getUserActiveSessions, uuid={}", uuid);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<UserSession> page = userSessionService.getSessionsByUserUuid(uuid, pageable);

        List<UserSessionDTO> ssnDtos = null;

        if (page != null && page.hasContent()) {
            ssnDtos = entityDTOMapper.mapUserSessionsToUserSessionDTOs(page.getContent());
        } else {
            ssnDtos = new ArrayList<>();
        }

        CustomPage<UserSessionDTO> result = new CustomPage<UserSessionDTO>(new PageImpl<UserSessionDTO>(ssnDtos, pageable, page.getTotalElements()));

        return new ResponseEntity<>(result, OK);
    }

}
