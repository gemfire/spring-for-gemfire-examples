package io.vmware.event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.LoaderHelper;

@SuppressWarnings({ "deprecation" })
public class ItemCacheLoader implements CacheLoader<String, String> {
	
	private static LogWriter log;

	static {
		log = CacheFactory.getAnyInstance().getDistributedSystem().getLogWriter();
	}

	private ConnectionPool connectionPool;

	@Override
	public void initialize(Cache cache, Properties properties) {
		this.connectionPool = ConnectionPool.getInstance();
	}

	public String load(LoaderHelper<String, String> helper) {
		int itemId = Integer.parseInt(helper.getKey());
		log.info("Cache miss... Loading data from postgres...");
		log.info("itemId: "+itemId);

		try (Connection conn = connectionPool.getConnection()) {
			log.info("connected:"+ conn.getSchema());
			DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
			Result<Record> result = create.fetch("select filler from pgbench_tellers where tid=?", itemId);

			return result.isEmpty() ? null : (String) result.getValue(0, 0);
		} catch (SQLException e) {
			log.error("Exception", e);
			throw new RuntimeException(e);
		}
    }
}
