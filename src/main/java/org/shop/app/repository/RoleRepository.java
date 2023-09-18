package org.shop.app.repository;

import org.shop.app.entity.ClientRoles;
import org.shop.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findRoleByRoleName(ClientRoles clientRoles);

}
