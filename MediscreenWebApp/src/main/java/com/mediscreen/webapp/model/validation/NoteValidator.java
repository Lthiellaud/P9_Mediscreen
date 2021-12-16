package com.mediscreen.webapp.model.validation;

import com.mediscreen.webapp.utils.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NoteValidator implements ConstraintValidator<ValidNote, String> {


    @Override
    public void initialize(ValidNote constraintNote) {

    }

    @Override
    public boolean isValid(String note, ConstraintValidatorContext constraintValidatorContext) {

        return !StringUtil.nettoyerTags(note).equals("");
    }
}
