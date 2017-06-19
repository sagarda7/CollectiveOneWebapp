package org.collectiveone.modules.initiatives.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.collectiveone.modules.tokens.model.MemberTransfer;
import org.collectiveone.modules.users.model.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity(name = "members")
public class Member implements Comparable<Member>{

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative initiative;
	
	@ManyToOne
	private AppUser user;
	
	@OneToMany(mappedBy = "member")
	private List<MemberTransfer> tokensTransfers = new ArrayList<MemberTransfer>();


	@Override
    public int compareTo(Member m) {
        return user.getNickname().compareTo(m.getUser().getNickname()) ;
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

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public List<MemberTransfer> getTokensTransfers() {
		return tokensTransfers;
	}

	public void setTokensTransfers(List<MemberTransfer> tokensTransfers) {
		this.tokensTransfers = tokensTransfers;
	}
	
	

}