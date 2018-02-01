package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.ast.om.Location;
import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
import com.sun.xml.internal.rngom.parse.Context;
import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
import com.sun.xml.internal.rngom.parse.Parseable;
import java.util.List;

public abstract interface SchemaBuilder<N extends ParsedNameClass, P extends ParsedPattern, E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
{
  public abstract NameClassBuilder<N, E, L, A, CL> getNameClassBuilder()
    throws BuildException;

  public abstract P makeChoice(List<P> paramList, L paramL, A paramA)
    throws BuildException;

  public abstract P makeInterleave(List<P> paramList, L paramL, A paramA)
    throws BuildException;

  public abstract P makeGroup(List<P> paramList, L paramL, A paramA)
    throws BuildException;

  public abstract P makeOneOrMore(P paramP, L paramL, A paramA)
    throws BuildException;

  public abstract P makeZeroOrMore(P paramP, L paramL, A paramA)
    throws BuildException;

  public abstract P makeOptional(P paramP, L paramL, A paramA)
    throws BuildException;

  public abstract P makeList(P paramP, L paramL, A paramA)
    throws BuildException;

  public abstract P makeMixed(P paramP, L paramL, A paramA)
    throws BuildException;

  public abstract P makeEmpty(L paramL, A paramA);

  public abstract P makeNotAllowed(L paramL, A paramA);

  public abstract P makeText(L paramL, A paramA);

  public abstract P makeAttribute(N paramN, P paramP, L paramL, A paramA)
    throws BuildException;

  public abstract P makeElement(N paramN, P paramP, L paramL, A paramA)
    throws BuildException;

  public abstract DataPatternBuilder makeDataPatternBuilder(String paramString1, String paramString2, L paramL)
    throws BuildException;

  public abstract P makeValue(String paramString1, String paramString2, String paramString3, Context paramContext, String paramString4, L paramL, A paramA)
    throws BuildException;

  public abstract Grammar<P, E, L, A, CL> makeGrammar(Scope<P, E, L, A, CL> paramScope);

  public abstract P annotate(P paramP, A paramA)
    throws BuildException;

  public abstract P annotateAfter(P paramP, E paramE)
    throws BuildException;

  public abstract P commentAfter(P paramP, CL paramCL)
    throws BuildException;

  public abstract P makeExternalRef(Parseable paramParseable, String paramString1, String paramString2, Scope<P, E, L, A, CL> paramScope, L paramL, A paramA)
    throws BuildException, IllegalSchemaException;

  public abstract L makeLocation(String paramString, int paramInt1, int paramInt2);

  public abstract A makeAnnotations(CL paramCL, Context paramContext);

  public abstract ElementAnnotationBuilder<P, E, L, A, CL> makeElementAnnotationBuilder(String paramString1, String paramString2, String paramString3, L paramL, CL paramCL, Context paramContext);

  public abstract CL makeCommentList();

  public abstract P makeErrorPattern();

  public abstract boolean usesComments();

  public abstract P expandPattern(P paramP)
    throws BuildException, IllegalSchemaException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.SchemaBuilder
 * JD-Core Version:    0.6.2
 */