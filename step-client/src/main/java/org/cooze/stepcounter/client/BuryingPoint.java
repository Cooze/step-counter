package org.cooze.stepcounter.client;


import org.cooze.stepcounter.core.SchemaContainer;
import org.cooze.stepcounter.core.protocol.NameValue;
import org.cooze.stepcounter.core.protocol.TransportBody;
import org.cooze.stepcounter.core.protocol.TransportSchema;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-07
 **/
public final class BuryingPoint {

    private static final long COUNT_0 = 0L;
    private static final long COUNT_1 = 1L;
    private static final int ZERO = 0;

    private final static class Holder {
        private static ThreadLocal<TransportSchema> data = ThreadLocal.withInitial(() -> new TransportSchema());
    }

    private BuryingPoint() {

    }

    public static void init(String schema) {
        Holder.data.get().setHead(SchemaContainer.instance().getSchema(schema).getHead());
        TransportBody transportBody = TransportBody.BodyBuilder.newInstance().build();
        for (NameValue<Long> c : SchemaContainer.instance().getSchema(schema).getBody().getCountFields()) {
            transportBody.getCountFields().add(new NameValue<>(c.getName(), c.getValue()));
        }
        for (NameValue<String> k : SchemaContainer.instance().getSchema(schema).getBody().getKeyFields()) {
            transportBody.getKeyFields().add(new NameValue<>(k.getName(), k.getValue()));
        }
        Holder.data.get().setBody(transportBody);
    }

    public static void setKeyField(String keyField, String value) {
        int i = Holder.data.get().getBody().getKeyFields().indexOf(new NameValue<>(keyField, value));
        if (i < ZERO) {
            throw new RuntimeException("schema 没有 keyField =" + keyField);
        }
        Holder.data.get().getBody().getKeyFields().get(i).setValue(value);
    }

    public static void setCountPoint(String countField) {
        if (Holder.data.get() == null) {
            throw new RuntimeException("schema 未初始化！");
        }
        int i = Holder.data.get().getBody().getCountFields().indexOf(new NameValue<>(countField, COUNT_0));
        if (i < ZERO) {
            throw new RuntimeException("schema 没有 countField =" + countField);
        }
        Holder.data.get().getBody().getCountFields().get(i).setValue(COUNT_1);
    }

    public static TransportSchema build() {
        return Holder.data.get();
    }

    public static void clean() {
        Holder.data.remove();
    }


}
