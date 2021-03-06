package com.needto.mongo;

import com.needto.dao.inter.ConfuseId;
import com.needto.dao.inter.ICtime;
import com.needto.dao.inter.IUptime;
import com.needto.dao.inter.Id;
import com.needto.dao.models.BaseEntity;
import com.needto.tool.utils.Utils;
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
        }else if(doc.containsKey(Id.ID)){
            oid = MongoQueryUtils.getId(doc.get(Id.ID));
            if(oid != null){
                oidExist = true;
            }else{
                oid = new ObjectId();
            }
            doc.remove(Id.ID);
        }else{
            oid = new ObjectId();
        }
        doc.put(MongoDao.OID, oid);
        confuseId = Utils.confuseStr(oid.toString());
        doc.put(ConfuseId.CONFUSE_ID, confuseId);
        if(!oidExist){
            doc.putIfAbsent(ICtime.CTIME, now);
        }else{
            ctime = (Date) doc.get(ICtime.CTIME);
        }
        doc.put(IUptime.UPTIME, now);

        Class sourceClass = source.getClass().getSuperclass();
        try {
            Field idField = sourceClass.getDeclaredField(Id.ID);
            idField.setAccessible(true);
            idField.set(source, oid.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }

        try {
            Field uptimeField = sourceClass.getDeclaredField(IUptime.UPTIME);
            iMongoIntercept.beforeSave((BaseEntity) source, doc);
            uptimeField.setAccessible(true);
            uptimeField.set(source, now);

        } catch (NoSuchFieldException | IllegalAccessException e) {
        }

        try {

            Field confuseIdField = sourceClass.getDeclaredField(ConfuseId.CONFUSE_ID);
            confuseIdField.setAccessible(true);
            confuseIdField.set(source, confuseId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }

        try {
            Field ctimeField = sourceClass.getDeclaredField(ICtime.CTIME);
            ctimeField.setAccessible(true);
            ctimeField.set(source, ctime);
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
        iMongoIntercept.beforeSave((BaseEntity) source, doc);
    }

    /**
     * 在反序列化之前将_id转为id
     * @param event
     */
    @Override
    public void onAfterLoad(AfterLoadEvent event) {
        final Document source = (Document) event.getSource();
        if(source.get(MongoDao.OID) != null){
            source.put(Id.ID, source.get(MongoDao.OID).toString());
        }
        iMongoIntercept.afterLoad(source);
    }
}
