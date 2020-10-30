package com.jnu.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jnu.util.DateUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-6-8 上午11:56
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        List<FieldFilter> fillers = new ArrayList<FieldFilter>();
        fillers.add(new DeletedFieldFilter());
        fillers.add(new CreateTimeFieldFilter());
        fillers.add(new ModifyTimeFieldFilter());
        for (FieldFilter filler : fillers) {
            filler.doFiller(this, metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        List<FieldFilter> fillers = new ArrayList<FieldFilter>();
        fillers.add(new ModifyTimeFieldFilter());
        for (FieldFilter filler : fillers) {
            filler.doFiller(this, metaObject);
        }
    }

    /**
     * 字段填充接口
     *
     * @author mq
     */
    public interface FieldFilter {

        /**
         * 字段填充
         *
         * @param metaObject 字段
         */
        void doFiller(MetaObjectHandler metaObjectHandler, MetaObject metaObject);
    }

    public class CreateTimeFieldFilter implements FieldFilter {

        /**
         * 属性名称
         */
        private static final String FIELD_NAME = "createTime";

        @Override
        public void doFiller(MetaObjectHandler metaObjectHandler, MetaObject metaObject) {
            if (metaObject.hasSetter(FIELD_NAME)) {
                metaObjectHandler.setFieldValByName(FIELD_NAME, DateUtil.getNow(), metaObject);
            }
        }
    }

    public class DeletedFieldFilter implements FieldFilter {

        /**
         * 属性名称
         */
        private static final String FIELD_NAME = "deleted";

        @Override
        public void doFiller(MetaObjectHandler metaObjectHandler, MetaObject metaObject) {
            if (metaObject.hasSetter(FIELD_NAME)) {
                metaObjectHandler.setFieldValByName(FIELD_NAME, false, metaObject);
            }
        }
    }

    public class ModifyTimeFieldFilter implements FieldFilter {

        /**
         * 属性名称
         */
        private static final String FIELD_NAME = "updateTime";

        @Override
        public void doFiller(MetaObjectHandler metaObjectHandler, MetaObject metaObject) {
            if (metaObject.hasSetter(FIELD_NAME)) {
                metaObjectHandler.setFieldValByName(FIELD_NAME, DateUtil.getNow(), metaObject);
            }
        }
    }

}
