package com.lovemesomecoding.pizzaria.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDAO {

    User save(User user);

    User getById(Long id);

    User getByUuid(String uuid);

    User getByEmail(String email);

    boolean doesEmailExist(String email);
}
