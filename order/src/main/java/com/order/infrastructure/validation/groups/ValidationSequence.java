package com.order.infrastructure.validation.groups;


import com.product.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankGroups.class, SizeGroups.class, CustomGroups.class})
public interface ValidationSequence {

}
