package com.willy.sqlMapper;

import java.util.List;

import com.willy.entity.FieldMapList;

public interface FieldMapListDAO {
    int deleteByPrimaryKey(String fieldKind);

    int insert(FieldMapList record);

    int insertSelective(FieldMapList record);

    List<FieldMapList> selectByPrimaryKey(String fieldKind);

    int updateByPrimaryKeySelective(FieldMapList record);

    int updateByPrimaryKey(FieldMapList record);
}