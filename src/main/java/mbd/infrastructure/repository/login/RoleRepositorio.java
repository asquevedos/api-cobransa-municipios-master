package mbd.infrastructure.repository.login;


import mbd.model.login.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;





public interface RoleRepositorio extends JpaRepository<Role, Long>{

	
    @Query("select r from Role r where r.nombre like :roleName")
    Role findRoleByName(@Param("roleName") String roleName);
}
