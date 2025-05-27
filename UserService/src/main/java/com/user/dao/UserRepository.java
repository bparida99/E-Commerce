package com.user.dao;

import com.user.dto.CredentialDto;
import com.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"addresses","credential"})
    public User findByEmail(String email);

    @EntityGraph(attributePaths = {"addresses","credential"})
    public List<User> findAll();

    @Query("SELECT new com.user.dto.CredentialDto(u.email, c.passwordHash) " +
            "FROM User u JOIN u.credential c WHERE u.email = :email")
    public CredentialDto findCredentials(@Param("email") String email);
}
