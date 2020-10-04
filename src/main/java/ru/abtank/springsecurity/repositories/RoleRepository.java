package ru.abtank.springsecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.abtank.springsecurity.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
