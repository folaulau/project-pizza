package com.lovemesomecoding.pizzaria.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<User, Long> {

    User findByUuid(String uuid);

    User findByEmail(String email);

}
