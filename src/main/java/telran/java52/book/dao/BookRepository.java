package telran.java52.book.dao;

import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import telran.java52.book.model.Author;
import telran.java52.book.model.Book;

@Repository
public class BookRepository {

	@PersistenceContext//(type = PersistenceContextType.EXTENDED)
	EntityManager em;
//  TRANSACTION - default/ соединение держится только на время транзакции, каждый отдельный запрос - отдельная транзакция
//	EXTENDED - держать соединение так долго как только можно, убиваем мультисрединг - плохое решение
	
	@Transactional
	public void addBooks() {
		Author markTwain = Author.builder()
				.fullName("Mark Twain")
				.build();
		em.persist(markTwain);

		Book pandp = Book.builder()
				.isbn("978-0140350173")
				.title("The Prince and Pauper")
				.author(markTwain)
				.build();
		em.persist(pandp);

		Author ilf = Author.builder()
				.fullName("Ilya Ilf")
				.build();
		Author petrov = Author.builder()
				.fullName("Yevgeny Petrov")
				.build();
		em.persist(ilf);
		em.persist(petrov);
		Book chairs12 = Book.builder()
				.isbn("978-0810114845")
				.title("The Twelve Chairs")
				.author(ilf)
				.author(petrov)
				.build();
		em.persist(chairs12);
	}
	
	@Transactional(readOnly = true)
	public void printAuthorsOfBook(String isbn) {
//		Book book = em.find(Book.class, isbn); 
		
//		в book вытаскиваются только "обертки" без реальных данных
//		поетому получается org.hibernate.LazyInitializationException на этапе когда понадобилось распечатать данные
//		решение 1 
//		 PersistenceContextType
//		решение 2
//		@ManyToMany(fetch = FetchType.EAGER) в Book
//		решение 3 пока лучшее но есть еще вариант
//		@Transactional(readOnly = true)
//		решение 4 самый лучший 
//		сделать "ЖАДНУЮ"-EAGER загрузку точечно в нужном месте с помощью join fetch в query
		TypedQuery<Book> query = em.createQuery("select b from Book b left join fetch b.authors a where b.isbn = ?1",Book.class);
		query.setParameter(1, isbn);
		Book book= query.getSingleResult();
		
		Set<Author> authors = book.getAuthors();
		authors.forEach(a->System.out.println(a.getFullName()));
	}
}
