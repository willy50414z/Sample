package com.willy.ibatis.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.willy.ibatis.entity.Student;

public class StudentDaoImpl implements IStudentDao{
	private static SqlMapClient client = IbatisUtil.getClient();  
	  
    /** 
     * �d����� 
     * @return ��^Student���X 
     */  
    @Override  
    public List<Student> findAll() {  
        try {  
            return client.queryForList("findAll");  
        } catch (SQLException e) {
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * �q�LID�d���� 
     * @param id 
     * @return ��^���Student 
     */  
    @Override  
    public Student findById(int id) {  
        try {  
            return (Student) client.queryForObject("findById",id);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * �q�Lname�ҽk�d�� 
     * @param name 
     * @return ��^Student���X 
     */  
    @Override  
    public List<Student> findByName(String name) {  
        try {  
            return client.queryForList("findByName",name);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * ���J�@��Student 
     * @param student 
     */  
    @Override  
    public void addStudent(Student student) {  
        try {  
            client.insert("addStudent",student);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * ��sStudent 
     * @param student 
     */  
    @Override  
    public void updateStudent(Student student) {  
        try {  
            client.update("updateStudent",student);
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * �R�����w��Student 
     * @param id 
     */  
    @Override  
    public void deleteById(int id) {  
        try {  
            client.delete("deleteById",id);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }

	@Override
	public void batchInsert(ArrayList<Student> studentList) {
		// TODO Auto-generated method stub
		try {  
//			client.startBatch();
//			for(Student st:studentList) {
				client.insert("batchInsert",studentList);
//			}
//            client.executeBatch();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
	}  
}

