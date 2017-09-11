package org.collectiveone.modules.comments.dto;

import java.util.List;

import org.collectiveone.modules.comments.CommentsThread;
import org.collectiveone.modules.initiatives.InitiativeDto;
import org.collectiveone.modules.model.ModelCard;

public class CommentsThreadDto {

	private String id;
	private InitiativeDto initiativeDto;
	private List<CommentsDto> commentsDtos;
	
	public CommentsThread toEntity(CommentsThread commentsThread, CommentsThreadDto commentsThreadDto) {
		
		if (commentsThread == null) commentsThread = new CommentsThread();
				
		return commentsThread;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InitiativeDto getInitiativeDto() {
		return initiativeDto;
	}

	public void setInitiativeDto(InitiativeDto initiativeDto) {
		this.initiativeDto = initiativeDto;
	}

	public List<CommentsDto> getCommentsDtos() {
		return commentsDtos;
	}

	public void setCommentsDtos(List<CommentsDto> commentsDtos) {
		this.commentsDtos = commentsDtos;
	}
	
	}
