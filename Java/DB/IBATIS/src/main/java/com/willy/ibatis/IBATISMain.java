package com.willy.ibatis;
import java.util.ArrayList;
import java.util.List;

import com.willy.ibatis.entity.IStudentDao;
import com.willy.ibatis.entity.Student;
import com.willy.ibatis.entity.StudentDaoImpl;

public class IBATISMain {
	public static void main(String[] args) {
		
		try {
			IStudentDao dao = new StudentDaoImpl();  
	        List<Student> students = null;
	        System.out.println("-----  查詢所有Student  -----");
	        students = dao.findAll();
	        System.out.println(students);
	        System.out.println("-----  查詢Student By ID  -----");
	        Student student;
	        student=dao.findById(2);
	        System.out.println(student);
	        System.out.println("-----  查詢Student By Name  -----");
	        students=dao.findByName("Willy");
	        System.out.println(students);
	        System.out.println("-----  Insert Student  -----");
	        Student student_Add=new Student();
	        student_Add.setId(5);
	        student_Add.setAge(52);
	        student_Add.setName("youngMan");
	        dao.addStudent(student_Add);
	        student=dao.findById(student_Add.getId());
	        System.out.println(student);
	        System.out.println("-----  Update Student  -----");
	        student_Add.setName("oldMan");
	        dao.updateStudent(student_Add);
	        student=dao.findById(student_Add.getId());
	        System.out.println(student);
	        System.out.println("-----  Delete Student By ID  -----");
	        dao.deleteById(student_Add.getId());
	        student=dao.findById(student_Add.getId());
	        System.out.println(student);
	        System.out.println("----- Batch Insert -----");
	        ArrayList<Student> studentList=new ArrayList<Student>();
	        student_Add.setId(9);
	        student_Add.setAge(40);
	        student_Add.setName("Savena");
	        studentList.add(student_Add);
	        student_Add=new Student();
	        student_Add.setId(6);
	        student_Add.setAge(28);
	        student_Add.setName("Yami");
	        studentList.add(student_Add);
	        student_Add=new Student();
	        student_Add.setId(7);
	        student_Add.setAge(30);
	        student_Add.setName("Carina");
	        studentList.add(student_Add);
	        student_Add=new Student();
	        student_Add.setId(8);
	        student_Add.setAge(30);
	        student_Add.setName("Tina");
	        studentList.add(student_Add);
	        dao.batchInsert(studentList);
	        students = dao.findAll();
	        System.out.println(students);
	        
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
}
