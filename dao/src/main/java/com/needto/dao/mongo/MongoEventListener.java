package com.needto.dao.mongo;

import com.needto.common.utils.Utils;
import com.needto.dao.models.BaseEntity;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * mongo事件监听器
 */
@Component
public class MongoEventListener extends AbstractMongoEventListener {

    /**
     * 插入数据时进行数据转换处理
     * @param event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent event) {
        Document doc = event.getDocument();
        Date now = new Date();
        assert doc != null;
        boolean oidExist = false;
        ObjectId oid;
        String confuseId;
        Date ctime = now;
        if(doc.containsKey(MongoDao.OID)){
            oid = MongoQueryUtils.getId(doc.get(MongoDao.OID));
            if(oid != null){
                oidExist = true;
            }else{
                oid = new ObjectId();
            }
        }else if(doc.containsKey(BaseEntity.ID)){
            oid = MongoQueryUtils.getId(doc.get(BaseEntity.ID));
            if(oid != null){
                oidExist = true;
            }else{
                oid = new ObjectId();
            }
            doc.remove(BaseEntity.ID);
        }else{
            oid = new ObjectId();
        }
        doc.put(MongoDao.OID, oid);
        confuseId = Utils.confuseStr(oid.toString());
        doc.put(BaseEntity.CONFUSE_ID, confuseId);
        if(!oidExist){
            doc.putIfAbsent(BaseEntity.CTIME, now);
        }else{
            ctime = (Date) doc.get(BaseEntity.CTIME);
        }
        doc.put(BaseEntity.UPTIME, now);

        // 更新原对象信息
        Object source = event.getSource();
        if(source instanceof BaseEntity){
            try {
                Class sourceClass = source.getClass().getSuperclass();
                Field idField = sourceClass.getDeclaredField(BaseEntity.ID);
                idField.setAccessible(true);
                idField.set(source, oid.toString());

                Field confuseIdField = sourceClass.getDeclaredField(BaseEntity.CONFUSE_ID);
                confuseIdField.setAccessible(true);
                confuseIdField.set(source, confuseId);

                Field ctimeField = sourceClass.getDeclaredField(BaseEntity.CTIME);
                ctimeField.setAccessible(true);
                ctimeField.set(source, ctime);

                Field uptimeField = sourceClass.getDeclaredField(BaseEntity.UPTIME);
                uptimeField.setAccessible(true);
                uptimeField.set(source, now);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在反序列化之前将_id转为id
     * @param event
     */
    @Override
    public void onAfterLoad(AfterLoadEvent event) {
        final Document source = (Document) event.getSource();
        if(source.get(MongoDao.OID) != null){
            source.put(BaseEntity.ID, source.get(MongoDao.OID).toString());
        }
    }
}
