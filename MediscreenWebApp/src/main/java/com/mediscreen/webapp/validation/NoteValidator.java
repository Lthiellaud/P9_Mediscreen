package com.mediscreen.webapp.validation;

import com.mediscreen.webapp.utils.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoteValidator implements ConstraintValidator<ValidNote, String> {


    @Override
    public void initialize(ValidNote constraintNote) {

    }

    @Override
    public boolean isValid(String note, ConstraintValidatorContext constraintValidatorContext) {

        return !StringUtil.nettoyerTags(note).equals("");
    }
}
