package com.sun.xml.internal.rngom.parse;

import com.sun.xml.internal.rngom.ast.builder.BuildException;
import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
import com.sun.xml.internal.rngom.ast.builder.Scope;
import com.sun.xml.internal.rngom.ast.om.ParsedPattern;

public abstract interface Parseable
{
  public abstract <P extends ParsedPattern> P parse(SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder)
    throws BuildException, IllegalSchemaException;

  public abstract <P extends ParsedPattern> P parseInclude(String paramString1, SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder, IncludedGrammar<P, ?, ?, ?, ?> paramIncludedGrammar, String paramString2)
    throws BuildException, IllegalSchemaException;

  public abstract <P extends ParsedPattern> P parseExternal(String paramString1, SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder, Scope paramScope, String paramString2)
    throws BuildException, IllegalSchemaException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.Parseable
 * JD-Core Version:    0.6.2
 */