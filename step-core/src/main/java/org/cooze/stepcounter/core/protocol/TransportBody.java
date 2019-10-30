package org.cooze.stepcounter.core.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-07-29
 **/

public class TransportBody implements Serializable {

    private List<NameValue<String>> keyFields = new ArrayList<>();
    private List<NameValue<Long>> countFields = new ArrayList<>();


    public TransportBody() {
    }

    public List<NameValue<String>> getKeyFields() {
        return keyFields;
    }


    public List<NameValue<Long>> getCountFields() {
        return countFields;
    }

    public void setKeyFields(List<NameValue<String>> keyFields) {
        this.keyFields = keyFields;
    }

    public void setCountFields(List<NameValue<Long>> countFields) {
        this.countFields = countFields;
    }

    public static class BodyBuilder {

        private TransportBody body = null;

        public BodyBuilder() {
            this.body = new TransportBody();
        }

        public static BodyBuilder newInstance() {
            return new BodyBuilder();
        }

        public BodyBuilder appendKeyField(NameValue<String> keyField) {
            if (body.getKeyFields().contains(keyField)) {
                return this;
            }
            this.body.getKeyFields().add(keyField);
            return this;
        }

        public BodyBuilder appendCountField(NameValue<Long> countField) {
            if (body.getCountFields().contains(countField)) {
                return this;
            }
            this.body.getCountFields().add(countField);
            return this;
        }

        public void cleanCountFields() {
            this.body.getCountFields().clear();
        }

        public void cleanKeyFields() {
            this.body.getKeyFields().clear();
        }

        public TransportBody build() {
            return this.body;
        }

    }

    @Override
    public String toString() {
        return "TransportBody{" +
                "keyFields=" + keyFields +
                ", countFields=" + countFields +
                '}';
    }
}
