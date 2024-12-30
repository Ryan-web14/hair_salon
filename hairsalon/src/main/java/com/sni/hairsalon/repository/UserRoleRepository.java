package com.sni.hairsalon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sni.hairsalon.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
    UserRole findUserRoleByName(String name);
    UserRole findUserRoleById(long Id);

}
