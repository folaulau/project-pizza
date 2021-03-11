package com.lovemesomecoding.pizzaria.entity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserDAOImp implements UserDAO {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate   jdbcTemplate;

    @Override
    public User save(User user) {
        log.debug("save(..)");
        return userRepository.saveAndFlush(user);

    }

    @Override
    public User getById(Long id) {
        log.debug("getById({})", id);
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByUuid(String uuid) {
        log.debug("getByUuid({})", uuid);
        return userRepository.findByUuid(uuid);

    }

    @Override
    public User getByEmail(String email) {
        log.debug("getByEmail({})", email);
        return userRepository.findByEmail(email);

    }

    @Override
    public boolean doesEmailExist(String email) {
        log.debug("doesEmailExist({})", email);
        StringBuilder query = new StringBuilder();

        query.append("SELECT ");
        query.append("u.id as id ");
        query.append("FROM user as u ");
        query.append("WHERE u.email = ? ");

        log.info("query={}", query.toString());
        try {

            Long id = jdbcTemplate.queryForObject(query.toString(), Long.class, new Object[]{email.toLowerCase()});

            return (id == null || id == 0) ? false : true;
        } catch (Exception e) {
            log.warn("Exception, msg={}", e.getLocalizedMessage());
        }

        return false;
    }
}
