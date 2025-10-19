package com.common.infrastructure.validation.groups;

import com.common.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.common.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.common.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.GroupSequence;

@GroupSequence(
    {NotBlankGroups.class, SizeGroups.class, CustomGroups.class})
public interface ValidationSequence {

}
