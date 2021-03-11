package com.lovemesomecoding.pizzaria.data_loader;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.pizzaria.entity.address.Address;
import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.entity.user.UserDAO;
import com.lovemesomecoding.pizzaria.entity.user.UserGender;
import com.lovemesomecoding.pizzaria.entity.user.UserMaritalStatus;
import com.lovemesomecoding.pizzaria.entity.user.UserStatus;
import com.lovemesomecoding.pizzaria.entity.user.role.Authority;
import com.lovemesomecoding.pizzaria.entity.user.role.Role;
import com.lovemesomecoding.pizzaria.utils.PasswordUtils;

/**
 * It implements Spring Boot’s CommandLineRunner so that it gets run after all the beans are created and registered.
 * 
 * @author folaukaveinga
 *
 */
@Profile({"local"})
public class UserLoader implements CommandLineRunner{

    @Autowired
    private UserDAO userDAO;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUuid("user-33cdbbdd-75ed-44e3-8007-db8b7b8c3808");
        user.setFirstName("Folau");
        user.setLastName("Kaveinga");
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(LocalDate.of(1986, 12, 03));
        user.setEmail("folaudev@gmail.com");
        user.setPhoneNumber("3109934731");
        user.setPassword(PasswordUtils.hashPassword("Test1234!"));
        user.setGender(UserGender.MALE);
        user.setPasswordExpirationDate(LocalDate.now().plusYears(1));
        user.setMaritalStatus(UserMaritalStatus.MARRIED);
        user.setAboutMe("I am so cool you dont even know");

        Address address = new Address();
        address.setCity("Lehi");
        address.setId(1L);
        address.setState("UT");
        address.setStreet("123 test rd");
        address.setZipcode("84043");

        user.setAddress(address);
     
        user.addRole(new Role( Authority.USER));
        user.addRole(new Role( Authority.ADMIN));

        userDAO.save(user);

        user = new User();
        user.setId(2L);
        user.setUuid("user-22cdbbdd-75ed-44e3-8007-db8b7b8c3808");
        user.setFirstName("Lisa");
        user.setLastName("Kaveinga");
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(LocalDate.of(1987, 12, 03));
        user.setEmail("folaudev+1@gmail.com");
        user.setPhoneNumber("3109934731");
        user.setPassword(PasswordUtils.hashPassword("Test1234!"));
        user.setGender(UserGender.MALE);
        user.setPasswordExpirationDate(LocalDate.now().plusYears(1));
        user.setMaritalStatus(UserMaritalStatus.MARRIED);
        user.setAboutMe("I am so cool you dont even know");

        address = new Address();
        address.setCity("Lehi");
        address.setId(2L);
        address.setState("UT");
        address.setStreet("123 test rd");
        address.setZipcode("84043");

        user.setAddress(address);
        user.addRole(new Role( Authority.USER));

        userDAO.save(user);

        user = new User();
        user.setId(3L);
        user.setUuid("user-22cdbbdd-75ed-44e3-8007-db8b7b8c3832");
        user.setFirstName("Laulau");
        user.setLastName("Kaveinga");
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(LocalDate.of(2011, 12, 03));
        user.setEmail("folaudev+2@gmail.com");
        user.setPhoneNumber("3109934731");
        user.setPassword(PasswordUtils.hashPassword("Test1234!"));
        user.setGender(UserGender.MALE);
        user.setPasswordExpirationDate(LocalDate.now().plusYears(1));
        user.setMaritalStatus(UserMaritalStatus.SINGLE);
        user.setAboutMe("I am so cool you dont even know");

        address = new Address();
        address.setCity("Lehi");
        address.setId(3L);
        address.setState("UT");
        address.setStreet("123 test rd");
        address.setZipcode("84043");

        user.setAddress(address);
        user.addRole(new Role( Authority.ADMIN));

        userDAO.save(user);

        user = new User();
        user.setId(4L);
        user.setUuid("user-22cdbbdd-75ed-44e3-8007-db8b7b8c3831");
        user.setFirstName("Kinga");
        user.setLastName("Kaveinga");
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(LocalDate.of(2013, 12, 03));
        user.setEmail("folaudev+3@gmail.com");
        user.setPhoneNumber("3109934731");
        user.setPassword(PasswordUtils.hashPassword("Test1234!"));
        user.setGender(UserGender.MALE);
        user.setPasswordExpirationDate(LocalDate.now().plusYears(1));
        user.setMaritalStatus(UserMaritalStatus.SINGLE);
        user.setAboutMe("I am so cool you dont even know");

        address = new Address();
        address.setCity("Lehi");
        address.setId(4L);
        address.setState("UT");
        address.setStreet("123 test rd");
        address.setZipcode("84043");

        user.setAddress(address);
        user.addRole(new Role( Authority.USER));
        user.addRole(new Role( Authority.ADMIN));

        userDAO.save(user);

        user = new User();
        user.setId(5L);
        user.setUuid("user-22cdbbdd-75ed-44e3-8007-db8b7b8c3839");
        user.setFirstName("Fusi");
        user.setLastName("Kaveinga");
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(LocalDate.of(2015, 12, 03));
        user.setEmail("folaudev+4@gmail.com");
        user.setPhoneNumber("3109934731");
        user.setPassword(PasswordUtils.hashPassword("Test1234!"));
        user.setGender(UserGender.FEMALE);
        user.setPasswordExpirationDate(LocalDate.now().plusYears(1));
        user.setMaritalStatus(UserMaritalStatus.SINGLE);
        user.setAboutMe("I am so cool you dont even know");

        address = new Address();
        address.setCity("Lehi");
        address.setId(5L);
        address.setState("UT");
        address.setStreet("123 test rd");
        address.setZipcode("84043");

        user.setAddress(address);
        user.addRole(new Role( Authority.USER));

        userDAO.save(user);

        user = new User();
        user.setId(6L);
        user.setUuid("user-22cdbbdd-75ed-44e3-8007-db8b7b8c3856");
        user.setFirstName("Mele");
        user.setLastName("Kaveinga");
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(LocalDate.of(2020, 12, 03));
        user.setEmail("folaudev+5@gmail.com");
        user.setPhoneNumber("3109934731");
        user.setPassword(PasswordUtils.hashPassword("Test1234!"));
        user.setGender(UserGender.FEMALE);
        user.setPasswordExpirationDate(LocalDate.now().plusYears(1));
        user.setMaritalStatus(UserMaritalStatus.SINGLE);
        user.setAboutMe("I am so cool you dont even know");

        address = new Address();
        address.setCity("Lehi");
        address.setId(6L);
        address.setState("UT");
        address.setStreet("123 test rd");
        address.setZipcode("84043");

        user.setAddress(address);
        user.addRole(new Role( Authority.ADMIN));

        userDAO.save(user);
    }

}
