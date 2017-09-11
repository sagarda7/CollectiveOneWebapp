package org.collectiveone.modules.comments;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.comments.dto.CommentsThreadDto;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeRelationship;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "comments_thread")
public class CommentsThread {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	
	@ManyToOne
	private Initiative initiative;
	
	@OneToMany(mappedBy = "commentsThread")
	private Set<Comments> comments = new LinkedHashSet<Comments>();
	
	
	public CommentsThreadDto toDto() {
		CommentsThreadDto commentsThreadDto = new CommentsThreadDto();
		
		commentsThreadDto.setId(id.toString());
		commentsThreadDto.setInitiativeDto(initiative.toDto());
		
		return commentsThreadDto;
	}


	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public Initiative getInitiative() {
		return initiative;
	}


	public void setInitiative(Initiative initiative) {
		this.initiative = initiative;
	}


	public Set<Comments> getComments() {
		return comments;
	}


	public void setComments(Set<Comments> comments) {
		this.comments = comments;
	}
	

	
	
}
