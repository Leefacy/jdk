/*      */ package sun.tools.java;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import sun.tools.javac.SourceClass;
/*      */ import sun.tools.javac.SourceMember;
/*      */ import sun.tools.tree.Context;
/*      */ import sun.tools.tree.Expression;
/*      */ import sun.tools.tree.LocalMember;
/*      */ import sun.tools.tree.UplevelReference;
/*      */ import sun.tools.tree.Vset;
/*      */ 
/*      */ public class ClassDefinition
/*      */   implements Constants
/*      */ {
/*      */   protected Object source;
/*      */   protected long where;
/*      */   protected int modifiers;
/*      */   protected Identifier localName;
/*      */   protected ClassDeclaration declaration;
/*      */   protected IdentifierToken superClassId;
/*      */   protected IdentifierToken[] interfaceIds;
/*      */   protected ClassDeclaration superClass;
/*      */   protected ClassDeclaration[] interfaces;
/*      */   protected ClassDefinition outerClass;
/*      */   protected MemberDefinition outerMember;
/*      */   protected MemberDefinition innerClassMember;
/*      */   protected MemberDefinition firstMember;
/*      */   protected MemberDefinition lastMember;
/*      */   protected boolean resolved;
/*      */   protected String documentation;
/*      */   protected boolean error;
/*      */   protected boolean nestError;
/*      */   protected UplevelReference references;
/*      */   protected boolean referencesFrozen;
/*   67 */   private Hashtable fieldHash = new Hashtable(31);
/*      */   private int abstr;
/*   74 */   private Hashtable localClasses = null;
/*   75 */   private final int LOCAL_CLASSES_SIZE = 31;
/*      */   protected Context classContext;
/*  185 */   protected boolean supersCheckStarted = !(this instanceof SourceClass);
/*      */ 
/* 1179 */   MethodSet allMethods = null;
/*      */ 
/* 1189 */   private List permanentlyAbstractMethods = new ArrayList();
/*      */ 
/* 1208 */   protected static boolean doInheritanceChecks = true;
/*      */ 
/*      */   public Context getClassContext()
/*      */   {
/*   87 */     return this.classContext;
/*      */   }
/*      */ 
/*      */   protected ClassDefinition(Object paramObject, long paramLong, ClassDeclaration paramClassDeclaration, int paramInt, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken)
/*      */   {
/*   96 */     this.source = paramObject;
/*   97 */     this.where = paramLong;
/*   98 */     this.declaration = paramClassDeclaration;
/*   99 */     this.modifiers = paramInt;
/*  100 */     this.superClassId = paramIdentifierToken;
/*  101 */     this.interfaceIds = paramArrayOfIdentifierToken;
/*      */   }
/*      */ 
/*      */   public final Object getSource()
/*      */   {
/*  108 */     return this.source;
/*      */   }
/*      */ 
/*      */   public final boolean getError()
/*      */   {
/*  115 */     return this.error;
/*      */   }
/*      */ 
/*      */   public final void setError()
/*      */   {
/*  122 */     this.error = true;
/*  123 */     setNestError();
/*      */   }
/*      */ 
/*      */   public final boolean getNestError()
/*      */   {
/*  137 */     return (this.nestError) || ((this.outerClass != null) && (this.outerClass.getNestError()));
/*      */   }
/*      */ 
/*      */   public final void setNestError()
/*      */   {
/*  145 */     this.nestError = true;
/*  146 */     if (this.outerClass != null)
/*      */     {
/*  153 */       this.outerClass.setNestError();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final long getWhere()
/*      */   {
/*  161 */     return this.where;
/*      */   }
/*      */ 
/*      */   public final ClassDeclaration getClassDeclaration()
/*      */   {
/*  168 */     return this.declaration;
/*      */   }
/*      */ 
/*      */   public final int getModifiers()
/*      */   {
/*  175 */     return this.modifiers;
/*      */   }
/*      */   public final void subModifiers(int paramInt) {
/*  178 */     this.modifiers &= (paramInt ^ 0xFFFFFFFF);
/*      */   }
/*      */   public final void addModifiers(int paramInt) {
/*  181 */     this.modifiers |= paramInt;
/*      */   }
/*      */ 
/*      */   public final ClassDeclaration getSuperClass()
/*      */   {
/*  201 */     if (!this.supersCheckStarted) throw new CompilerError("unresolved super");
/*      */ 
/*  203 */     return this.superClass;
/*      */   }
/*      */ 
/*      */   public ClassDeclaration getSuperClass(Environment paramEnvironment)
/*      */   {
/*  220 */     return getSuperClass();
/*      */   }
/*      */ 
/*      */   public final ClassDeclaration[] getInterfaces()
/*      */   {
/*  227 */     if (this.interfaces == null) throw new CompilerError("getInterfaces");
/*  228 */     return this.interfaces;
/*      */   }
/*      */ 
/*      */   public final ClassDefinition getOuterClass()
/*      */   {
/*  235 */     return this.outerClass;
/*      */   }
/*      */ 
/*      */   protected final void setOuterClass(ClassDefinition paramClassDefinition)
/*      */   {
/*  242 */     if (this.outerClass != null) throw new CompilerError("setOuterClass");
/*  243 */     this.outerClass = paramClassDefinition;
/*      */   }
/*      */ 
/*      */   protected final void setOuterMember(MemberDefinition paramMemberDefinition)
/*      */   {
/*  252 */     if ((isStatic()) || (!isInnerClass())) throw new CompilerError("setOuterField");
/*  253 */     if (this.outerMember != null) throw new CompilerError("setOuterField");
/*  254 */     this.outerMember = paramMemberDefinition;
/*      */   }
/*      */ 
/*      */   public final boolean isInnerClass()
/*      */   {
/*  264 */     return this.outerClass != null;
/*      */   }
/*      */ 
/*      */   public final boolean isMember()
/*      */   {
/*  272 */     return (this.outerClass != null) && (!isLocal());
/*      */   }
/*      */ 
/*      */   public final boolean isTopLevel()
/*      */   {
/*  280 */     return (this.outerClass == null) || (isStatic()) || (isInterface());
/*      */   }
/*      */ 
/*      */   public final boolean isInsideLocal()
/*      */   {
/*  294 */     return (isLocal()) || ((this.outerClass != null) && 
/*  294 */       (this.outerClass
/*  294 */       .isInsideLocal()));
/*      */   }
/*      */ 
/*      */   public final boolean isInsideLocalOrAnonymous()
/*      */   {
/*  304 */     return (isLocal()) || (isAnonymous()) || ((this.outerClass != null) && 
/*  304 */       (this.outerClass
/*  304 */       .isInsideLocalOrAnonymous()));
/*      */   }
/*      */ 
/*      */   public Identifier getLocalName()
/*      */   {
/*  311 */     if (this.localName != null) {
/*  312 */       return this.localName;
/*      */     }
/*      */ 
/*  315 */     return getName().getFlatName().getName();
/*      */   }
/*      */ 
/*      */   public void setLocalName(Identifier paramIdentifier)
/*      */   {
/*  322 */     if (isLocal())
/*  323 */       this.localName = paramIdentifier;
/*      */   }
/*      */ 
/*      */   public final MemberDefinition getInnerClassMember()
/*      */   {
/*  331 */     if (this.outerClass == null)
/*  332 */       return null;
/*  333 */     if (this.innerClassMember == null)
/*      */     {
/*  335 */       Identifier localIdentifier = getName().getFlatName().getName();
/*  336 */       for (MemberDefinition localMemberDefinition = this.outerClass.getFirstMatch(localIdentifier); 
/*  337 */         localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMatch()) {
/*  338 */         if (localMemberDefinition.isInnerClass()) {
/*  339 */           this.innerClassMember = localMemberDefinition;
/*  340 */           break;
/*      */         }
/*      */       }
/*  343 */       if (this.innerClassMember == null)
/*  344 */         throw new CompilerError("getInnerClassField");
/*      */     }
/*  346 */     return this.innerClassMember;
/*      */   }
/*      */ 
/*      */   public final MemberDefinition findOuterMember()
/*      */   {
/*  354 */     return this.outerMember;
/*      */   }
/*      */ 
/*      */   public final boolean isStatic()
/*      */   {
/*  361 */     return (this.modifiers & 0x8) != 0;
/*      */   }
/*      */ 
/*      */   public final ClassDefinition getTopClass()
/*      */   {
/*      */     ClassDefinition localClassDefinition;
/*  369 */     for (Object localObject = this; (localClassDefinition = ((ClassDefinition)localObject).outerClass) != null; localObject = localClassDefinition);
/*  371 */     return localObject;
/*      */   }
/*      */ 
/*      */   public final MemberDefinition getFirstMember()
/*      */   {
/*  378 */     return this.firstMember;
/*      */   }
/*      */   public final MemberDefinition getFirstMatch(Identifier paramIdentifier) {
/*  381 */     return (MemberDefinition)this.fieldHash.get(paramIdentifier);
/*      */   }
/*      */ 
/*      */   public final Identifier getName()
/*      */   {
/*  388 */     return this.declaration.getName();
/*      */   }
/*      */ 
/*      */   public final Type getType()
/*      */   {
/*  395 */     return this.declaration.getType();
/*      */   }
/*      */ 
/*      */   public String getDocumentation()
/*      */   {
/*  402 */     return this.documentation;
/*      */   }
/*      */ 
/*      */   public static boolean containsDeprecated(String paramString)
/*      */   {
/*  411 */     if (paramString == null) {
/*  412 */       return false;
/*      */     }
/*      */ 
/*  415 */     int i = 0;
/*      */ 
/*  417 */     label111: for (; (i = paramString.indexOf("@deprecated", i)) >= 0; 
/*  417 */       i += "@deprecated".length())
/*      */     {
/*      */       char c;
/*  420 */       for (int j = i - 1; j >= 0; j--) {
/*  421 */         c = paramString.charAt(j);
/*  422 */         if ((c == '\n') || (c == '\r')) {
/*      */           break;
/*      */         }
/*  425 */         if (!Character.isSpace(c))
/*      */         {
/*      */           break label111;
/*      */         }
/*      */       }
/*  430 */       j = i + "@deprecated".length();
/*  431 */       if (j < paramString.length()) {
/*  432 */         c = paramString.charAt(j);
/*  433 */         if ((c != '\n') && (c != '\r') && (!Character.isSpace(c)));
/*      */       }
/*      */       else {
/*  437 */         return true;
/*      */       }
/*      */     }
/*  439 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean inSamePackage(ClassDeclaration paramClassDeclaration)
/*      */   {
/*  445 */     return inSamePackage(paramClassDeclaration.getName().getQualifier());
/*      */   }
/*      */ 
/*      */   public final boolean inSamePackage(ClassDefinition paramClassDefinition)
/*      */   {
/*  451 */     return inSamePackage(paramClassDefinition.getName().getQualifier());
/*      */   }
/*      */ 
/*      */   public final boolean inSamePackage(Identifier paramIdentifier) {
/*  455 */     return getName().getQualifier().equals(paramIdentifier);
/*      */   }
/*      */ 
/*      */   public final boolean isInterface()
/*      */   {
/*  462 */     return (getModifiers() & 0x200) != 0;
/*      */   }
/*      */   public final boolean isClass() {
/*  465 */     return (getModifiers() & 0x200) == 0;
/*      */   }
/*      */   public final boolean isPublic() {
/*  468 */     return (getModifiers() & 0x1) != 0;
/*      */   }
/*      */   public final boolean isPrivate() {
/*  471 */     return (getModifiers() & 0x2) != 0;
/*      */   }
/*      */   public final boolean isProtected() {
/*  474 */     return (getModifiers() & 0x4) != 0;
/*      */   }
/*      */   public final boolean isPackagePrivate() {
/*  477 */     return (this.modifiers & 0x7) == 0;
/*      */   }
/*      */   public final boolean isFinal() {
/*  480 */     return (getModifiers() & 0x10) != 0;
/*      */   }
/*      */   public final boolean isAbstract() {
/*  483 */     return (getModifiers() & 0x400) != 0;
/*      */   }
/*      */   public final boolean isSynthetic() {
/*  486 */     return (getModifiers() & 0x80000) != 0;
/*      */   }
/*      */   public final boolean isDeprecated() {
/*  489 */     return (getModifiers() & 0x40000) != 0;
/*      */   }
/*      */   public final boolean isAnonymous() {
/*  492 */     return (getModifiers() & 0x10000) != 0;
/*      */   }
/*      */   public final boolean isLocal() {
/*  495 */     return (getModifiers() & 0x20000) != 0;
/*      */   }
/*      */   public final boolean hasConstructor() {
/*  498 */     return getFirstMatch(idInit) != null;
/*      */   }
/*      */ 
/*      */   public final boolean mustBeAbstract(Environment paramEnvironment)
/*      */   {
/*  509 */     if (isAbstract()) {
/*  510 */       return true;
/*      */     }
/*      */ 
/*  518 */     collectInheritedMethods(paramEnvironment);
/*      */ 
/*  522 */     Iterator localIterator = getMethods();
/*  523 */     while (localIterator.hasNext()) {
/*  524 */       MemberDefinition localMemberDefinition = (MemberDefinition)localIterator.next();
/*      */ 
/*  526 */       if (localMemberDefinition.isAbstract()) {
/*  527 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  533 */     return getPermanentlyAbstractMethods().hasNext();
/*      */   }
/*      */ 
/*      */   public boolean superClassOf(Environment paramEnvironment, ClassDeclaration paramClassDeclaration)
/*      */     throws ClassNotFound
/*      */   {
/*  541 */     while (paramClassDeclaration != null) {
/*  542 */       if (getClassDeclaration().equals(paramClassDeclaration)) {
/*  543 */         return true;
/*      */       }
/*  545 */       paramClassDeclaration = paramClassDeclaration.getClassDefinition(paramEnvironment).getSuperClass();
/*      */     }
/*  547 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean enclosingClassOf(ClassDefinition paramClassDefinition)
/*      */   {
/*  554 */     while ((paramClassDefinition = paramClassDefinition.getOuterClass()) != null) {
/*  555 */       if (this == paramClassDefinition) {
/*  556 */         return true;
/*      */       }
/*      */     }
/*  559 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean subClassOf(Environment paramEnvironment, ClassDeclaration paramClassDeclaration)
/*      */     throws ClassNotFound
/*      */   {
/*  566 */     ClassDeclaration localClassDeclaration = getClassDeclaration();
/*  567 */     while (localClassDeclaration != null) {
/*  568 */       if (localClassDeclaration.equals(paramClassDeclaration)) {
/*  569 */         return true;
/*      */       }
/*  571 */       localClassDeclaration = localClassDeclaration.getClassDefinition(paramEnvironment).getSuperClass();
/*      */     }
/*  573 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean implementedBy(Environment paramEnvironment, ClassDeclaration paramClassDeclaration)
/*      */     throws ClassNotFound
/*      */   {
/*  580 */     for (; paramClassDeclaration != null; paramClassDeclaration = paramClassDeclaration.getClassDefinition(paramEnvironment).getSuperClass()) {
/*  581 */       if (getClassDeclaration().equals(paramClassDeclaration)) {
/*  582 */         return true;
/*      */       }
/*  584 */       ClassDeclaration[] arrayOfClassDeclaration = paramClassDeclaration.getClassDefinition(paramEnvironment).getInterfaces();
/*  585 */       for (int i = 0; i < arrayOfClassDeclaration.length; i++) {
/*  586 */         if (implementedBy(paramEnvironment, arrayOfClassDeclaration[i])) {
/*  587 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  591 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean couldImplement(ClassDefinition paramClassDefinition)
/*      */   {
/*  607 */     if (!doInheritanceChecks) {
/*  608 */       throw new CompilerError("couldImplement: no checks");
/*      */     }
/*      */ 
/*  612 */     if ((!isInterface()) || (!paramClassDefinition.isInterface())) {
/*  613 */       throw new CompilerError("couldImplement: not interface");
/*      */     }
/*      */ 
/*  618 */     if (this.allMethods == null) {
/*  619 */       throw new CompilerError("couldImplement: called early");
/*      */     }
/*      */ 
/*  626 */     Iterator localIterator = paramClassDefinition.getMethods();
/*      */ 
/*  628 */     while (localIterator.hasNext())
/*      */     {
/*  631 */       MemberDefinition localMemberDefinition1 = (MemberDefinition)localIterator
/*  631 */         .next();
/*      */ 
/*  633 */       Identifier localIdentifier = localMemberDefinition1.getName();
/*  634 */       Type localType = localMemberDefinition1.getType();
/*      */ 
/*  637 */       MemberDefinition localMemberDefinition2 = this.allMethods.lookupSig(localIdentifier, localType);
/*      */ 
/*  642 */       if (localMemberDefinition2 != null)
/*      */       {
/*  644 */         if (!localMemberDefinition2.sameReturnType(localMemberDefinition1)) {
/*  645 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  650 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean extendsCanAccess(Environment paramEnvironment, ClassDeclaration paramClassDeclaration)
/*      */     throws ClassNotFound
/*      */   {
/*  661 */     if (this.outerClass != null) {
/*  662 */       return this.outerClass.canAccess(paramEnvironment, paramClassDeclaration);
/*      */     }
/*      */ 
/*  667 */     ClassDefinition localClassDefinition = paramClassDeclaration.getClassDefinition(paramEnvironment);
/*      */ 
/*  669 */     if (localClassDefinition.isLocal())
/*      */     {
/*  672 */       throw new CompilerError("top local");
/*      */     }
/*      */ 
/*  675 */     if (localClassDefinition.isInnerClass()) {
/*  676 */       MemberDefinition localMemberDefinition = localClassDefinition.getInnerClassMember();
/*      */ 
/*  679 */       if (localMemberDefinition.isPublic()) {
/*  680 */         return true;
/*      */       }
/*      */ 
/*  686 */       if (localMemberDefinition.isPrivate()) {
/*  687 */         return getClassDeclaration().equals(localMemberDefinition.getTopClass().getClassDeclaration());
/*      */       }
/*      */ 
/*  691 */       return getName().getQualifier().equals(localMemberDefinition.getClassDeclaration().getName().getQualifier());
/*      */     }
/*      */ 
/*  695 */     if (localClassDefinition.isPublic()) {
/*  696 */       return true;
/*      */     }
/*      */ 
/*  700 */     return getName().getQualifier().equals(paramClassDeclaration.getName().getQualifier());
/*      */   }
/*      */ 
/*      */   public boolean canAccess(Environment paramEnvironment, ClassDeclaration paramClassDeclaration)
/*      */     throws ClassNotFound
/*      */   {
/*  707 */     ClassDefinition localClassDefinition = paramClassDeclaration.getClassDefinition(paramEnvironment);
/*      */ 
/*  709 */     if (localClassDefinition.isLocal())
/*      */     {
/*  711 */       return true;
/*      */     }
/*      */ 
/*  714 */     if (localClassDefinition.isInnerClass()) {
/*  715 */       return canAccess(paramEnvironment, localClassDefinition.getInnerClassMember());
/*      */     }
/*      */ 
/*  719 */     if (localClassDefinition.isPublic()) {
/*  720 */       return true;
/*      */     }
/*      */ 
/*  724 */     return getName().getQualifier().equals(paramClassDeclaration.getName().getQualifier());
/*      */   }
/*      */ 
/*      */   public boolean canAccess(Environment paramEnvironment, MemberDefinition paramMemberDefinition)
/*      */     throws ClassNotFound
/*      */   {
/*  735 */     if (paramMemberDefinition.isPublic()) {
/*  736 */       return true;
/*      */     }
/*      */ 
/*  739 */     if ((paramMemberDefinition.isProtected()) && (subClassOf(paramEnvironment, paramMemberDefinition.getClassDeclaration()))) {
/*  740 */       return true;
/*      */     }
/*      */ 
/*  743 */     if (paramMemberDefinition.isPrivate())
/*      */     {
/*  745 */       return getTopClass().getClassDeclaration()
/*  745 */         .equals(paramMemberDefinition
/*  745 */         .getTopClass().getClassDeclaration());
/*      */     }
/*      */ 
/*  748 */     return getName().getQualifier().equals(paramMemberDefinition.getClassDeclaration().getName().getQualifier());
/*      */   }
/*      */ 
/*      */   public boolean permitInlinedAccess(Environment paramEnvironment, ClassDeclaration paramClassDeclaration)
/*      */     throws ClassNotFound
/*      */   {
/*  759 */     return ((paramEnvironment.opt()) && (paramClassDeclaration.equals(this.declaration))) || (
/*  759 */       (paramEnvironment
/*  759 */       .opt_interclass()) && (canAccess(paramEnvironment, paramClassDeclaration)));
/*      */   }
/*      */ 
/*      */   public boolean permitInlinedAccess(Environment paramEnvironment, MemberDefinition paramMemberDefinition)
/*      */     throws ClassNotFound
/*      */   {
/*  770 */     return ((paramEnvironment.opt()) && 
/*  769 */       (paramMemberDefinition.clazz
/*  769 */       .getClassDeclaration().equals(this.declaration))) || (
/*  770 */       (paramEnvironment
/*  770 */       .opt_interclass()) && (canAccess(paramEnvironment, paramMemberDefinition)));
/*      */   }
/*      */ 
/*      */   public boolean protectedAccess(Environment paramEnvironment, MemberDefinition paramMemberDefinition, Type paramType)
/*      */     throws ClassNotFound
/*      */   {
/*  798 */     return (paramMemberDefinition
/*  788 */       .isStatic()) || 
/*  790 */       ((paramType
/*  790 */       .isType(9)) && 
/*  790 */       (paramMemberDefinition.getName() == idClone) && 
/*  791 */       (paramMemberDefinition
/*  791 */       .getType().getArgumentTypes().length == 0)) || 
/*  793 */       ((paramType
/*  793 */       .isType(10)) && 
/*  795 */       (paramEnvironment
/*  794 */       .getClassDefinition(paramType
/*  794 */       .getClassName())
/*  795 */       .subClassOf(paramEnvironment, 
/*  795 */       getClassDeclaration()))) || 
/*  798 */       (getName().getQualifier()
/*  798 */       .equals(paramMemberDefinition
/*  798 */       .getClassDeclaration().getName().getQualifier()));
/*      */   }
/*      */ 
/*      */   public MemberDefinition getAccessMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean)
/*      */   {
/*  808 */     throw new CompilerError("binary getAccessMember");
/*      */   }
/*      */ 
/*      */   public MemberDefinition getUpdateMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean)
/*      */   {
/*  817 */     throw new CompilerError("binary getUpdateMember");
/*      */   }
/*      */ 
/*      */   public MemberDefinition getVariable(Environment paramEnvironment, Identifier paramIdentifier, ClassDefinition paramClassDefinition)
/*      */     throws AmbiguousMember, ClassNotFound
/*      */   {
/*  836 */     return getVariable0(paramEnvironment, paramIdentifier, paramClassDefinition, true, true);
/*      */   }
/*      */ 
/*      */   private MemberDefinition getVariable0(Environment paramEnvironment, Identifier paramIdentifier, ClassDefinition paramClassDefinition, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws AmbiguousMember, ClassNotFound
/*      */   {
/*  854 */     for (Object localObject1 = getFirstMatch(paramIdentifier); 
/*  855 */       localObject1 != null; 
/*  856 */       localObject1 = ((MemberDefinition)localObject1).getNextMatch()) {
/*  857 */       if (((MemberDefinition)localObject1).isVariable()) {
/*  858 */         if (((paramBoolean1) || (!((MemberDefinition)localObject1).isPrivate())) && ((paramBoolean2) || 
/*  859 */           (!((MemberDefinition)localObject1)
/*  859 */           .isPackagePrivate())))
/*      */         {
/*  861 */           return localObject1;
/*      */         }
/*      */ 
/*  865 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  871 */     localObject1 = getSuperClass();
/*  872 */     Object localObject2 = null;
/*  873 */     if (localObject1 != null)
/*      */     {
/*  875 */       if (paramBoolean2);
/*  876 */       localObject2 = ((ClassDeclaration)localObject1)
/*  875 */         .getClassDefinition(paramEnvironment)
/*  876 */         .getVariable0(paramEnvironment, paramIdentifier, paramClassDefinition, false, 
/*  878 */         inSamePackage((ClassDeclaration)localObject1));
/*      */     }
/*      */ 
/*  882 */     for (int i = 0; i < this.interfaces.length; i++)
/*      */     {
/*  888 */       MemberDefinition localMemberDefinition = this.interfaces[i]
/*  887 */         .getClassDefinition(paramEnvironment)
/*  888 */         .getVariable0(paramEnvironment, paramIdentifier, paramClassDefinition, true, true);
/*      */ 
/*  890 */       if (localMemberDefinition != null)
/*      */       {
/*  893 */         if ((localObject2 != null) && 
/*  894 */           (paramClassDefinition
/*  894 */           .canAccess(paramEnvironment, (MemberDefinition)localObject2)) && 
/*  894 */           (localMemberDefinition != localObject2))
/*      */         {
/*  897 */           throw new AmbiguousMember(localMemberDefinition, (MemberDefinition)localObject2);
/*      */         }
/*  899 */         localObject2 = localMemberDefinition;
/*      */       }
/*      */     }
/*  902 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public boolean reportDeprecated(Environment paramEnvironment)
/*      */   {
/*  910 */     return (isDeprecated()) || ((this.outerClass != null) && 
/*  910 */       (this.outerClass
/*  910 */       .reportDeprecated(paramEnvironment)));
/*      */   }
/*      */ 
/*      */   public void noteUsedBy(ClassDefinition paramClassDefinition, long paramLong, Environment paramEnvironment)
/*      */   {
/*  919 */     if (reportDeprecated(paramEnvironment))
/*  920 */       paramEnvironment.error(paramLong, "warn.class.is.deprecated", this);
/*      */   }
/*      */ 
/*      */   public MemberDefinition getInnerClass(Environment paramEnvironment, Identifier paramIdentifier)
/*      */     throws ClassNotFound
/*      */   {
/*  941 */     for (Object localObject = getFirstMatch(paramIdentifier); 
/*  942 */       localObject != null; localObject = ((MemberDefinition)localObject).getNextMatch()) {
/*  943 */       if ((((MemberDefinition)localObject).isInnerClass()) && 
/*  944 */         (!((MemberDefinition)localObject).getInnerClass().isLocal()))
/*      */       {
/*  947 */         return localObject;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  968 */     localObject = getSuperClass(paramEnvironment);
/*  969 */     if (localObject != null) {
/*  970 */       return ((ClassDeclaration)localObject).getClassDefinition(paramEnvironment).getInnerClass(paramEnvironment, paramIdentifier);
/*      */     }
/*  972 */     return null;
/*      */   }
/*      */ 
/*      */   private MemberDefinition matchMethod(Environment paramEnvironment, ClassDefinition paramClassDefinition, Identifier paramIdentifier1, Type[] paramArrayOfType, boolean paramBoolean, Identifier paramIdentifier2)
/*      */     throws AmbiguousMember, ClassNotFound
/*      */   {
/*  989 */     if ((this.allMethods == null) || (!this.allMethods.isFrozen()))
/*      */     {
/*  991 */       throw new CompilerError("matchMethod called early");
/*      */     }
/*      */ 
/*  996 */     Object localObject1 = null;
/*      */ 
/*  999 */     ArrayList localArrayList = null;
/*      */ 
/* 1003 */     Iterator localIterator = this.allMethods.lookupName(paramIdentifier1);
/*      */     Object localObject2;
/* 1005 */     while (localIterator.hasNext()) {
/* 1006 */       localObject2 = (MemberDefinition)localIterator.next();
/*      */ 
/* 1009 */       if ((paramEnvironment.isApplicable((MemberDefinition)localObject2, paramArrayOfType)) && 
/* 1014 */         (paramClassDefinition != null ? 
/* 1015 */         paramClassDefinition.canAccess(paramEnvironment, (MemberDefinition)localObject2) : 
/* 1018 */         (!paramBoolean) || (
/* 1019 */         (!((MemberDefinition)localObject2).isPrivate()) && (
/* 1020 */         (!((MemberDefinition)localObject2)
/* 1020 */         .isPackagePrivate()) || (paramIdentifier2 == null) || 
/* 1022 */         (inSamePackage(paramIdentifier2))))))
/*      */       {
/* 1035 */         if (localObject1 == null)
/*      */         {
/* 1037 */           localObject1 = localObject2;
/*      */         }
/* 1039 */         else if (paramEnvironment.isMoreSpecific((MemberDefinition)localObject2, localObject1))
/*      */         {
/* 1042 */           localObject1 = localObject2;
/*      */         }
/* 1047 */         else if (!paramEnvironment.isMoreSpecific(localObject1, (MemberDefinition)localObject2)) {
/* 1048 */           if (localArrayList == null) {
/* 1049 */             localArrayList = new ArrayList();
/*      */           }
/* 1051 */           localArrayList.add(localObject2);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1057 */     if ((localObject1 != null) && (localArrayList != null))
/*      */     {
/* 1060 */       localObject2 = localArrayList.iterator();
/* 1061 */       while (((Iterator)localObject2).hasNext()) {
/* 1062 */         MemberDefinition localMemberDefinition = (MemberDefinition)((Iterator)localObject2).next();
/* 1063 */         if (!paramEnvironment.isMoreSpecific(localObject1, localMemberDefinition)) {
/* 1064 */           throw new AmbiguousMember(localObject1, localMemberDefinition);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1069 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public MemberDefinition matchMethod(Environment paramEnvironment, ClassDefinition paramClassDefinition, Identifier paramIdentifier, Type[] paramArrayOfType)
/*      */     throws AmbiguousMember, ClassNotFound
/*      */   {
/* 1084 */     return matchMethod(paramEnvironment, paramClassDefinition, paramIdentifier, paramArrayOfType, false, null);
/*      */   }
/*      */ 
/*      */   public MemberDefinition matchMethod(Environment paramEnvironment, ClassDefinition paramClassDefinition, Identifier paramIdentifier)
/*      */     throws AmbiguousMember, ClassNotFound
/*      */   {
/* 1099 */     return matchMethod(paramEnvironment, paramClassDefinition, paramIdentifier, Type.noArgs, false, null);
/*      */   }
/*      */ 
/*      */   public MemberDefinition matchAnonConstructor(Environment paramEnvironment, Identifier paramIdentifier, Type[] paramArrayOfType)
/*      */     throws AmbiguousMember, ClassNotFound
/*      */   {
/* 1117 */     return matchMethod(paramEnvironment, null, idInit, paramArrayOfType, true, paramIdentifier);
/*      */   }
/*      */ 
/*      */   public MemberDefinition findMethod(Environment paramEnvironment, Identifier paramIdentifier, Type paramType)
/*      */     throws ClassNotFound
/*      */   {
/* 1133 */     for (MemberDefinition localMemberDefinition = getFirstMatch(paramIdentifier); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMatch())
/*      */     {
/* 1135 */       if (localMemberDefinition.getType().equalArguments(paramType)) {
/* 1136 */         return localMemberDefinition;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1141 */     if (paramIdentifier.equals(idInit)) {
/* 1142 */       return null;
/*      */     }
/*      */ 
/* 1146 */     ClassDeclaration localClassDeclaration = getSuperClass();
/* 1147 */     if (localClassDeclaration == null) {
/* 1148 */       return null;
/*      */     }
/* 1150 */     return localClassDeclaration.getClassDefinition(paramEnvironment).findMethod(paramEnvironment, paramIdentifier, paramType);
/*      */   }
/*      */ 
/*      */   protected void basicCheck(Environment paramEnvironment)
/*      */     throws ClassNotFound
/*      */   {
/* 1156 */     if (this.outerClass != null)
/* 1157 */       this.outerClass.basicCheck(paramEnvironment);
/*      */   }
/*      */ 
/*      */   public void check(Environment paramEnvironment)
/*      */     throws ClassNotFound
/*      */   {
/*      */   }
/*      */ 
/*      */   public Vset checkLocalClass(Environment paramEnvironment, Context paramContext, Vset paramVset, ClassDefinition paramClassDefinition, Expression[] paramArrayOfExpression, Type[] paramArrayOfType)
/*      */     throws ClassNotFound
/*      */   {
/* 1170 */     throw new CompilerError("checkLocalClass");
/*      */   }
/*      */ 
/*      */   protected Iterator getPermanentlyAbstractMethods()
/*      */   {
/* 1197 */     if (this.allMethods == null) {
/* 1198 */       throw new CompilerError("isPermanentlyAbstract() called early");
/*      */     }
/*      */ 
/* 1201 */     return this.permanentlyAbstractMethods.iterator();
/*      */   }
/*      */ 
/*      */   public static void turnOffInheritanceChecks()
/*      */   {
/* 1218 */     doInheritanceChecks = false;
/*      */   }
/*      */ 
/*      */   private void collectOneClass(Environment paramEnvironment, ClassDeclaration paramClassDeclaration, MethodSet paramMethodSet1, MethodSet paramMethodSet2, MethodSet paramMethodSet3)
/*      */   {
/*      */     try
/*      */     {
/* 1241 */       ClassDefinition localClassDefinition = paramClassDeclaration.getClassDefinition(paramEnvironment);
/* 1242 */       Iterator localIterator = localClassDefinition.getMethods(paramEnvironment);
/* 1243 */       while (localIterator.hasNext())
/*      */       {
/* 1245 */         Object localObject = (MemberDefinition)localIterator
/* 1245 */           .next();
/*      */ 
/* 1255 */         if ((!((MemberDefinition)localObject).isPrivate()) && 
/* 1256 */           (!((MemberDefinition)localObject)
/* 1256 */           .isConstructor()) && (
/* 1257 */           (!localClassDefinition
/* 1257 */           .isInterface()) || (((MemberDefinition)localObject).isAbstract())))
/*      */         {
/* 1263 */           Identifier localIdentifier = ((MemberDefinition)localObject).getName();
/* 1264 */           Type localType = ((MemberDefinition)localObject).getType();
/*      */ 
/* 1269 */           MemberDefinition localMemberDefinition1 = paramMethodSet1
/* 1269 */             .lookupSig(localIdentifier, localType);
/*      */ 
/* 1273 */           if ((((MemberDefinition)localObject).isPackagePrivate()) && 
/* 1274 */             (!inSamePackage(((MemberDefinition)localObject)
/* 1274 */             .getClassDeclaration())))
/*      */           {
/* 1276 */             if ((localMemberDefinition1 != null) && ((this instanceof SourceClass)))
/*      */             {
/* 1290 */               paramEnvironment.error(((MemberDefinition)localObject).getWhere(), "warn.no.override.access", localMemberDefinition1, localMemberDefinition1
/* 1293 */                 .getClassDeclaration(), ((MemberDefinition)localObject)
/* 1294 */                 .getClassDeclaration());
/*      */             }
/*      */ 
/* 1303 */             if (((MemberDefinition)localObject).isAbstract()) {
/* 1304 */               this.permanentlyAbstractMethods.add(localObject);
/*      */             }
/*      */ 
/*      */           }
/* 1311 */           else if (localMemberDefinition1 != null)
/*      */           {
/* 1319 */             localMemberDefinition1.checkOverride(paramEnvironment, (MemberDefinition)localObject);
/*      */           }
/*      */           else
/*      */           {
/* 1329 */             MemberDefinition localMemberDefinition2 = paramMethodSet2
/* 1329 */               .lookupSig(localIdentifier, localType);
/*      */ 
/* 1333 */             if (localMemberDefinition2 == null)
/*      */             {
/* 1337 */               if ((paramMethodSet3 != null) && 
/* 1338 */                 (localClassDefinition
/* 1338 */                 .isInterface()) && (!isInterface()))
/*      */               {
/* 1346 */                 localObject = new SourceMember((MemberDefinition)localObject, this, paramEnvironment);
/*      */ 
/* 1349 */                 paramMethodSet3.add((MemberDefinition)localObject);
/*      */               }
/*      */ 
/* 1357 */               paramMethodSet2.add((MemberDefinition)localObject);
/* 1358 */             } else if ((isInterface()) && 
/* 1359 */               (!localMemberDefinition2
/* 1359 */               .isAbstract()) && 
/* 1360 */               (((MemberDefinition)localObject)
/* 1360 */               .isAbstract()))
/*      */             {
/* 1367 */               paramMethodSet2.replace((MemberDefinition)localObject);
/*      */             }
/* 1376 */             else if ((localMemberDefinition2.checkMeet(paramEnvironment, (MemberDefinition)localObject, 
/* 1378 */               getClassDeclaration())) && 
/* 1384 */               (!localMemberDefinition2.couldOverride(paramEnvironment, (MemberDefinition)localObject)))
/*      */             {
/* 1393 */               if (((MemberDefinition)localObject).couldOverride(paramEnvironment, localMemberDefinition2))
/*      */               {
/* 1398 */                 if ((paramMethodSet3 != null) && 
/* 1399 */                   (localClassDefinition
/* 1399 */                   .isInterface()) && (!isInterface()))
/*      */                 {
/* 1407 */                   localObject = new SourceMember((MemberDefinition)localObject, this, paramEnvironment);
/*      */ 
/* 1411 */                   paramMethodSet3.replace((MemberDefinition)localObject);
/*      */                 }
/*      */ 
/* 1417 */                 paramMethodSet2.replace((MemberDefinition)localObject);
/*      */               }
/*      */               else
/*      */               {
/* 1428 */                 paramEnvironment.error(this.where, "nontrivial.meet", localObject, localMemberDefinition2
/* 1430 */                   .getClassDefinition(), ((MemberDefinition)localObject)
/* 1431 */                   .getClassDeclaration());
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (ClassNotFound localClassNotFound) { paramEnvironment.error(getWhere(), "class.not.found", localClassNotFound.name, this); }
/*      */ 
/*      */   }
/*      */ 
/*      */   protected void collectInheritedMethods(Environment paramEnvironment)
/*      */   {
/* 1459 */     if (this.allMethods != null) {
/* 1460 */       if (this.allMethods.isFrozen())
/*      */       {
/* 1463 */         return;
/*      */       }
/*      */ 
/* 1467 */       throw new CompilerError("collectInheritedMethods()");
/*      */     }
/*      */ 
/* 1471 */     MethodSet localMethodSet1 = new MethodSet();
/* 1472 */     this.allMethods = new MethodSet();
/*      */     MethodSet localMethodSet2;
/* 1475 */     if (paramEnvironment.version12())
/* 1476 */       localMethodSet2 = null;
/*      */     else {
/* 1478 */       localMethodSet2 = new MethodSet();
/*      */     }
/*      */ 
/* 1484 */     for (Object localObject = getFirstMember(); 
/* 1485 */       localObject != null; 
/* 1486 */       localObject = ((MemberDefinition)localObject).nextMember)
/*      */     {
/* 1489 */       if ((((MemberDefinition)localObject).isMethod()) && 
/* 1490 */         (!((MemberDefinition)localObject)
/* 1490 */         .isInitializer()))
/*      */       {
/* 1506 */         methodSetAdd(paramEnvironment, localMethodSet1, (MemberDefinition)localObject);
/* 1507 */         methodSetAdd(paramEnvironment, this.allMethods, (MemberDefinition)localObject);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1517 */     localObject = getSuperClass(paramEnvironment);
/* 1518 */     if (localObject != null) {
/* 1519 */       collectOneClass(paramEnvironment, (ClassDeclaration)localObject, localMethodSet1, this.allMethods, localMethodSet2);
/*      */ 
/* 1524 */       ClassDefinition localClassDefinition = ((ClassDeclaration)localObject).getClassDefinition();
/* 1525 */       Iterator localIterator = localClassDefinition.getPermanentlyAbstractMethods();
/* 1526 */       while (localIterator.hasNext()) {
/* 1527 */         this.permanentlyAbstractMethods.add(localIterator.next());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1535 */     for (int i = 0; i < this.interfaces.length; i++) {
/* 1536 */       collectOneClass(paramEnvironment, this.interfaces[i], localMethodSet1, this.allMethods, localMethodSet2);
/*      */     }
/*      */ 
/* 1539 */     this.allMethods.freeze();
/*      */ 
/* 1547 */     if ((localMethodSet2 != null) && (localMethodSet2.size() > 0))
/* 1548 */       addMirandaMethods(paramEnvironment, localMethodSet2.iterator());
/*      */   }
/*      */ 
/*      */   private static void methodSetAdd(Environment paramEnvironment, MethodSet paramMethodSet, MemberDefinition paramMemberDefinition)
/*      */   {
/* 1569 */     MemberDefinition localMemberDefinition = paramMethodSet.lookupSig(paramMemberDefinition.getName(), paramMemberDefinition
/* 1570 */       .getType());
/* 1571 */     if (localMemberDefinition != null) {
/* 1572 */       Type localType1 = localMemberDefinition.getType().getReturnType();
/* 1573 */       Type localType2 = paramMemberDefinition.getType().getReturnType();
/*      */       try {
/* 1575 */         if (paramEnvironment.isMoreSpecific(localType2, localType1))
/* 1576 */           paramMethodSet.replace(paramMemberDefinition);
/*      */       } catch (ClassNotFound localClassNotFound) {
/*      */       }
/*      */     }
/*      */     else {
/* 1581 */       paramMethodSet.add(paramMemberDefinition);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Iterator getMethods(Environment paramEnvironment)
/*      */   {
/* 1591 */     if (this.allMethods == null) {
/* 1592 */       collectInheritedMethods(paramEnvironment);
/*      */     }
/* 1594 */     return getMethods();
/*      */   }
/*      */ 
/*      */   public Iterator getMethods()
/*      */   {
/* 1603 */     if (this.allMethods == null) {
/* 1604 */       throw new CompilerError("getMethods: too early");
/*      */     }
/* 1606 */     return this.allMethods.iterator();
/*      */   }
/*      */ 
/*      */   protected void addMirandaMethods(Environment paramEnvironment, Iterator paramIterator)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void inlineLocalClass(Environment paramEnvironment)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void resolveTypeStructure(Environment paramEnvironment)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Identifier resolveName(Environment paramEnvironment, Identifier paramIdentifier)
/*      */   {
/* 1690 */     paramEnvironment.dtEvent("ClassDefinition.resolveName: " + paramIdentifier);
/*      */ 
/* 1693 */     if (paramIdentifier.isQualified())
/*      */     {
/* 1697 */       Identifier localIdentifier = resolveName(paramEnvironment, paramIdentifier.getHead());
/*      */ 
/* 1699 */       if (localIdentifier.hasAmbigPrefix())
/*      */       {
/* 1704 */         return localIdentifier;
/*      */       }
/*      */ 
/* 1707 */       if (!paramEnvironment.classExists(localIdentifier)) {
/* 1708 */         return paramEnvironment.resolvePackageQualifiedName(paramIdentifier);
/*      */       }
/*      */       try
/*      */       {
/* 1712 */         return paramEnvironment.getClassDefinition(localIdentifier)
/* 1712 */           .resolveInnerClass(paramEnvironment, paramIdentifier
/* 1712 */           .getTail());
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound1) {
/* 1715 */         return Identifier.lookupInner(localIdentifier, paramIdentifier.getTail());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1727 */     int i = -2;
/* 1728 */     LocalMember localLocalMember = null;
/* 1729 */     if (this.classContext != null) {
/* 1730 */       localLocalMember = this.classContext.getLocalClass(paramIdentifier);
/* 1731 */       if (localLocalMember != null) {
/* 1732 */         i = localLocalMember.getScopeNumber();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1737 */     for (ClassDefinition localClassDefinition = this; localClassDefinition != null; localClassDefinition = localClassDefinition.outerClass) {
/*      */       try {
/* 1739 */         MemberDefinition localMemberDefinition = localClassDefinition.getInnerClass(paramEnvironment, paramIdentifier);
/* 1740 */         if ((localMemberDefinition != null) && ((localLocalMember == null) || 
/* 1741 */           (this.classContext
/* 1741 */           .getScopeNumber(localClassDefinition) > 
/* 1741 */           i)))
/*      */         {
/* 1744 */           return localMemberDefinition.getInnerClass().getName();
/*      */         }
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound2)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1752 */     if (localLocalMember != null) {
/* 1753 */       return localLocalMember.getInnerClass().getName();
/*      */     }
/*      */ 
/* 1757 */     return paramEnvironment.resolveName(paramIdentifier);
/*      */   }
/*      */ 
/*      */   public Identifier resolveInnerClass(Environment paramEnvironment, Identifier paramIdentifier)
/*      */   {
/* 1768 */     if (paramIdentifier.isInner()) throw new CompilerError("inner");
/*      */     Object localObject;
/* 1769 */     if (paramIdentifier.isQualified()) {
/* 1770 */       localObject = resolveInnerClass(paramEnvironment, paramIdentifier.getHead());
/*      */       try
/*      */       {
/* 1773 */         return paramEnvironment.getClassDefinition((Identifier)localObject)
/* 1773 */           .resolveInnerClass(paramEnvironment, paramIdentifier
/* 1773 */           .getTail());
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound2) {
/* 1776 */         return Identifier.lookupInner((Identifier)localObject, paramIdentifier.getTail());
/*      */       }
/*      */     }
/*      */     try {
/* 1780 */       localObject = getInnerClass(paramEnvironment, paramIdentifier);
/* 1781 */       if (localObject != null) {
/* 1782 */         return ((MemberDefinition)localObject).getInnerClass().getName();
/*      */       }
/*      */     }
/*      */     catch (ClassNotFound localClassNotFound1)
/*      */     {
/*      */     }
/* 1788 */     return Identifier.lookupInner(getName(), paramIdentifier);
/*      */   }
/*      */ 
/*      */   public boolean innerClassExists(Identifier paramIdentifier)
/*      */   {
/* 1804 */     for (MemberDefinition localMemberDefinition = getFirstMatch(paramIdentifier.getHead()); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMatch()) {
/* 1805 */       if ((localMemberDefinition.isInnerClass()) && 
/* 1806 */         (!localMemberDefinition.getInnerClass().isLocal()))
/*      */       {
/* 1810 */         return (!paramIdentifier.isQualified()) || 
/* 1810 */           (localMemberDefinition
/* 1810 */           .getInnerClass().innerClassExists(paramIdentifier.getTail()));
/*      */       }
/*      */     }
/* 1813 */     return false;
/*      */   }
/*      */ 
/*      */   public MemberDefinition findAnyMethod(Environment paramEnvironment, Identifier paramIdentifier)
/*      */     throws ClassNotFound
/*      */   {
/* 1821 */     for (MemberDefinition localMemberDefinition = getFirstMatch(paramIdentifier); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMatch()) {
/* 1822 */       if (localMemberDefinition.isMethod()) {
/* 1823 */         return localMemberDefinition;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1828 */     ClassDeclaration localClassDeclaration = getSuperClass();
/* 1829 */     if (localClassDeclaration == null)
/* 1830 */       return null;
/* 1831 */     return localClassDeclaration.getClassDefinition(paramEnvironment).findAnyMethod(paramEnvironment, paramIdentifier);
/*      */   }
/*      */ 
/*      */   public int diagnoseMismatch(Environment paramEnvironment, Identifier paramIdentifier, Type[] paramArrayOfType1, int paramInt, Type[] paramArrayOfType2)
/*      */     throws ClassNotFound
/*      */   {
/* 1852 */     int[] arrayOfInt = new int[paramArrayOfType1.length];
/* 1853 */     Type[] arrayOfType = new Type[paramArrayOfType1.length];
/* 1854 */     if (!diagnoseMismatch(paramEnvironment, paramIdentifier, paramArrayOfType1, paramInt, arrayOfInt, arrayOfType))
/* 1855 */       return -2;
/* 1856 */     for (int i = paramInt; i < paramArrayOfType1.length; i++) {
/* 1857 */       if (arrayOfInt[i] < 4) {
/* 1858 */         paramArrayOfType2[0] = arrayOfType[i];
/* 1859 */         return i << 2 | arrayOfInt[i];
/*      */       }
/*      */     }
/* 1862 */     return -1;
/*      */   }
/*      */ 
/*      */   private boolean diagnoseMismatch(Environment paramEnvironment, Identifier paramIdentifier, Type[] paramArrayOfType1, int paramInt, int[] paramArrayOfInt, Type[] paramArrayOfType2)
/*      */     throws ClassNotFound
/*      */   {
/* 1868 */     boolean bool = false;
/*      */ 
/* 1870 */     for (MemberDefinition localMemberDefinition = getFirstMatch(paramIdentifier); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMatch()) {
/* 1871 */       if (localMemberDefinition.isMethod())
/*      */       {
/* 1874 */         localObject = localMemberDefinition.getType().getArgumentTypes();
/* 1875 */         if (localObject.length == paramArrayOfType1.length) {
/* 1876 */           bool = true;
/* 1877 */           for (int i = paramInt; i < paramArrayOfType1.length; i++) {
/* 1878 */             Type localType1 = paramArrayOfType1[i];
/* 1879 */             Type localType2 = localObject[i];
/* 1880 */             if (paramEnvironment.implicitCast(localType1, localType2)) {
/* 1881 */               paramArrayOfInt[i] = 4;
/*      */             } else {
/* 1883 */               if ((paramArrayOfInt[i] <= 2) && (paramEnvironment.explicitCast(localType1, localType2))) {
/* 1884 */                 if (paramArrayOfInt[i] < 2) paramArrayOfType2[i] = null;
/* 1885 */                 paramArrayOfInt[i] = 2; } else {
/* 1886 */                 if (paramArrayOfInt[i] > 0)
/*      */                   continue;
/*      */               }
/* 1889 */               if (paramArrayOfType2[i] == null)
/* 1890 */                 paramArrayOfType2[i] = localType2;
/* 1891 */               else if (paramArrayOfType2[i] != localType2)
/* 1892 */                 paramArrayOfInt[i] |= 1;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1898 */     if (paramIdentifier.equals(idInit)) {
/* 1899 */       return bool;
/*      */     }
/*      */ 
/* 1903 */     Object localObject = getSuperClass();
/* 1904 */     if ((localObject != null) && 
/* 1905 */       (((ClassDeclaration)localObject).getClassDefinition(paramEnvironment).diagnoseMismatch(paramEnvironment, paramIdentifier, paramArrayOfType1, paramInt, paramArrayOfInt, paramArrayOfType2)))
/*      */     {
/* 1907 */       bool = true;
/*      */     }
/* 1909 */     return bool;
/*      */   }
/*      */ 
/*      */   public void addMember(MemberDefinition paramMemberDefinition)
/*      */   {
/* 1917 */     if (this.firstMember == null) {
/* 1918 */       this.firstMember = (this.lastMember = paramMemberDefinition);
/* 1919 */     } else if ((paramMemberDefinition.isSynthetic()) && (paramMemberDefinition.isFinal()) && 
/* 1920 */       (paramMemberDefinition
/* 1920 */       .isVariable()))
/*      */     {
/* 1922 */       paramMemberDefinition.nextMember = this.firstMember;
/* 1923 */       this.firstMember = paramMemberDefinition;
/* 1924 */       paramMemberDefinition.nextMatch = ((MemberDefinition)this.fieldHash.get(paramMemberDefinition.name));
/*      */     } else {
/* 1926 */       this.lastMember.nextMember = paramMemberDefinition;
/* 1927 */       this.lastMember = paramMemberDefinition;
/* 1928 */       paramMemberDefinition.nextMatch = ((MemberDefinition)this.fieldHash.get(paramMemberDefinition.name));
/*      */     }
/* 1930 */     this.fieldHash.put(paramMemberDefinition.name, paramMemberDefinition);
/*      */   }
/*      */ 
/*      */   public void addMember(Environment paramEnvironment, MemberDefinition paramMemberDefinition)
/*      */   {
/* 1937 */     addMember(paramMemberDefinition);
/* 1938 */     if (this.resolved)
/*      */     {
/* 1940 */       paramMemberDefinition.resolveTypeStructure(paramEnvironment);
/*      */     }
/*      */   }
/*      */ 
/*      */   public UplevelReference getReference(LocalMember paramLocalMember)
/*      */   {
/* 1948 */     for (UplevelReference localUplevelReference = this.references; localUplevelReference != null; localUplevelReference = localUplevelReference.getNext()) {
/* 1949 */       if (localUplevelReference.getTarget() == paramLocalMember) {
/* 1950 */         return localUplevelReference;
/*      */       }
/*      */     }
/* 1953 */     return addReference(paramLocalMember);
/*      */   }
/*      */ 
/*      */   protected UplevelReference addReference(LocalMember paramLocalMember) {
/* 1957 */     if (paramLocalMember.getClassDefinition() == this) {
/* 1958 */       throw new CompilerError("addReference " + paramLocalMember);
/*      */     }
/* 1960 */     referencesMustNotBeFrozen();
/* 1961 */     UplevelReference localUplevelReference = new UplevelReference(this, paramLocalMember);
/* 1962 */     this.references = localUplevelReference.insertInto(this.references);
/* 1963 */     return localUplevelReference;
/*      */   }
/*      */ 
/*      */   public UplevelReference getReferences()
/*      */   {
/* 1970 */     return this.references;
/*      */   }
/*      */ 
/*      */   public UplevelReference getReferencesFrozen()
/*      */   {
/* 1979 */     this.referencesFrozen = true;
/* 1980 */     return this.references;
/*      */   }
/*      */ 
/*      */   public final void referencesMustNotBeFrozen()
/*      */   {
/* 1987 */     if (this.referencesFrozen)
/* 1988 */       throw new CompilerError("referencesMustNotBeFrozen " + this);
/*      */   }
/*      */ 
/*      */   public MemberDefinition getClassLiteralLookup(long paramLong)
/*      */   {
/* 1996 */     throw new CompilerError("binary class");
/*      */   }
/*      */ 
/*      */   public void addDependency(ClassDeclaration paramClassDeclaration)
/*      */   {
/* 2003 */     throw new CompilerError("addDependency");
/*      */   }
/*      */ 
/*      */   public ClassDefinition getLocalClass(String paramString)
/*      */   {
/* 2013 */     if (this.localClasses == null) {
/* 2014 */       return null;
/*      */     }
/* 2016 */     return (ClassDefinition)this.localClasses.get(paramString);
/*      */   }
/*      */ 
/*      */   public void addLocalClass(ClassDefinition paramClassDefinition, String paramString)
/*      */   {
/* 2021 */     if (this.localClasses == null) {
/* 2022 */       this.localClasses = new Hashtable(31);
/*      */     }
/* 2024 */     this.localClasses.put(paramString, paramClassDefinition);
/*      */   }
/*      */ 
/*      */   public void print(PrintStream paramPrintStream)
/*      */   {
/* 2032 */     if (isPublic()) {
/* 2033 */       paramPrintStream.print("public ");
/*      */     }
/* 2035 */     if (isInterface())
/* 2036 */       paramPrintStream.print("interface ");
/*      */     else {
/* 2038 */       paramPrintStream.print("class ");
/*      */     }
/* 2040 */     paramPrintStream.print(getName() + " ");
/* 2041 */     if (getSuperClass() != null) {
/* 2042 */       paramPrintStream.print("extends " + getSuperClass().getName() + " ");
/*      */     }
/* 2044 */     if (this.interfaces.length > 0) {
/* 2045 */       paramPrintStream.print("implements ");
/* 2046 */       for (int i = 0; i < this.interfaces.length; i++) {
/* 2047 */         if (i > 0) {
/* 2048 */           paramPrintStream.print(", ");
/*      */         }
/* 2050 */         paramPrintStream.print(this.interfaces[i].getName());
/* 2051 */         paramPrintStream.print(" ");
/*      */       }
/*      */     }
/* 2054 */     paramPrintStream.println("{");
/*      */ 
/* 2056 */     for (MemberDefinition localMemberDefinition = getFirstMember(); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 2057 */       paramPrintStream.print("    ");
/* 2058 */       localMemberDefinition.print(paramPrintStream);
/*      */     }
/*      */ 
/* 2061 */     paramPrintStream.println("}");
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2068 */     return getClassDeclaration().toString();
/*      */   }
/*      */ 
/*      */   public void cleanup(Environment paramEnvironment)
/*      */   {
/* 2076 */     if (paramEnvironment.dump()) {
/* 2077 */       paramEnvironment.output("[cleanup " + getName() + "]");
/*      */     }
/* 2079 */     for (MemberDefinition localMemberDefinition = getFirstMember(); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 2080 */       localMemberDefinition.cleanup(paramEnvironment);
/*      */     }
/*      */ 
/* 2083 */     this.documentation = null;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ClassDefinition
 * JD-Core Version:    0.6.2
 */