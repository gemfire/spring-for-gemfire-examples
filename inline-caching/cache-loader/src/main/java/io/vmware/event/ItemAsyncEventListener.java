package io.vmware.event;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Operation;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;


@SuppressWarnings("deprecation")
public class ItemAsyncEventListener implements AsyncEventListener {

	private static LogWriter log;
	
	static {
		log = CacheFactory.getAnyInstance().getDistributedSystem().getLogWriter();
	}

	@Override
	public boolean processEvents(List<AsyncEvent> events) {
		try(Connection conn = ConnectionPool.getInstance().getConnection()) {
			log.info("connected:"+ conn.getSchema());
			return process(conn, events);
		} catch (SQLException e) {
			log.error("Exception", e);
			return false;
		}
	}

	private boolean process(Connection conn, List<AsyncEvent> events) {
		DSLContext dslContext = DSL.using(conn, SQLDialect.POSTGRES);

		for (AsyncEvent event : events) {
			Operation operation = event.getOperation();
			if(Arrays.asList(Operation.UPDATE, Operation.CREATE, Operation.LOCAL_LOAD_CREATE).contains(operation)) {
				createOrUpdate(dslContext, event);
			} else {
				log.error("operation not implemented: " + operation);
			}
		}

		return true;
	}

	private static void createOrUpdate(DSLContext create, AsyncEvent event) {
		int itemId = Integer.parseInt((String) event.getKey());
		String value = (String) event.getDeserializedValue();
		log.info(String.format("process event itemId: %s value: %s", itemId, value));

		Table<Record> tellerTable = DSL.table("pgbench_tellers");
		Field<Object> fillerField = DSL.field("filler");
		Field<Object> idField = DSL.field("tid");

		if (create.fetchExists(tellerTable)) {
			int result = create.update(tellerTable)
					.set(fillerField, value)
					.where(idField.eq(itemId))
					.execute();

			log.info("update result: " + result);
		} else {
			log.info("create");

			int result = create.insertInto(tellerTable)
					.columns(idField, fillerField)
					.values(itemId, value)
					.execute();

			log.info("create result: " + result);
		}
	}
}
