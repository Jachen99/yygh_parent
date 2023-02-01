package space.jachen.yygh.hosp.mapper;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.jachen.yygh.hosp.User.User;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/2/1 18:43
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {

    // 如果方法名发神经报红 可能是ide自动测的问题
    // 代码可跑通不影响
    List<User> findByNameLike(String name);

    List<User> findByAgeAfterOrName(Integer age,String name);

    List<User> findByEmailIsNotNull();

}
