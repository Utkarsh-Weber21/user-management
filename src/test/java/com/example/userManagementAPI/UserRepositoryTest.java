package com.example.userManagementAPI;

import com.example.userManagementAPI.model.User;
import com.example.userManagementAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        user1 = new User("Alice singh", "allicesingh@gmail.com");
        user2 = new User("Bob raj", "bobraj@gmail.com");

        entityManager.persist(user1);
        entityManager.persist(user2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByEmailIdFound(){
        Optional<User> foundUser = userRepository.findByEmail(user1.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(user1.getName());
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(foundUser.get().getId()).isEqualTo(user1.getId());
    }

    @Test
    void testFindByEmailIdNotFound(){
        Optional<User> notFoundUser = userRepository.findByEmail("abc@gmail.com");
        assertThat(notFoundUser).isNotPresent();
    }

    @Test
    void testSaveUser(){
        User newUser = new User("Jack Kumar","jackkumar@gmail.com");

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser).isNotNull();
        assertThat(newUser.getName()).isEqualTo("Jack Kumar");
        assertThat(newUser.getEmail()).isEqualTo("jackkumar@gmail.com");
    }

    @Test
    void testUpdateUser() throws Exception{

        String newName = "jack raj";
        String newEmail = "jackj@gmail.com";

        user3 = new User("Marsh","marsh@gmail.com");

        user3.setName(newName);
        user3.setEmail(newEmail);

        User updatedUser3 = userRepository.save(user3);
        assertThat(updatedUser3.getId()).isNotNull();
        assertThat(updatedUser3.getName()).isEqualTo(newName);
        assertThat(updatedUser3.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void deleteUser(){
        userRepository.delete(user2);
        Optional<User> users = userRepository.findById(user2.getId());
        assertThat(users).isNotPresent();
    }
}