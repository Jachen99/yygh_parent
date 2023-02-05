package space.jachen.yygh.hosp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.jachen.yygh.model.hosp.Schedule;

import java.util.Date;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/2/2 10:32
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    Schedule findByHoscodeAndHosScheduleId(String hoscode,String hosScheduleId);

    List<Schedule> findByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date workDate);

}
