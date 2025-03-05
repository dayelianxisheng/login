package com.qiuciyun.login.mapper;

import com.qiuciyun.login.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    
    @Select("SELECT * FROM product")
    List<Product> findAll();
    
    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findById(Long id);
    
    @Insert("INSERT INTO product (name, description, price, stock, category, create_time, update_time, status) " +
            "VALUES (#{name}, #{description}, #{price}, #{stock}, #{category}, #{createTime}, #{updateTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);
    
    @Update("UPDATE product SET name = #{name}, description = #{description}, " +
            "price = #{price}, stock = #{stock}, category = #{category}, " +
            "update_time = #{updateTime}, status = #{status} WHERE id = #{id}")
    int update(Product product);
    
    @Delete("DELETE FROM product WHERE id = #{id}")
    int delete(Long id);
} 