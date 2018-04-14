package com.yubajin.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.yubajin.mybatis.bean.Employee;
import com.yubajin.mybatis.dao.EmployeeMapper;
import com.yubajin.mybatis.dao.EmployeeMapperAnnotation;
import com.yubajin.mybatis.dao.EmployeeMapperPlus;
/**
 * 1、接口式编程
 * 原生:					Dao		===>	DaoImpl
 * mybatis				Mapper  ===>	xxMapper.xml
 * 
 * 2、SqlSession代表和数据库的一次会话,用完必须关闭
 * 3、SqlSession和connection一样都是非线程安全，每次使用都应该去获取新的对象
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象
 * 		(将接口和xml进行绑定)
 * 		EmployeeMapper empMapper		sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要配置文件:
 * 		mybatis的全局配置文件:包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件:保存了每个sql语句的映射信息，将sql抽取出来。
 * @author Administrator
 *
 */


public class MyBatisTest {

	public SqlSessionFactory getSqlSessionFactory() throws IOException{
		String resource = "conf/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return (new SqlSessionFactoryBuilder().build(inputStream));
	}
	/*
	@Test
	public void test() throws IOException {
		
//		String resource = "conf/mybatis-config.xml";
//		InputStream inputStream = Resources.getResourceAsStream(resource);
//		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		//2、获取sqlSession实例，能直接执行已经映射的sql语句
		
		SqlSession openSession = getSqlSessionFactory().openSession();
		
		try{
			Employee employee = openSession.selectOne("com.yubajin.mybatis.EmployeeMapper.selectEmp",1);
			System.out.println(employee);			
		}finally{
			openSession.close();			
		}
	}
	*/
	
	@Test
	public void test01() throws IOException{
		//1、获取sqlSessionFactory对象
		//2、获取sqlSession对象
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		
		try{
			//3、获取接口的实现类对象
			//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			//mapper是代理对象
			EmployeeMapper 	mapper = sqlSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(2);
			System.out.println("employee:\n" + employee);
			}finally{
				sqlSession.close();
			}
		}
	
	@Test
	public void test02() throws IOException{
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		
		try{			
			EmployeeMapperAnnotation mapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println("select employee by class with annotation without xml配置:\n" + employee);
			
		}finally{
			sqlSession.close();
		}
	}
	
	
	/**
	 * 1、mybatis允许增删改直接定义一下类型返回值
	 * 		Integer,Long,Boolean
	 * 		在接口返回方法上定义返回值，mybatis自动封装返回值
	 * 2、我们需要手动提交数据
	 * 		sqlSessionFactory.openSession();==>手动提交
	 * 		sqlSessionFactory.openSession(true);==>自动提交
	 * @throws IOException
	 */
	@Test
	public void test03() throws IOException{
		//获取到的sqlSession不会自动提交数据
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try{
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			
//			Employee Jerry = new Employee(null, "Mickey", "男", "Mickey@yubajin.com");
//			Long addEmp = mapper.addEmp(Jerry);
//			System.out.println("插入了" + addEmp + "条记录");
//			System.out.println("添加记录的id为:" + Jerry.getId());
			
			boolean deleteEmpById = mapper.deleteEmpById(8);
			System.out.println("是否成功删除记录?" + deleteEmpById);
			
			//返回集合List
			List<Employee> emps = mapper.getEmpsByLastNameLike("%小%");
			for(Employee employee : emps){
				System.out.println(employee);
			}
			
			//返回map,key就是列名，值就是对应的值
			Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
			System.out.println(map);
			
			//键是这条记录的主键(可以设置其他的属性，比如名字等)，值是封装后的javabean对象
			Map<Integer, Employee> empByLastNameLikeReturnMap = mapper.getEmpByLastNameLikeReturnMap("%小%");
			System.out.println(empByLastNameLikeReturnMap);
			
			//手动提交数据
			sqlSession.commit();
		}finally{
			sqlSession.close();
		}
	}
	
	@Test
	public void test04() throws IOException{
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		
		try{			
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpByIdAndLastName(3, "Jerry");
			System.out.println("select employee with many param by@param:\n" + employee);
			
		}finally{
			sqlSession.close();
		}
	}
	
	@Test
	public void test05() throws IOException{
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		
		try{			
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tableName", "tb1_employee");
			map.put("id", 3);
			map.put("lastName", "Jerry");
			Employee employee = mapper.getEmpByMap(map);
			System.out.println("select employee with many param bymap:\n" + employee);
			
		}finally{
			sqlSession.close();
		}
	}
	
	@Test
	public void test06() throws IOException{
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		
		try{
			EmployeeMapperPlus mapper = sqlSession.getMapper(EmployeeMapperPlus.class);
//			Employee empById = mapper.getEmpById(1);
//			System.out.println(empById);
			
//			Employee empAndDept = mapper.getEmpAndDept(1);
//			System.out.println(empAndDept);
//			System.out.println(empAndDept.getDept());
			
			Employee employee = mapper.getEmpByIdStep(1);
			System.out.println(employee);
			System.out.println(employee.getDept());
		}finally{
			sqlSession.close();
		}
		
	}
	
}
