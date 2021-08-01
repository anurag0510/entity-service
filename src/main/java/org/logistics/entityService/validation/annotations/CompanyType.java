package org.logistics.entityService.validation.annotations;

import org.logistics.entityService.validation.definitions.CompanyTypeValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompanyTypeValidation.class)
public @interface CompanyType {

    String message() default "types should be out of CNR, CEE or TRN only";

    Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};
}
