package org.test.bookpub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.test.bookpub.entity.Author;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Publisher;
import org.test.bookpub.repository.AuthorRepository;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.repository.PublisherRepository;

public class StartupRunner implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());

	// @Autowired
	// private DataSource ds;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private PublisherRepository publisherRepository;

//	@Scheduled(initialDelay = 1000, fixedRate = 10000)
//	public void run() {
//
//		//logger.info("Number	of	books:	" + bookRepository.count());
//	}

	@Override
	public void run(String... arg0) throws Exception {
		logger.info("Hello");
		Author	author	=	new	Author("Alex",	"Antonov");
		author	=	authorRepository.save(author);
		Publisher	publisher	=	new	Publisher("Packt");
		publisher	=	publisherRepository.save(publisher);
		Book	book	=	new	Book("978-1-78528-415-1",	"Spring	Boot	Recipes", author, publisher);
		bookRepository.save(book);
		// logger.info("DataSource: "+ds.toString());
		logger.info("DataSource:	" + jdbcTemplate.getDataSource().toString());
		logger.info("Number	of	books:	" + bookRepository.count());
		
	}

}
