package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.collectiveone.modules.conversations.MessageThread;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "model_sections")
public class ModelSection {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative initiative;
	
	private Boolean isTopModelSection;
	
	@Column(name = "title", length = 30)
	private String title;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "description")
	private String description;
	
	@ManyToMany
	@OrderColumn(name = "cards_order")
	private List<ModelCardWrapper> cardsWrappers = new ArrayList<ModelCardWrapper>();
	
	@ManyToMany
	private List<ModelCardWrapper> cardsWrappersTrash = new ArrayList<ModelCardWrapper>();
	
	/* should be one to many but there seems to be a bug in Hibernate 
	 * see https://stackoverflow.com/questions/4022509/constraint-violation-in-hibernate-unidirectional-onetomany-mapping-with-jointabl
	 * */
	@ManyToMany
	@OrderColumn(name = "subsections_order")
	private List<ModelSection> subsections = new ArrayList<ModelSection>();
	
	@ManyToMany
	private List<ModelSection> subsectionsTrash = new ArrayList<ModelSection>();
	
	@OneToOne
	private MessageThread messageThread;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		ModelSection other = (ModelSection) obj;
		return id.equals(other.getId());
	}

	public ModelSectionDto toDto() {
		ModelSectionDto sectionDto = new ModelSectionDto();
		
		sectionDto.setId(id.toString());
		sectionDto.setTitle(title);
		sectionDto.setDescription(description);
		sectionDto.setnSubsections(subsections.size());
		sectionDto.setnCards(cardsWrappers.size());
		
		if (initiative != null) sectionDto.setInitiativeId(initiative.getId().toString());
		
		return sectionDto;
	}
	
	public ModelSectionDto toDtoLight () {
		ModelSectionDto sectionDto = new ModelSectionDto();
		
		sectionDto.setId(id.toString());
		sectionDto.setTitle(title);
		sectionDto.setDescription(description);
		sectionDto.setIsTopModelSection(isTopModelSection);
		
		return sectionDto; 
	}
	
	public Boolean getIsTopModelSection() {
		return isTopModelSection;
	}

	public void setIsTopModelSection(Boolean isTopModelSection) {
		this.isTopModelSection = isTopModelSection;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ModelCardWrapper> getCardsWrappers() {
		return cardsWrappers;
	}

	public void setCardsWrappers(List<ModelCardWrapper> cardsWrappers) {
		this.cardsWrappers = cardsWrappers;
	}
	
	public List<ModelCardWrapper> getCardsWrappersTrash() {
		return cardsWrappersTrash;
	}

	public void setCardsWrappersTrash(List<ModelCardWrapper> cardsWrappersTrash) {
		this.cardsWrappersTrash = cardsWrappersTrash;
	}

	public List<ModelSection> getSubsections() {
		return subsections;
	}

	public void setSubsections(List<ModelSection> subsections) {
		this.subsections = subsections;
	}

	public List<ModelSection> getSubsectionsTrash() {
		return subsectionsTrash;
	}

	public void setSubsectionsTrash(List<ModelSection> subsectionsTrash) {
		this.subsectionsTrash = subsectionsTrash;
	}

	public MessageThread getMessageThread() {
		return messageThread;
	}

	public void setMessageThread(MessageThread messageThread) {
		this.messageThread = messageThread;
	}
	
}
