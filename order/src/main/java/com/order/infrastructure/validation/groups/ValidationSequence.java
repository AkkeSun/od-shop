package com.order.infrastructure.validation.groups;


import com.order.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.order.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.order.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankGroups.class, SizeGroups.class, CustomGroups.class})
public interface ValidationSequence {

}
