package org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype;

import org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.base.GenericJavaDescriptor;
import org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.base.GenericType;
import org.digitalmind.buildingblocks.core.jpaauditor.entity.extension.Secret;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class JpaSecretType extends GenericType<Secret> {

    public static class MutabilityPlan extends MutableMutabilityPlan<Secret> {

        public static final MutabilityPlan INSTANCE = new MutabilityPlan();

        @Override
        protected Secret deepCopyNotNull(Secret value) {
            return new Secret(value);
        }

    }

    public static final JpaSecretType INSTANCE = new JpaSecretType();

    public static final GenericJavaDescriptor<Secret> DESCRIPTOR = new GenericJavaDescriptor<Secret>(Secret.class, MutabilityPlan.INSTANCE);

    public JpaSecretType() {
        super(VarcharTypeDescriptor.INSTANCE, DESCRIPTOR);
    }

    protected JpaSecretType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor<Secret> javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
    }

}
