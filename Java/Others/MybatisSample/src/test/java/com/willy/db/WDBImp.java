package com.willy.db;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.willy.entity.FieldMapList;
import com.willy.sqlMapper.FieldMapListDAO;

@Service
@Primary
public class WDBImp implements FieldMapListDAO{

	@Autowired
	private SqlSession ss;
	@Autowired
	private SqlSessionTemplate sst;
	
	@Override
	public List<FieldMapList> selectByPrimaryKey(String fieldKind) {
		// TODO Auto-generated method stub
		HashMap map=new HashMap();
		map.put("fieldkind", fieldKind);
		
		BoundSql boundSql = sst.getSqlSessionFactory().getConfiguration().
		        getMappedStatement("selectByPrimaryKey")
		        .getBoundSql(map);
		        String sql = boundSql.getSql();
		        System.out.println(sql);
		return sst.<FieldMapList>selectList("selectByPrimaryKey",map);
	}

	@Override
	public int deleteByPrimaryKey(String fieldKind) {
		// TODO Auto-generated method stub
		HashMap map=new HashMap();
		map.put("fieldkind", fieldKind);
		return ss.delete("deleteByPrimaryKey",map);
	}

	@Override
	public int insert(FieldMapList record) {
		// TODO Auto-generated method stub
		return ss.insert("insert",record);
	}

	@Override
	public int insertSelective(FieldMapList record) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int updateByPrimaryKeySelective(FieldMapList record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(FieldMapList record) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
