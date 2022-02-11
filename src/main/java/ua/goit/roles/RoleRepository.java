package ua.goit.roles;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
//  Role findByName(String roleName);
}