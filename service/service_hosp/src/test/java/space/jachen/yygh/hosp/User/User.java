package space.jachen.yygh.hosp.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author JaChen
 * @date 2023/2/1 15:12
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("User")
@Builder
public class User {
    @Id
    private String id;
    private String name;
    private Integer age;
    private String email;
    private Date createDate;
}
