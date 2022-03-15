package br.com.letscode.token.entity;

import br.com.letscode.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "USER", indexes = {
	@Index(name = "username", unique = true, columnList = "username")
})
public class User extends BaseEntity<Long> {

	@NotNull
	@Column(name = "username", nullable = false)
	private String username;

	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "score")
	private Integer totalScore;

	@Column(name = "qtd_steps")
	private Integer totalSteps;

	@ElementCollection(targetClass = Privilege.class, fetch = FetchType.EAGER)
	@CollectionTable
	@Enumerated(EnumType.STRING)
	private List<Privilege> privileges;

	public User(Long id, String username, String password, List<Privilege> privileges) {
		this(id, username, password);
		this.privileges = privileges;
	}

	public User(Long id, String username, String password) {
		super.setId(id);
		this.username = username;
		this.password = password;
	}

	public User() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(Integer totalSteps) {
		this.totalSteps = totalSteps;
	}
}