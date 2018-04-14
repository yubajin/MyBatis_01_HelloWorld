package com.yubajin.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.yubajin.mybatis.bean.Employee;

public interface EmployeeMapper {
	
	//多条记录封装一个map:Map<String,Employee>:键是这条记录的主键，值是封装后的javabean对象
	//告诉mybatis封装这个map的时候使用哪个属性作为主键
	@MapKey("lastName")
	public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
	
	//返回一条记录的map,key就是列名，值就是对应的值
	public Map<String, Object> getEmpByIdReturnMap(Integer id);
	
	//模糊查询,返回集合
	public List<Employee> getEmpsByLastNameLike(String lastName);
	
	public Employee getEmpById(Integer id);
	
	public Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);
	
	public Employee getEmpByMap(Map<String, Object> map);
	
	/**
	 * mybatis允许增删改直接定义一下类型返回值
	 * 	Integer,Long,Boolean
	 * 在接口返回方法上定义返回值，mybatis自动封装返回值
	 * 下列为返回增加操作后影响的行数
	 */
	public Long addEmp(Employee employee);
	
	public void updateEmp(Employee employee);
	
	public boolean deleteEmpById(Integer id);
}
