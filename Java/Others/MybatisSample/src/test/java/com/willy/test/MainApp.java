package com.willy.test;

import java.util.List;

import com.willy.db.WDBImp;
import com.willy.entity.FieldMapList;
import com.willy.spring.BeanFactory;

public class MainApp {
	public static void main(String[] args) throws Exception {
		
		List<FieldMapList> fmlList=BeanFactory.get(WDBImp.class).selectByPrimaryKey("100002fieldKind");
//		for(FieldMapList fml : fmlList) {
//			System.out.println(fml.getFieldDesc());
//		}
//		System.out.println("----------------------------");
//		
//		FieldMapList fml=new FieldMapList();
//		fml.setFieldDesc("fieldDesc");
//		fml.setFieldKey(1);
//		fml.setFieldKind("fieldKind");
//		fml.setFieldValue("fieldValue");
//		fml.setLastModifyDate(new Date());
//		fml.setLastModifyUser("lastModifyUser");
//		BeanFactory.get(WDBImp.class).insert(fml);
//		
//		fmlList=BeanFactory.get(WDBImp.class).selectByPrimaryKey("fieldKind");
//		for(FieldMapList fml1 : fmlList) {
//			System.out.println(fml1.getFieldDesc());
//		}
//		
//		BeanFactory.get(WDBImp.class).deleteByPrimaryKey("fieldKind");
//		
//		fmlList=BeanFactory.get(WDBImp.class).selectByPrimaryKey("fieldKind");
//		for(FieldMapList fml1 : fmlList) {
//			System.out.println(fml1.getFieldDesc());
//		}
	}
}

