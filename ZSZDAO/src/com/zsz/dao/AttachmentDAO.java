package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.AttachmentDTO;

public class AttachmentDAO {
	/**
	 *获取所有的设施 
	 * @return
	 */
	public AttachmentDTO[] getAll(){
		ResultSet rs=null;
		try{
			rs=JDBCUtils.executeQuery("select * from T_attachments");
			List<AttachmentDTO> list =new ArrayList<AttachmentDTO>();
			while(rs.next()){
				list.add(toAttachmentDTO(rs));
			}
			return list.toArray(new AttachmentDTO[list.size()]);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
	public static AttachmentDTO toAttachmentDTO(ResultSet rs) throws SQLException{
		AttachmentDTO attachment=new AttachmentDTO();
		attachment.setDeleted(rs.getBoolean("IsDeleted"));
		attachment.setIconName(rs.getString("IconName"));
		attachment.setId(rs.getLong("Id"));
		attachment.setName(rs.getString("Name"));
		return attachment;
	}
	/**
	 *获取房子houseId拥用的设施 
	 * @param houseId
	 * @return
	 */
	public AttachmentDTO[] getAttachments(long houseId){
		StringBuilder sb =new StringBuilder();
		sb.append("select a.* from T_attachments a where Id in\n");
		sb.append("(\n");
		sb.append("select AttachmentId from T_houseattachments where HouseId=?\n");
		sb.append(")\n");
		ResultSet rs=null;
		try {
			rs=JDBCUtils.executeQuery(sb.toString(),houseId);
			List<AttachmentDTO> list=new ArrayList<AttachmentDTO>();
			while(rs.next()){
				list.add(toAttachmentDTO(rs));
			}
			return list.toArray(new AttachmentDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JDBCUtils.closeAll(rs);
		}
	}
}
