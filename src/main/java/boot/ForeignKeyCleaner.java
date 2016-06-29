package boot;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class ForeignKeyCleaner {

	@Autowired
	EntityManager em;

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
