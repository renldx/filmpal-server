package com.renldx.filmpal.server.repository;

import com.renldx.filmpal.server.model.Role;
import com.renldx.filmpal.server.model.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleCode name);

}
