package com.zzlhs.springcloud.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {
	
	@Select("update account set money = #{money} + money where id = #{id}")
	public void updateMoney(@Param("money") double money, @Param("id") Integer id);
}
