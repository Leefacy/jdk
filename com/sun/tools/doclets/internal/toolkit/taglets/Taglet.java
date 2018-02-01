package com.sun.tools.doclets.internal.toolkit.taglets;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.internal.toolkit.Content;

public abstract interface Taglet
{
  public abstract boolean inField();

  public abstract boolean inConstructor();

  public abstract boolean inMethod();

  public abstract boolean inOverview();

  public abstract boolean inPackage();

  public abstract boolean inType();

  public abstract boolean isInlineTag();

  public abstract String getName();

  public abstract Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
    throws IllegalArgumentException;

  public abstract Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
    throws IllegalArgumentException;

  public abstract String toString();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.Taglet
 * JD-Core Version:    0.6.2
 */