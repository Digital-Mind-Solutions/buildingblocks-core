package org.digitalmind.buildingblocks.core.jpautils.entity.customtype;

import org.digitalmind.buildingblocks.core.jpautils.entity.customtype.base.GenericJavaDescriptor;
import org.digitalmind.buildingblocks.core.jpautils.entity.customtype.base.GenericType;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class JpaParametersType extends GenericType<Parameters> {

    public static class MutabilityPlan extends MutableMutabilityPlan<Parameters> {

        public static final MutabilityPlan INSTANCE = new MutabilityPlan();

        @Override
        protected Parameters deepCopyNotNull(Parameters value) {
            return new Parameters(value);
        }

    }

    public static final JpaParametersType INSTANCE = new JpaParametersType();

    public static final GenericJavaDescriptor<Parameters> DESCRIPTOR =
            new GenericJavaDescriptor<Parameters>(Parameters.class, MutabilityPlan.INSTANCE);

    public JpaParametersType() {
        super(VarcharTypeDescriptor.INSTANCE, DESCRIPTOR);
    }

    //ClobTypeDescriptor.CLOB_BINDING

}
