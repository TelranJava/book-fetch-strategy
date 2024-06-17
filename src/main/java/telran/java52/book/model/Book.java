package telran.java52.book.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(of = "isbn")
@Entity
public class Book {
	@Id
	String isbn;
	String title;
	@Singular
	@ManyToMany//(fetch = FetchType.EAGER)
//	LAZY - default, доставать только "обертки"
//	EAGER - доставать авторов всегда всех вместе со всеми данными, снизит эфективность запросов  
	Set<Author> authors;
}
