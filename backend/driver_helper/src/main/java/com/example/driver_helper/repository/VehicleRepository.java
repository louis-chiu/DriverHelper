package com.example.driver_helper.repository;

import com.example.driver_helper.entity.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//  CrudRepository中的方法
//
//      save(entity):添加一条数据
//      save(entities)：添加多条数据entities为集合
//      findOne(id)：根据id查询一条数据
//      exists(id)：判断id是否存在
//      findAll()：查询全部数据
//      delete(id)：根据id删除数据
//      delete(entity)：根据一条数据的信息删除数据
//      delete(entities)：根据多条数据的信息删除数据
//      deleteAll()：删除全部信息


@Repository
public interface VehicleRepository  extends CrudRepository<Vehicle, Long> {

}
