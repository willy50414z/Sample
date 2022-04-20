package com.willy.sqlMapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.willy.entity.FieldMapList;

@Mapper
public interface IFieldMapListDAO {
	@Select("select * from FieldMapList where FieldKind = #{fieldKind}")
    public FieldMapList getFieldMapList(@Param("fieldKind")String fieldKind);
	
	@Insert("<script> " +
            "insert into FieldMapList"+
            "(FieldKind,FieldKey,FieldValue,FieldDesc,LastModifyUser,LastModifyDate) " +
            "values " +
            "<foreach collection=\"items\" index=\"index\" item=\"item\" separator=\",\"> "+
            "(#{item.fieldKind},#{item.fieldKey},#{item.fieldValue},#{item.fieldDesc},#{item.lastModifyUser},#{item.lastModifyDate})"+
            "</foreach> " +
            "</script>")
	public int batchInsertWithTransaction(@Param("items")List<FieldMapList> fmlList);
	
	@Insert("insert into FieldMapList"+
            "(FieldKind,FieldKey,FieldValue,FieldDesc,LastModifyUser,LastModifyDate) " +
            "values "+
            "(#{fml.fieldKind},#{fml.fieldKey},#{fml.fieldValue},#{fml.fieldDesc},#{fml.lastModifyUser},#{fml.lastModifyDate})"
            )
	
	public int insert(@Param("fml")FieldMapList fml);
	public int TestTransaction(List<FieldMapList> fml,List<FieldMapList> fml1);
	
	
	@Update("update fieldmaplist set FieldKey=#{fieldKey} where FieldKind='fieldKind0'")
	void modifyFieldKey(int fieldKey);
}
