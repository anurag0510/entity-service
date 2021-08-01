package org.logistics.entityService.data.type;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import com.vladmihalcea.hibernate.type.array.internal.ArraySqlTypeDescriptor;

import java.util.Properties;

public class EntityArrayType extends AbstractSingleColumnStandardBasicType<String[]> {

    public static final EntityArrayType INSTANCE = new EntityArrayType();

    public EntityArrayType() {
        super(ArraySqlTypeDescriptor.INSTANCE, EntityArrayTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "entity-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    public void setParameterValues(Properties parameters) {
        ((EntityArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
