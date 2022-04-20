package com.willy.db;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.willy.entity.FieldMapList;
import com.willy.sqlMapper.IFieldMapListDAO;

@Service
public class IFieldMapListImp implements IFieldMapListDAO {
	//batch
	@Autowired
	@Qualifier("batchSqlSession")
	private SqlSession ss_batch;
	
	@Autowired
	private SqlSession ss;

	@Override
	public FieldMapList getFieldMapList(String fieldKind) {
		// TODO Auto-generated method stub
		try {
			IFieldMapListDAO userDAO=ss.getMapper(IFieldMapListDAO.class);
			FieldMapList fml = userDAO.getFieldMapList("100000fieldKind");
			System.out.println(fml.getFieldDesc());
		} finally {
		}
		return null;
	}

	@Override
	public int batchInsertWithTransaction(List<FieldMapList> fmlList) {
		//超過2000筆不可用
//		IFieldMapListDAO userDAO = ss.getMapper(IFieldMapListDAO.class);
//		int updateCount = userDAO.batchInsertWithTransaction(fmlList);
		
		//用batch方式
		int rowCounts=0;
		IFieldMapListDAO userDAO = ss_batch.getMapper(IFieldMapListDAO.class);
		for(FieldMapList fml : fmlList) {
			userDAO.insert(fml);
			if(rowCounts % 1000 == 0 && rowCounts>0) {
				ss_batch.commit();
				ss_batch.clearCache();
			}
			ss_batch.commit();
			ss_batch.clearCache();
			rowCounts++;
		}
		return rowCounts;
	}

	@Override
	public int insert(FieldMapList fml) {
		// TODO Auto-generated method stub
		IFieldMapListDAO userDAO=ss.getMapper(IFieldMapListDAO.class);
		int i=10;
		fml.setFieldDesc("desc"+i);
		fml.setFieldKey(i);
		fml.setFieldKind("fieldKind"+i);
		fml.setFieldValue("fieldValue");
		fml.setLastModifyDate(new Date());
		fml.setLastModifyUser("lastModifyUser"+i);
		userDAO.insert(fml);
		userDAO.insert(fml);
		return 0;
	}

	@Override
	public int TestTransaction(List<FieldMapList> fml,List<FieldMapList> fml1) {
		try {
		batchInsertWithTransaction(fml);
		batchInsertWithTransaction(fml1);
		}catch(Exception e) {
			ss_batch.rollback();
		}
		return 0;
	}

	@Override
	@Transactional//(propagation = Propagation.REQUIRED, transactionManager = "transactionManager",rollbackFor = Exception.class)
	public void modifyFieldKey(int fieldKey) {
		// TODO Auto-generated method stub
		IFieldMapListDAO userDAO=ss.getMapper(IFieldMapListDAO.class);
		userDAO.modifyFieldKey(fieldKey);
		int a=1/0;
		userDAO.modifyFieldKey(30);
	}


}
