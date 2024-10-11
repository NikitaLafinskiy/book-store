package com.bookstore.domain.user.repository;

import com.bookstore.domain.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.role = :roleName")
    Role findByRoleName(Role.RoleName roleName);
}
