package greenfield.group.com.menuservice.repository;

import greenfield.group.com.menuservice.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {
    List<Menu> findByOwnerRoleIn(String role);

    List<Menu> findByOwnerRoleIn(List<String> roleList);
}
