package com.zsz.dao.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.commons.dbcp2.BasicDataSource;

import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;



public class JDBCUtils {
	final static BasicDataSource ds;
static{
	Properties prop=new Properties();
	try {
		prop.load(JDBCUtils.class.getResourceAsStream("/dbcp2.properties"));
		String driverClassName=prop.getProperty("driverClassName");
		String url=prop.getProperty("url");
		String username=prop.getProperty("username");
		String password=prop.getProperty("password");
		ds=new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
	} catch (IOException e) {
		throw new RuntimeException(e);
	}
}
/**
 * 关闭连接
 * @param c
 */
public static void closeQuietly(AutoCloseable c){
	if(c!=null){
		try {
			c.close();
		} catch (Exception e) {
			
		}
	}
}
/**
 * 获得一个连接
 * @return
 * @throws SQLException 
 */
public static Connection getConnection() throws SQLException{
	return ds.getConnection();
}
/**
 * 根据rs关闭所有连接
 * @param rs
 */
public static void closeAll(ResultSet rs){
	if(rs==null){
		return;
	}
	try {
		Statement	stmt = rs.getStatement();
		Connection conn=stmt.getConnection();
		closeQuietly(rs);
		closeQuietly(stmt);
		closeQuietly(conn);
	} catch (SQLException e) {
		throw new RuntimeException();
	}
}
/**
 * 事物 回滚  安静关闭
 * @param conn
 */
public static void rollBack(Connection conn){
	if(conn!=null){
		try {
			conn.rollback();
		} catch (SQLException e) {
		}
	}
}
/**
 * 执行非查询代码
 * @param sql
 * @param params
 * @throws SQLException
 */
 public static void executeNonQuery(String sql,Object...params) throws SQLException{
	 Connection conn=ds.getConnection();
	 try{
		executeNonQuery(conn, sql, params);
	 }finally{
		closeQuietly(conn); 
	 }
 }
 /**
  * 执行非查询代码,传入一个链接
  * @param conn
  * @param sql
  * @param params
  * @throws SQLException
  */
 public static void executeNonQuery(Connection conn,String sql,Object...params) throws SQLException{
	 PreparedStatement ps=null;
	 try{
		 ps=conn.prepareStatement(sql);
		 for(int i=0;i<params.length;i++){
			 ps.setObject(i+1,params[i]);
		 }
		 ps.execute();
	 }finally{
		 closeQuietly(ps);
	 }
 }
 /**
  * 执行查询操作
  * @param sql
  * @param params
  * @return
  * @throws SQLException
  */
public static ResultSet executeQuery(String sql,Object... params) throws SQLException{
	Connection conn=ds.getConnection();
	try{
		 return executeQuery(conn,sql,params);
	}catch(SQLException ex){
		closeQuietly(conn);
		throw ex;
	}
}
/**
 * 执行查询操作,传入一个链接
 * @param sql
 * @param params
 * @return
 * @throws SQLException
 */
public static ResultSet executeQuery(Connection conn,String sql,Object... params) throws SQLException{
	 PreparedStatement ps=null;
		try{
			ps=conn.prepareStatement(sql);
			 for(int i=0;i<params.length;i++){
				 ps.setObject(i+1,params[i]);
			 }
			 return ps.executeQuery();
		}catch(SQLException ex){
			closeQuietly(ps);
			throw ex;
		}
}
/**
 * 专门用来执行插入数据的方法，可以得到自增字段的值
 * @param sql
 * @param params
 * @return
 * @throws SQLException
 */
public static long executeInsert(String sql,Object...params) throws SQLException{
	Connection conn=ds.getConnection();
	try{
		return executeInsert(conn,sql,params);
}finally{
	 closeQuietly(conn);
}
}
/**
 * 专门用来执行插入数据的方法，可以得到自增字段的值,传入一个链接。
 * @param conn
 * @param sql
 * @param params
 * @return
 * @throws SQLException
 */
public static long executeInsert(Connection conn,String sql,Object...params) throws SQLException{
	 PreparedStatement psInsert=null;
	 PreparedStatement psLastInsertId=null;
	 ResultSet rs=null;
	try{
		psInsert=conn.prepareStatement(sql);
		 for(int i=0;i<params.length;i++){
			 psInsert.setObject(i+1,params[i]);
		 }
		psInsert.execute();
		psLastInsertId=conn.prepareStatement("select Last_insert_id()");
		rs=psLastInsertId.executeQuery();
		if(rs.next()){
			return rs.getLong(1);
		}else{
			throw new RuntimeException("没有查到自增字段的值");
		}
}finally{
	 closeQuietly(rs);
	 closeQuietly(psLastInsertId);
	 closeQuietly(psInsert);
}
}
/**
 * 执行查询，并且返回第一行，第一列的值，如果没有返回null
 * select count(*) form T
 * @param sql
 * @param params
 * @return
 * @throws SQLException
 */
public static Object querySingle(String sql,Object...params) throws SQLException{
	Connection conn=ds.getConnection();
	try{
		return querySingle(conn,sql,params);
	}catch(SQLException e){
		throw new RuntimeException(e);
	}finally{
		closeQuietly(conn);
	}
}
/**
 * 执行查询，并且返回第一行，第一列的值，如果没有返回null,传入一个连接
 * select count(*) form T
 * @param conn
 * @param sql
 * @param params
 * @return
 * @throws SQLException
 */
public static Object querySingle(Connection conn,String sql,Object...params) throws SQLException{
	PreparedStatement ps=null;
	ResultSet rs=null;
	try{
	ps=conn.prepareStatement(sql);
	for(int i=0;i<params.length;i++){
		ps.setObject(i+1,params[i]);
	}
	rs=ps.executeQuery();
	if(rs.next()){
		return rs.getObject(1);
	}else{
		return 0;
	}
}catch(SQLException e){
	throw new RuntimeException(e);
}finally{
	closeQuietly(rs);
	closeQuietly(ps);
}
}
}