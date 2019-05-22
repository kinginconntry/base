package com.needto.mongo;

import com.needto.common.utils.Utils;
import com.needto.dao.models.BaseEntity;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * mongo事件监听器
 * 转换_id成id，混淆id，创建时间，更新时间
 */
@Component
public class MongoEventListener extends AbstractMongoEventListener {

    @Autowired
    private ApplicationContext applicationContext;

    private IMongoIntercept iMongoIntercept;

    @PostConstruct
    public void init(){
        try {
            iMongoIntercept = applicationContext.getBean(IMongoIntercept.class);
        }catch (Exception e){
            // 默认的配置
            iMongoIntercept = new IMongoIntercept() {
                @Override
                public void beforeSave(BaseEntity source, Document document) {}
                @Override
                public void afterLoad(Document document) {}
            };
        }

    }

    /**
     * 插入数据时进行数据转换处理
     * @param event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent event) {
        // 更新原对象信息
        Object source = event.getSource();
        if(!(source instanceof BaseEntity)){
            return;
        }
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
            iMongoIntercept.beforeSave((BaseEntity) source, doc);
            uptimeField.setAccessible(true);
            uptimeField.set(source, now);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
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
        iMongoIntercept.afterLoad(source);
    }
}
