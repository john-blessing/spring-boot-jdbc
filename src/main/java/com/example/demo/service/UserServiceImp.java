package com.example.demo.service;

import com.example.demo.entity.Answer;
import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ClassRoom> findAllClassRoom() {
        String sql = "select * from class_room";

        List<ClassRoom> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ClassRoom.class));

        return list;
    }

    @Override
    public User findUserById(int user_id) {
        String sql = "select c.user_id AS user_id, c.username AS username, c.classroom AS classroom, c.avator AS avator FROM " +
                "(SELECT a.user_id AS user_id, a.username AS username, a.avator AS avator, b.c_name AS classroom FROM " +
                "my_local_db.user a, my_local_db.class_room b " +
                "WHERE a.c_id = b.c_id) c " +
                "WHERE user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{user_id}, new BeanPropertyRowMapper<>(User.class));
        return user;
    }

    @Override
    public User findUser(String username, String password) {
        String sql = "        SELECT \n" +
                "    c.user_id AS user_id,\n" +
                "    c.username AS username,\n" +
                "    c.classroom AS classroom, c.avator AS avator\n" +
                "FROM\n" +
                "    (SELECT \n" +
                "        a.user_id AS user_id,\n" +
                "            a.username AS username,\n" +
                "            b.c_name AS classroom,\n" +
                "            a.password AS password, a.avator AS avator\n" +
                "    FROM\n" +
                "        my_local_db.user a, my_local_db.class_room b\n" +
                "    WHERE\n" +
                "        a.c_id = b.c_id) c\n" +
                "WHERE\n" +
                "    c.username = ? \n" +
                "        AND c.password = ?;";
        List<User> res = jdbcTemplate.query(sql, new Object[]{username, password}, new BeanPropertyRowMapper<>(User.class));
        return res.size() > 0 ? res.get(0) : null;
    }

    @Override
    public int register(String username, String password) {
        String sql = "insert into my_local_db.user values(NULL,?, ?, 0);";
        int rows = jdbcTemplate.update(sql, username, password);
        return rows;
    }

    @Override
    public List<Question> searchQuestions(String content, int page_index, int page_size) {
        String sql = "select * from my_local_db.question where q_content like ? limit ?, ?;";
        String content1 = "%" + content + "%";
        List<Question> question = jdbcTemplate.query(sql, new Object[]{content1, page_index * page_size, page_size}, (ResultSet rs, int rowNum) -> {
            Question question1 = new Question();
            question1.setQ_type(rs.getString("q_type"));
            question1.setQ_content(rs.getString("q_content"));
            question1.setQ_id(rs.getInt("q_id"));
            JSONArray jsonArray = new JSONArray(rs.getString("q_answer"));
            List<Answer> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Answer answer = new Answer();
                JSONObject obj = (JSONObject) jsonArray.get(i);
                answer.setId((String) obj.get("id"));
                answer.setContent((String) obj.get("content"));
                list.add(answer);
            }
            question1.setQ_answer(list);
            return question1;
        });
        return question;
    }

    @Override
    public int createQuestion(Question question) {
        String sql = "insert into my_local_db.question values(NULL, ?, ?, ?)";
        int rows = jdbcTemplate.update(sql, question.getQ_type(), question.getQ_content(), JSONObject.valueToString(question.getQ_answer()));
        return rows;
    }

    @Override
    public long findAllQuestionCount() {
        String sql = "select count(q_id) as total_count from my_local_db.question";
        int total_count = jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) ->  rs.getInt("total_count"));
        System.out.println(">>>>>>>>>.enter " + total_count);
        return total_count;
    }

    @Override
    public Map getArticleById(int id) {
        String sql = "select title, content from my_local_db.article where a_id = " + id;
        Map hashMap = (Map) jdbcTemplate.queryForMap(sql);

        return hashMap;
    }
}
