package com.example.zhuangqf.endpoint;

import com.example.zhuangqf.entity.Student;
import com.example.zhuangqf.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by zhuangqf on 11/18/16.
 */
@Component
@Path("/student")
@Consumes("application/json")
@Produces("application/json")
public class StudentEndpoint {

    @Autowired
    StudentMapper studentMapper;

    @GET
    @Path("/all.json")
    public List<Student>findAllStudents(){
        List<Student> studentList = studentMapper.findAllStudents();
        return studentList;
    }

    @GET
    @Path("/{id}.json")
    public Student findById(@PathParam("id") int id){
        Student student = studentMapper.findById(id);
        return student;
    }


    @POST
    @Path("/insert.json")
    public Student insert(Student student){
        studentMapper.insert(student);
        return student;
    }

    @POST
    @Path("/update.json")
    public Student update(Student student){
        studentMapper.update(student);
        return student;
    }

    @DELETE
    @Path("/delete/{id}")
    public String delete(@PathParam("id")int id){
        int result = studentMapper.deleteById(id);
        if(result==1) return "delete success";
        return "delete failed";
    }

}
