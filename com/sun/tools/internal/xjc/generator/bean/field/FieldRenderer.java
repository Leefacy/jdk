package com.sun.tools.internal.xjc.generator.bean.field;

import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
import com.sun.tools.internal.xjc.model.CPropertyInfo;
import com.sun.tools.internal.xjc.outline.FieldOutline;

public abstract interface FieldRenderer
{
  public abstract FieldOutline generate(ClassOutlineImpl paramClassOutlineImpl, CPropertyInfo paramCPropertyInfo);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer
 * JD-Core Version:    0.6.2
 */