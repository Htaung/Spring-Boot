package org.test;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.test.bookpub.repository.BookRepository;

public class StartupRunner implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());

	//@Autowired
	//private DataSource ds;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		logger.info("DataSource: "+jdbcTemplate.getDataSource().toString());
		logger.info("Number of books: " + bookRepository.count());
	}
}