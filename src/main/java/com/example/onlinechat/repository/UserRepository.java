package com.example.onlinechat.repository;

import com.example.onlinechat.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findUserById(UUID id);

    Optional<User> findUserByUsername(String username);

    List<User> findUsersByIdIn(List<UUID> ids);
}
