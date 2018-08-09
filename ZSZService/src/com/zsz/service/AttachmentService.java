package com.zsz.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.AttachmentDAO;
import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.AttachmentDTO;

public class AttachmentService {
	private AttachmentDAO dao=new AttachmentDAO();
	public AttachmentDTO[] getAll(){
		return dao.getAll();
	}
	public AttachmentDTO[] getAttachments(long houseId){
		return dao.getAttachments(houseId);
	}
}
