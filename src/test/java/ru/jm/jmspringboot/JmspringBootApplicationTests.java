package ru.jm.jmspringboot;

import org.junit.jupiter.api.Test;
import static  org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.jm.jmspringboot.model.Role;
import ru.jm.jmspringboot.model.User;
import ru.jm.jmspringboot.service.UserService;

@SpringBootTest
class JmspringBootApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    Role getAdminRole() {
        Role admRole = null;
        try {
            admRole = userService.getAdminRole();
        } catch (EmptyResultDataAccessException ex) {
            userService.createRoles();
            admRole = userService.getAdminRole();
        }
        return admRole;
    }

    @Test
    void testPresentAdminUser() {
        //  создаёт пользователя ADMIN с паролем 100 если такой не существует (и роли заодно :)
        Role admRole = getAdminRole();

        User admUser = userService.getUser("ADMIN");

        if (admUser == null) {

            assertEquals(admRole.getRoleName(),"ROLE_ADMIN");
            User user = new User();
            user.setUsername("ADMIN");
            user.setPassword("100");
            userService.updateUserRoles(user,true, false);
            userService.saveUser(user);
            admUser = userService.getUser("ADMIN");
        }
        assertEquals(admUser.getUsername(),"ADMIN");
    }


}
