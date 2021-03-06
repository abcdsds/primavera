package com.genius.primavera.domain.relation.manyToMany;

import lombok.*;

import javax.persistence.*;

import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@ToString
@Table(name = "SELLER")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class Seller {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@NonNull
	@Column(name = "NAME")
	private String name;

	@OneToMany(mappedBy = "seller")
	Set<Contract> contracts;
}
