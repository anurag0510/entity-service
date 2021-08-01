package org.logistics.entityService.data.type;

import com.vladmihalcea.hibernate.type.array.internal.AbstractArrayTypeDescriptor;

public class EntityArrayTypeDescriptor extends AbstractArrayTypeDescriptor<String[]> {

    public static final EntityArrayTypeDescriptor INSTANCE = new EntityArrayTypeDescriptor();

    public EntityArrayTypeDescriptor() {
        super(String[].class);
    }

    protected String getSqlArrayType() {
        return "entity_type";
    }
}
