package org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype;

import org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.base.GenericJavaDescriptor;
import org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.base.GenericType;
import org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.common.ListString;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class JpaListStringType extends GenericType<ListString> {

    public static class MutabilityPlan extends MutableMutabilityPlan<ListString> {

        public static final MutabilityPlan INSTANCE = new MutabilityPlan();

        @Override
        protected ListString deepCopyNotNull(ListString value) {
            return new ListString(value);
        }

    }

    public static final JpaListStringType INSTANCE = new JpaListStringType();

    public static final GenericJavaDescriptor<ListString> DESCRIPTOR =
            new GenericJavaDescriptor<ListString>(ListString.class, MutabilityPlan.INSTANCE);

    public JpaListStringType() {
        super(VarcharTypeDescriptor.INSTANCE, DESCRIPTOR);
    }

    public JpaListStringType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor<ListString> javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
    }

}
