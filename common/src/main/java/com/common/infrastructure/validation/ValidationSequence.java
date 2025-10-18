package com.common.infrastructure.validation;

import com.common.infrastructure.validation.ValidationGroups.CustomGroups;
import com.common.infrastructure.validation.ValidationGroups.NotBlankGroups;
import com.common.infrastructure.validation.ValidationGroups.SizeGroups;
import jakarta.validation.GroupSequence;

@GroupSequence(
    {NotBlankGroups.class, SizeGroups.class, CustomGroups.class})
public interface ValidationSequence {

}
