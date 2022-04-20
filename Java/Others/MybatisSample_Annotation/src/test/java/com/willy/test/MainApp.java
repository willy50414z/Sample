package com.willy.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.willy.db.IFieldMapListImp;
import com.willy.entity.FieldMapList;
import com.willy.spring.BeanFactory;
import com.willy.sqlMapper.IFieldMapListDAO;

public class MainApp {
    public static void main(String[] args) {
//    	BeanFactory.get(IFieldMapListDAO.class).getFieldMapList("100000fieldKind");
    	FieldMapList fml=new FieldMapList();
    	List<FieldMapList> fmlList=new ArrayList<FieldMapList>();
    	for(int i=0;i<500;i++) {
    		fml.setFieldDesc("desc"+i);
    		fml.setFieldKey(i);
    		fml.setFieldKind("fieldKind"+i);
    		fml.setFieldValue("fieldValue");
    		fml.setLastModifyDate(new Date());
    		fml.setLastModifyUser("lastModifyUser"+i);
    		fmlList.add(fml);
//    		fmlList1.add(fml);
    		
    		fml=new FieldMapList();
    	}
    	BeanFactory.get(IFieldMapListImp.class).batchInsertWithTransaction(fmlList);
    	//測試Transaction
//    	BeanFactory.get(IFieldMapListImp.class).modifyFieldKey(990);
    }
}

