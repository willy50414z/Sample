package com.willy.ibatis.entity;

import java.util.ArrayList;
import java.util.List;

import com.willy.ibatis.entity.Student;

public interface IStudentDao {
	List<Student> findAll();           
    Student findById(int id);           
    List<Student> findByName(String name);
    void addStudent(Student student);      
    void updateStudent(Student student);
    void deleteById(int id);          
    void batchInsert(ArrayList<Student> student);
}
