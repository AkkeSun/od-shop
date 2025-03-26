package com.account.infrastructure.validation;

import com.account.infrastructure.validation.ValidationGroups.CustomGroups;
import com.account.infrastructure.validation.ValidationGroups.NotBlankGroups;
import com.account.infrastructure.validation.ValidationGroups.SizeGroups;
import jakarta.validation.GroupSequence;

@GroupSequence(
    {NotBlankGroups.class, SizeGroups.class, CustomGroups.class})
public interface ValidationSequence {

}
