package org.logistics.entityService.validation.definitions;

import org.logistics.entityService.constants.EntityTypes;
import org.logistics.entityService.validation.annotations.CompanyType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompanyTypeValidation implements ConstraintValidator<CompanyType, String[]> {
    @Override
    public void initialize(CompanyType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String[] types, ConstraintValidatorContext constraintValidatorContext) {
        if(types == null || types.length == 0)
            return false;
        for(String type: types) {
            if(type.equals(EntityTypes.CNR.name()) || type.equals(EntityTypes.TRN.name()) || type.equals(EntityTypes.CEE.name()))
                continue;
            else return false;
        }
        return true;
    }
}
