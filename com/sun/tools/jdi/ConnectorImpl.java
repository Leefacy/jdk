/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.Connector.Argument;
/*     */ import com.sun.jdi.connect.Connector.BooleanArgument;
/*     */ import com.sun.jdi.connect.Connector.IntegerArgument;
/*     */ import com.sun.jdi.connect.Connector.SelectedArgument;
/*     */ import com.sun.jdi.connect.Connector.StringArgument;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ abstract class ConnectorImpl
/*     */   implements Connector
/*     */ {
/*  42 */   Map<String, Connector.Argument> defaultArguments = new LinkedHashMap();
/*     */ 
/*  45 */   static String trueString = null;
/*     */   static String falseString;
/* 122 */   private ResourceBundle messages = null;
/*     */ 
/*     */   public Map<String, Connector.Argument> defaultArguments()
/*     */   {
/*  49 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*  50 */     Collection localCollection = this.defaultArguments.values();
/*     */ 
/*  52 */     Iterator localIterator = localCollection.iterator();
/*  53 */     while (localIterator.hasNext()) {
/*  54 */       ArgumentImpl localArgumentImpl = (ArgumentImpl)localIterator.next();
/*  55 */       localLinkedHashMap.put(localArgumentImpl.name(), (Connector.Argument)localArgumentImpl.clone());
/*     */     }
/*  57 */     return localLinkedHashMap;
/*     */   }
/*     */ 
/*     */   void addStringArgument(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
/*     */   {
/*  62 */     this.defaultArguments.put(paramString1, new StringArgumentImpl(paramString1, paramString2, paramString3, paramString4, paramBoolean));
/*     */   }
/*     */ 
/*     */   void addBooleanArgument(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  71 */     this.defaultArguments.put(paramString1, new BooleanArgumentImpl(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */   void addIntegerArgument(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, int paramInt1, int paramInt2)
/*     */   {
/*  81 */     this.defaultArguments.put(paramString1, new IntegerArgumentImpl(paramString1, paramString2, paramString3, paramString4, paramBoolean, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   void addSelectedArgument(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, List<String> paramList)
/*     */   {
/*  92 */     this.defaultArguments.put(paramString1, new SelectedArgumentImpl(paramString1, paramString2, paramString3, paramString4, paramBoolean, paramList));
/*     */   }
/*     */ 
/*     */   ArgumentImpl argument(String paramString, Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IllegalConnectorArgumentsException
/*     */   {
/* 102 */     ArgumentImpl localArgumentImpl = (ArgumentImpl)paramMap.get(paramString);
/* 103 */     if (localArgumentImpl == null) {
/* 104 */       throw new IllegalConnectorArgumentsException("Argument missing", paramString);
/*     */     }
/*     */ 
/* 107 */     String str = localArgumentImpl.value();
/* 108 */     if ((str == null) || (str.length() == 0)) {
/* 109 */       if (localArgumentImpl.mustSpecify()) {
/* 110 */         throw new IllegalConnectorArgumentsException("Argument unspecified", paramString);
/*     */       }
/*     */     }
/* 113 */     else if (!localArgumentImpl.isValid(str)) {
/* 114 */       throw new IllegalConnectorArgumentsException("Argument invalid", paramString);
/*     */     }
/*     */ 
/* 118 */     return localArgumentImpl;
/*     */   }
/*     */ 
/*     */   String getString(String paramString)
/*     */   {
/* 125 */     if (this.messages == null) {
/* 126 */       this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */     }
/* 128 */     return this.messages.getString(paramString);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 132 */     String str = name() + " (defaults: ";
/* 133 */     Iterator localIterator = defaultArguments().values().iterator();
/* 134 */     int i = 1;
/* 135 */     while (localIterator.hasNext()) {
/* 136 */       ArgumentImpl localArgumentImpl = (ArgumentImpl)localIterator.next();
/* 137 */       if (i == 0) {
/* 138 */         str = str + ", ";
/*     */       }
/* 140 */       str = str + localArgumentImpl.toString();
/* 141 */       i = 0;
/*     */     }
/* 143 */     str = str + ")";
/* 144 */     return str;
/*     */   }
/*     */   abstract class ArgumentImpl implements Connector.Argument, Cloneable, Serializable {
/*     */     private String name;
/*     */     private String label;
/*     */     private String description;
/*     */     private String value;
/*     */     private boolean mustSpecify;
/*     */ 
/* 157 */     ArgumentImpl(String paramString1, String paramString2, String paramString3, String paramBoolean, boolean arg6) { this.name = paramString1;
/* 158 */       this.label = paramString2;
/* 159 */       this.description = paramString3;
/* 160 */       this.value = paramBoolean;
/*     */       boolean bool;
/* 161 */       this.mustSpecify = bool; }
/*     */ 
/*     */     public abstract boolean isValid(String paramString);
/*     */ 
/*     */     public String name()
/*     */     {
/* 167 */       return this.name;
/*     */     }
/*     */ 
/*     */     public String label() {
/* 171 */       return this.label;
/*     */     }
/*     */ 
/*     */     public String description() {
/* 175 */       return this.description;
/*     */     }
/*     */ 
/*     */     public String value() {
/* 179 */       return this.value;
/*     */     }
/*     */ 
/*     */     public void setValue(String paramString) {
/* 183 */       if (paramString == null) {
/* 184 */         throw new NullPointerException("Can't set null value");
/*     */       }
/* 186 */       this.value = paramString;
/*     */     }
/*     */ 
/*     */     public boolean mustSpecify() {
/* 190 */       return this.mustSpecify;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 194 */       if ((paramObject != null) && ((paramObject instanceof Serializable))) {
/* 195 */         Connector.Argument localArgument = (Serializable)paramObject;
/*     */ 
/* 199 */         return (name().equals(localArgument.name())) && 
/* 197 */           (description().equals(localArgument.description())) && 
/* 198 */           (mustSpecify() == localArgument.mustSpecify()) && 
/* 199 */           (value().equals(localArgument.value()));
/*     */       }
/* 201 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 206 */       return description().hashCode();
/*     */     }
/*     */ 
/*     */     public Object clone() {
/*     */       try {
/* 211 */         return super.clone();
/*     */       } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */       }
/* 214 */       throw new InternalException();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 219 */       return name() + "=" + value();
/*     */     }
/*     */   }
/*     */ 
/*     */   class BooleanArgumentImpl extends ConnectorImpl.ArgumentImpl implements Connector.BooleanArgument
/*     */   {
/*     */     private static final long serialVersionUID = 1624542968639361316L;
/*     */ 
/*     */     BooleanArgumentImpl(String paramString1, String paramString2, String paramBoolean1, boolean paramBoolean2, boolean arg6)
/*     */     {
/* 229 */       super(paramString1, paramString2, paramBoolean1, null, bool);
/* 230 */       if (ConnectorImpl.trueString == null) {
/* 231 */         ConnectorImpl.trueString = ConnectorImpl.this.getString("true");
/* 232 */         ConnectorImpl.falseString = ConnectorImpl.this.getString("false");
/*     */       }
/* 234 */       setValue(paramBoolean2);
/*     */     }
/*     */ 
/*     */     public void setValue(boolean paramBoolean)
/*     */     {
/* 241 */       setValue(stringValueOf(paramBoolean));
/*     */     }
/*     */ 
/*     */     public boolean isValid(String paramString)
/*     */     {
/* 251 */       return (paramString.equals(ConnectorImpl.trueString)) || (paramString.equals(ConnectorImpl.falseString));
/*     */     }
/*     */ 
/*     */     public String stringValueOf(boolean paramBoolean)
/*     */     {
/* 262 */       return paramBoolean ? ConnectorImpl.trueString : ConnectorImpl.falseString;
/*     */     }
/*     */ 
/*     */     public boolean booleanValue()
/*     */     {
/* 274 */       return value().equals(ConnectorImpl.trueString);
/*     */     }
/*     */   }
/*     */ 
/*     */   class IntegerArgumentImpl extends ConnectorImpl.ArgumentImpl implements Connector.IntegerArgument
/*     */   {
/*     */     private static final long serialVersionUID = 763286081923797770L;
/*     */     private final int min;
/*     */     private final int max;
/*     */ 
/*     */     IntegerArgumentImpl(String paramString1, String paramString2, String paramString3, String paramBoolean, boolean paramInt1, int paramInt2, int arg8) {
/* 287 */       super(paramString1, paramString2, paramString3, paramBoolean, paramInt1);
/* 288 */       this.min = paramInt2;
/*     */       int i;
/* 289 */       this.max = i;
/*     */     }
/*     */ 
/*     */     public void setValue(int paramInt)
/*     */     {
/* 300 */       setValue(stringValueOf(paramInt));
/*     */     }
/*     */ 
/*     */     public boolean isValid(String paramString)
/*     */     {
/* 309 */       if (paramString == null)
/* 310 */         return false;
/*     */       try
/*     */       {
/* 313 */         return isValid(Integer.decode(paramString).intValue()); } catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 315 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean isValid(int paramInt)
/*     */     {
/* 325 */       return (this.min <= paramInt) && (paramInt <= this.max);
/*     */     }
/*     */ 
/*     */     public String stringValueOf(int paramInt)
/*     */     {
/* 340 */       return "" + paramInt;
/*     */     }
/*     */ 
/*     */     public int intValue()
/*     */     {
/* 352 */       if (value() == null)
/* 353 */         return 0;
/*     */       try
/*     */       {
/* 356 */         return Integer.decode(value()).intValue(); } catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 358 */       return 0;
/*     */     }
/*     */ 
/*     */     public int max()
/*     */     {
/* 367 */       return this.max;
/*     */     }
/*     */ 
/*     */     public int min()
/*     */     {
/* 375 */       return this.min;
/*     */     }
/*     */   }
/*     */ 
/*     */   class SelectedArgumentImpl extends ConnectorImpl.ArgumentImpl
/*     */     implements Connector.SelectedArgument
/*     */   {
/*     */     private static final long serialVersionUID = -5689584530908382517L;
/*     */     private final List<String> choices;
/*     */ 
/*     */     SelectedArgumentImpl(String paramString1, String paramString2, String paramBoolean, boolean paramList, List<String> arg6)
/*     */     {
/* 405 */       super(paramString1, paramString2, paramBoolean, paramList, bool);
/*     */       Collection localCollection;
/* 406 */       this.choices = Collections.unmodifiableList(new ArrayList(localCollection));
/*     */     }
/*     */ 
/*     */     public List<String> choices()
/*     */     {
/* 414 */       return this.choices;
/*     */     }
/*     */ 
/*     */     public boolean isValid(String paramString)
/*     */     {
/* 422 */       return this.choices.contains(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   class StringArgumentImpl extends ConnectorImpl.ArgumentImpl
/*     */     implements Connector.StringArgument
/*     */   {
/*     */     private static final long serialVersionUID = 7500484902692107464L;
/*     */ 
/*     */     StringArgumentImpl(String paramString1, String paramString2, String paramString3, String paramBoolean, boolean arg6)
/*     */     {
/* 385 */       super(paramString1, paramString2, paramString3, paramBoolean, bool);
/*     */     }
/*     */ 
/*     */     public boolean isValid(String paramString)
/*     */     {
/* 393 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ConnectorImpl
 * JD-Core Version:    0.6.2
 */