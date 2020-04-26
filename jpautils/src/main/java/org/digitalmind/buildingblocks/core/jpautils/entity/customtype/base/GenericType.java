package org.digitalmind.buildingblocks.core.jpautils.entity.customtype.base;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.lang.reflect.ParameterizedType;

public class GenericType<J> extends AbstractSingleColumnStandardBasicType<J> {

    private Class<J> type;

    public GenericType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor<J> javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
        this.type = (Class<J>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public String getName() {
        String name = this.getClass().getSimpleName();
        if (name.toLowerCase().startsWith("jpa")) {
            name = name.substring(3);
        }
        if (name.toLowerCase().endsWith("type")) {
            name = name.substring(0, name.length() - 4);
        }
        //name = this.getClass().getSimpleName();// + "AsSqlType" + this.getSqlTypeDescriptor().getSqlType();
        return name;
    }

    public Class<J> getJavaType() {
        return type;
    }

}
