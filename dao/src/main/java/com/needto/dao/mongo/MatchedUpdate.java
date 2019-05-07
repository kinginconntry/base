package com.needto.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Objects;

/**
 * 批量匹配更新数据传输类，和批量更新不同。
 * 批量更新：一组匹配条件，更新一批数据。
 * 批量匹配更新：包含多个批量更新。即可以同时指定多组（匹配条件，更新）的组合.
 * match: 匹配条件，相当于普通查询的where
 * update： 针对这个匹配条件的更新，相当于一个普通的更新。注意：必须含有$set, $unset, $inc等语句，否则即使multi=true也只能更新一条
 * upsert， multi和普通的更新语句意义一样。
 *
 *
 * @author ylx
 */
public class MatchedUpdate {
    private DBObject match;
    private DBObject update;
    private boolean upsert;
    private boolean multi;

    public MatchedUpdate(){}

    public MatchedUpdate(DBObject match, DBObject update) {
        this(match, update, false, false);
    }

    public MatchedUpdate(DBObject match, DBObject update, boolean upsert, boolean multi) {
        Objects.requireNonNull(match, "match cannot be null");
        Objects.requireNonNull(update, "update cannot be null");
        this.match = match;
        this.update = update;
        this.upsert = upsert;
        this.multi = multi;
    }

    /**
     * 注意所有的条件都是and关系
     * @param field
     * @param value
     * @return
     */
    public MatchedUpdate match(String field, Object value) {
        BasicDBObject w = (BasicDBObject) this.match;
        if (w == null) {
            w = new BasicDBObject();
            this.setMatch(w);
        }
        w.append(field, value);
        return this;
    }

    public MatchedUpdate upsert(boolean val) {
        this.setUpsert(val);
        return this;
    }

    public MatchedUpdate multi(boolean val) {
        this.setMulti(val);
        return this;
    }

    public MatchedUpdate set(String field, Object value) {
        BasicDBObject u = this.getOrCreateUpdate();
        this.getOrCreateOperation("$set", u).append(field, value);
        return this;
    }

    public MatchedUpdate unset(String field) {
        BasicDBObject u = this.getOrCreateUpdate();
        this.getOrCreateOperation("$unset", u).append(field, 1);
        return this;
    }

    public MatchedUpdate inc(String field, int value) {
        BasicDBObject u = this.getOrCreateUpdate();
        this.getOrCreateOperation("$inc", u).append(field, value);
        return this;
    }

    private BasicDBObject getOrCreateOperation(String operator, BasicDBObject upd) {
        if (!upd.containsField(operator)) {
            upd.put(operator, new BasicDBObject(1));
        }
        return ((BasicDBObject)upd.get(operator));
    }

    private BasicDBObject getOrCreateUpdate() {
        BasicDBObject u = (BasicDBObject) this.update;
        if (u == null) {
            u = new BasicDBObject();
            this.setUpdate(u);
        }
        return u;
    }

    public DBObject getMatch() {
        return match;
    }

    public void setMatch(DBObject match) {
        Objects.requireNonNull(match, "match cannot be null");
        this.match = match;
    }

    public DBObject getUpdate() {
        return update;
    }

    public void setUpdate(DBObject update) {
        Objects.requireNonNull(update, "update cannot be null");
        this.update = update;
    }

    public boolean isUpsert() {
        return upsert;
    }

    public void setUpsert(boolean upsert) {
        this.upsert = upsert;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }
}
