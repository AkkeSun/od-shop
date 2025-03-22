package com.account.global.validation;

import com.account.global.validation.ValidationGroups.CustomGroups;
import com.account.global.validation.ValidationGroups.NotBlankGroups;
import com.account.global.validation.ValidationGroups.SizeGroups;
import jakarta.validation.GroupSequence;

@GroupSequence(
    {NotBlankGroups.class, SizeGroups.class, CustomGroups.class})
public interface ValidationSequence {

}
