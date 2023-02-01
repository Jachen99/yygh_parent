package space.jachen.yygh.hosp;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import space.jachen.yygh.hosp.User.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author JaChen
 * @date 2023/2/1 15:11
 */
@SpringBootTest
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate template;

    // 1、增加的方法

    /**
     * 增加数据
     */
    @Test
    public void insert(){
            User user = template
                    .insert(new User(null, "胡歌", 30,
                            "12345@qq.com", new Date()));
            System.out.println("user = " + user);
    }

    /**
     * 批量增加数据
     */
    @Test
    public void batchInsert(){
        User.UserBuilder user1 = User.builder()
                .name("好人1")
                .age(17)
                .email("110@gamil.com")
                .createDate(new Date());

        User.UserBuilder user2 = User.builder()
                .name("好人2")
                .age(18)
                .email("111@gamil.com")
                .createDate(new Date());

        Collection<User.UserBuilder> collection = new ArrayList<>();
        collection.add(user1);
        collection.add(user2);

        // 这里指定要传入的表类型 User.class
        Collection<User.UserBuilder> userBuilders = template.insert(collection,User.class);

        for (User.UserBuilder userBuilder : userBuilders) {
            System.out.println("userBuilder = " + userBuilder);
        }
    }

    // 2、查询的方法

    /**
     * 查询所有数据
     */
    @Test
    public void getAll() {
        List<User> list = template.findAll(User.class);
        for (User user : list) {
            System.out.println("user = " + user);
        }
    }

    /**
     * 根据id查询
     */
    @Test
    public void getUserId() {
        User user = template.findById("63d7b78696d9140e49730444", User.class);
        System.out.println(user);
    }

    /**
     * 条件查询  where name ='jachen' and age=21
     */
    @Test
    public void queryUser() {
        //封装条件
        Query query = new Query(
                Criteria.where("name").is("jachen")
                        .and("age").is(21)
        );
        //调用方法
        List<User> list = template.find(query, User.class);
        System.out.println(list);
    }

    /**
     * 模糊查询  底层没有封装模糊查询方法 ，需要自己编写正则
     */
    @Test
    public void likeQueue(){

        // 正则匹配
        Pattern pattern = Pattern.compile("^.*人.*$");

        // 创建Query对象
        Query query = new Query(Criteria.where("name").regex(pattern));

        // 查询
        List<User> users = template.find(query, User.class);
        for (User user : users) {
            System.out.println("user = " + user);
        }
    }

    /**
     * 分页查询
     */
    @Test
    public void pageQueue(){

        System.out.println("查询第一页 每页俩条数据");

        // 设置当前页
        int page = 1;
        // 设置每页显示的条数
        int pageSize = 2;
        // 设置skip
        int skip = (page-1) * pageSize;

        Query query = new Query().skip(skip).limit(pageSize);

        List<User> users = template.find(query, User.class);

        for (User user : users) {
            System.out.println("user = " + user);
        }

        System.out.println("=====================================");

        System.out.println("查询name是好人，或者年龄大于20的数据");

        // 创建Criteria查询标准
        Criteria criteria = new Criteria();
        Criteria find = criteria.orOperator(Criteria.where("name").is("好人")
                , Criteria.where("age").lt(20));
        List<User> list = template.find(new Query(find), User.class);
        for (User user : list) {
            System.out.println("user = " + user);
        }
    }


    // 3、修改的方法

    /**
     * 修改指定id对象的数据
     */
    @Test
    public void update(){

        User user = template.findById("63d7b78696d9140e49730444", User.class);
        if (user!=null){
            //修改值设置到user对象
            user.setAge(18);
            user.setEmail("9141581@qq.com");
            //修改值封装到update对象里面
            Update update = new Update();
            update.set("age",user.getAge());
            update.set("email",user.getEmail());
            Query query = new Query(Criteria.where("id").is("63d7b78696d9140e49730444"));
            UpdateResult upsert = template.upsert(query, update, User.class);
            long count = upsert.getModifiedCount();
            System.out.println("count = " + count);
        }else {
            System.out.println("修改中断出现异常！");
        }

    }


    // 4、删除的方法

    /**
     * 删除操作
     */
    @Test
    public void delete(){
        Query query =
                new Query(Criteria.where("_id").is("63da209ec19103171259d992"));
        DeleteResult result = template.remove(query, User.class);
        long count = result.getDeletedCount();
        System.out.println(count);
    }

}
