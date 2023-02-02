package space.jachen.yygh.hosp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.jachen.yygh.model.hosp.Department;

/**
 * @author JaChen
 * @date 2023/2/2 10:33
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    Department findByHoscodeAndDepcode(String hoscode,String depcode);
}
