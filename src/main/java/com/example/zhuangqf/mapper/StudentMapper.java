package com.example.zhuangqf.mapper;

import com.example.zhuangqf.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhuangqf on 11/18/16.
 */
@Mapper
public interface StudentMapper {

    @Select("select * from student")
    List<Student>findAllStudents();

    @Select("select * from student where id = #{id}")
    Student findById(@Param("id")int id);

    @Insert("insert student(name,age) values (#{name},#{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Student student);

    @Update("update student set name=#{name},age=#{age} where id=#{id}")
    int update(Student student);

    @Delete("delete from student where id = #{id}")
    int deleteById(@Param("id")int id);

}
