package boot.bean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Component;

@Component
public class ForeignKeyCleaner {

	@PersistenceContext
	EntityManager em;
	
	@Transactional
	public void clean() {
		Session session = em.unwrap(Session.class);
		session.doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				DatabaseMetaData metadata = conn.getMetaData();
				ResultSet tables = metadata.getTables(null, null, null, null);
				while (tables.next()) {
					String table = tables.getString("TABLE_NAME");
					ResultSet fkeys = metadata.getImportedKeys(null, null, table);
					while (fkeys.next()) {
						String fkey = fkeys.getString("FK_NAME");
						String sql = String.format("ALTER TABLE %s DROP FOREIGN KEY %s", table, fkey);
						System.out.println(sql);
						Statement stmt = conn.createStatement();
						stmt.execute(sql);
						stmt.close();
					}
				}
			}
		});
	}
}
