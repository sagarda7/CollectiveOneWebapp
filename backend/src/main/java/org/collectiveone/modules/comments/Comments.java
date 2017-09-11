package org.collectiveone.modules.comments;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.comments.dto.CommentsDto;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "comments")
public class Comments {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "comment", length = 1024)
	private String comment;

	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	
	@ManyToOne
	private CommentsThread commentsThread;
	
	@ManyToOne
	private AppUser creator;
	
	
	
	
	
	public CommentsDto toDto() {
		CommentsDto commentsDto = new CommentsDto();
		
		commentsDto.setId(id.toString());
		commentsDto.setCommentsThreadDto(commentsThread.toDto());
		commentsDto.setCreationDate(creationDate);
		commentsDto.setCreator(creator.toDtoLight());
		commentsDto.setComment(comment);
		
		return commentsDto;
	}
	
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}



	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}



	public Timestamp getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}



	public CommentsThread getCommentsThread() {
		return commentsThread;
	}



	public void setCommentsThread(CommentsThread commentsThread) {
		this.commentsThread = commentsThread;
	}



	public AppUser getCreator() {
		return creator;
	}



	public void setCreator(AppUser creator) {
		this.creator = creator;
	}

		
	
}
