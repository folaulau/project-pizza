package com.lovemesomecoding.pizzaria.entity.user;

import com.lovemesomecoding.pizzaria.dto.SignUpDTO;
import com.lovemesomecoding.pizzaria.exception.ApiErrorResponse;
import com.lovemesomecoding.pizzaria.exception.ApiException;
import com.lovemesomecoding.pizzaria.utils.ValidationUtils;

public interface UserUtils {

    public static void validateSignUp(SignUpDTO signUpDTO, UserDAO userDAO) {

        if (signUpDTO == null) {
            throw new ApiException(ApiErrorResponse.DEFAULT_MSG, "Signup request data is empty");
        }

        if (signUpDTO.getEmail() == null || ValidationUtils.isValidEmailFormat(signUpDTO.getEmail()) == false) {
            throw new ApiException("Email is invalid", signUpDTO.getEmail() + " is invalid");
        }

        if (signUpDTO.getPassword() == null || signUpDTO.getPassword().length() == 0) {
            throw new ApiException("Password is invalid", "Password is empty");
        }

        if (ValidationUtils.isValidPassword(signUpDTO.getPassword()) == false) {
            throw new ApiException("Password is invalid", "Password is not strong enough");
        }

        // db checks

        boolean emailExist = userDAO.doesEmailExist(signUpDTO.getEmail());

        if (emailExist) {
            throw new ApiException("Email taken", signUpDTO.getEmail() + " is token");
        }

    }
}
