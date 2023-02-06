package space.jachen.yygh.hosp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.jachen.yygh.model.hosp.Hospital;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/2/2 10:32
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    Hospital findByHoscode(String hoscode);

    List<Hospital> findHospitalByHosnameLike(String hosname);
}
