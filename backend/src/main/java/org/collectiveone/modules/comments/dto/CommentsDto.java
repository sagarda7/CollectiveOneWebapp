package org.collectiveone.modules.comments.dto;

import java.sql.Timestamp;

import org.collectiveone.modules.comments.Comments;
import org.collectiveone.modules.users.AppUserDto;

public class CommentsDto {

	private String id;
	private CommentsThreadDto commentsThreadDto;
	private String comment;
	private AppUserDto creator;
	private Timestamp creationDate;
	private String intiativeId;
	
	public Comments toEntity(Comments comments, CommentsDto commentsDto) {
		
		if (comments == null) comments = new Comments();
		
		comments.setComment(commentsDto.getComment());
				
		return comments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CommentsThreadDto getCommentsThreadDto() {
		return commentsThreadDto;
	}

	public void setCommentsThreadDto(CommentsThreadDto commentsThreadDto) {
		this.commentsThreadDto = commentsThreadDto;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public AppUserDto getCreator() {
		return creator;
	}

	public void setCreator(AppUserDto creator) {
		this.creator = creator;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getIntiativeId() {
		return intiativeId;
	}

	public void setIntiativeId(String intiativeId) {
		this.intiativeId = intiativeId;
	}
	
		
}
