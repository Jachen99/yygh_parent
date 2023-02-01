package space.jachen.yygh.hosp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import space.jachen.yygh.hosp.User.User;
import space.jachen.yygh.hosp.mapper.UserRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author JaChen
 * @date 2023/2/1 16:54
 */
@SpringBootTest
public class MongoRepositoryTest {

    @Autowired
    private UserRepository repository;

    // 1、增加的方法

    /**
     * 增加一条数据
     *
     * 比较insert和save
     * 两种方法的主要区别在于它们是否允许存在相同的 _id 值。
     * 如果要插入新文档，那么可以使用 repository.insert()；
     * 如果要更新已有文档，则可以使用 repository.save()。
     */
    @Test
    public void insert(){
        User user = User.builder().createDate(new Date()).email("hahaha@abc.cn")
                .age(66).name("太上老君1").build();
        repository.insert(user);
        //repository.save(user);
    }

    /**
     * 增加多条数据
     */
    @Test
    public void batchInsert(){

        User user2 = User.builder().createDate(new Date()).email("hahaha@abc.cn")
                .age(66).name("太上老君2").build();
        User user3 = User.builder().createDate(new Date()).email("hahaha@abc.cn")
                .age(66).name("太上老君3").build();
        List<User> users = Arrays.asList(user3, user2);
        repository.saveAll(users);
    }

    // 2、查询操作
    /**
     * 查询全部 并根据id查询
     */
    @Test
    public void queryById(){

        List<User> users = repository.findAll();
        for (User user : users) {
            System.out.println("user = " + user);
        }

        Optional<User> user = repository.findById("63da07d1bfe3447c655f0e6f");
        System.out.println("user = " + user);

    }

    /**
     * 条件查询 where name='jachen' and age=18  findAll
     */
    @Test
    public void queryByNameAndAge(){

        User user = User.builder().name("太上老君").age(66).build();
        System.out.println("user = " + user);
        // Example根据给定的示例对象，查询数据库中与该示例对象匹配的数据。
        Example<User> example = Example.of(user);
        System.out.println("example = " + example);

        // 注意：如果只有表中一个数据查不出来 list为null
        /**
         * 为什么出现这种情况呢？
         * 1、数据库中没有与示例对象匹配的数据：请检查数据库中是否有与示例对象匹配的数据，并确保匹配条件是否正确。
         * 2、查询代码有错误：请检查查询代码，确保没有任何语法错误或逻辑错误。
         * 3、Example对象中没有填写完整的条件：示例对象只需包含需要匹配的字段，其他字段的值可以是 null，
         * 但是如果存在需要匹配的字段没有填写值，则该字段的匹配条件不完整，可能导致查询结果为空。
         * 4、我的错误是：数据库里的字段没有指定类型为space.jachen.yygh.hosp.User.User 出现空值
         *
         */
        List<User> userList = repository.findAll(example);
        userList.forEach(user1 -> {
            System.out.println("user1 = " + user1);
        });
    }

    /**
     * 条件查询 findOne()
     */
    @Test
    public void test(){
        //封装条件
        User user = new User();
        user.setAge(66);
        Example<User> example = Example.of(user);
        //调用方法得到结果 如果多条匹配打印第一条
        User user1 = repository.findOne(example).get();
        System.out.println(user1);
    }


    /**
     * 模糊查询
     */
    @Test
    public void findQueryLike() {

        //创建模糊查询匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        User user = new User();
        user.setName("上");
        Example<User> example = Example.of(user,matcher);

        // where name like '上'
        List<User> list = repository.findAll(example);
        list.forEach(System.out::println);
    }


    /**
     * 条件分页查询带排序
     */
    @Test
    public void findQueryPage() {
        //设置排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "age");

        //设置分页参数
        //第一个参数当前页  0代表第一页
        //第二个参数 每页显示记录数
        //第三个参数 排序规则对象
        Pageable pageable = PageRequest.of(0, 2, sort);

        //创建模糊查询匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        User user = new User();
        user.setName("上");
        Example<User> example = Example.of(user,matcher);

        //调用方法实现查询
        Page<User> pages = repository.findAll(example, pageable);
        List<User> list = pages.getContent();
        list.forEach(System.out::println);
    }


    // 自定义查询方法：可以简化操作

    /**
     * 根据名字模糊查询 带分页
     */
    @Test
    public void findByNameLike(){
        List<User> users = repository.findByNameLike("上");
        /*users.forEach(user -> {
            System.out.println("user = " + user);
        });*/
        users.forEach(System.out::println);
    }

    /**
     * 查询age>20岁的或者name=ceci的数据
     */
    @Test
    public void findByAgeAfterOrName(){
        List<User> users = repository.findByAgeAfterOrName(20, "ceci");
        users.forEach(System.out::println);
    }

    /**
     * 查询邮箱不是null的list
     */
    @Test
    public void findByEmailIsNotNull(){
        List<User> userList = repository.findByEmailIsNotNull();
        userList.forEach(System.out::println);
    }



}
