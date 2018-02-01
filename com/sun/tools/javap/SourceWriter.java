/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Attribute;
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.LineNumberTable_attribute;
/*     */ import com.sun.tools.classfile.LineNumberTable_attribute.Entry;
/*     */ import com.sun.tools.classfile.SourceFile_attribute;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class SourceWriter extends InstructionDetailWriter
/*     */ {
/*     */   private JavaFileManager fileManager;
/*     */   private ClassFile classFile;
/*     */   private SortedMap<Integer, SortedSet<Integer>> lineMap;
/*     */   private List<Integer> lineList;
/*     */   private String[] sourceLines;
/*     */ 
/*     */   static SourceWriter instance(Context paramContext)
/*     */   {
/*  62 */     SourceWriter localSourceWriter = (SourceWriter)paramContext.get(SourceWriter.class);
/*  63 */     if (localSourceWriter == null)
/*  64 */       localSourceWriter = new SourceWriter(paramContext);
/*  65 */     return localSourceWriter;
/*     */   }
/*     */ 
/*     */   protected SourceWriter(Context paramContext) {
/*  69 */     super(paramContext);
/*  70 */     paramContext.put(SourceWriter.class, this);
/*     */   }
/*     */ 
/*     */   void setFileManager(JavaFileManager paramJavaFileManager) {
/*  74 */     this.fileManager = paramJavaFileManager;
/*     */   }
/*     */ 
/*     */   public void reset(ClassFile paramClassFile, Code_attribute paramCode_attribute) {
/*  78 */     setSource(paramClassFile);
/*  79 */     setLineMap(paramCode_attribute);
/*     */   }
/*     */ 
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  83 */     String str = space(40);
/*  84 */     Set localSet = (Set)this.lineMap.get(Integer.valueOf(paramInstruction.getPC()));
/*     */     Iterator localIterator;
/*  85 */     if (localSet != null)
/*  86 */       for (localIterator = localSet.iterator(); localIterator.hasNext(); ) { int i = ((Integer)localIterator.next()).intValue();
/*  87 */         print(str);
/*  88 */         print(String.format(" %4d ", new Object[] { Integer.valueOf(i) }));
/*  89 */         if (i < this.sourceLines.length)
/*  90 */           print(this.sourceLines[i]);
/*  91 */         println();
/*  92 */         int j = nextLine(i);
/*  93 */         for (int k = i + 1; k < j; k++) {
/*  94 */           print(str);
/*  95 */           print(String.format("(%4d)", new Object[] { Integer.valueOf(k) }));
/*  96 */           if (k < this.sourceLines.length)
/*  97 */             print(this.sourceLines[k]);
/*  98 */           println();
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean hasSource()
/*     */   {
/* 105 */     return this.sourceLines.length > 0;
/*     */   }
/*     */ 
/*     */   private void setLineMap(Code_attribute paramCode_attribute) {
/* 109 */     TreeMap localTreeMap = new TreeMap();
/*     */ 
/* 111 */     TreeSet localTreeSet = new TreeSet();
/* 112 */     for (Attribute localAttribute : paramCode_attribute.attributes) {
/* 113 */       if ((localAttribute instanceof LineNumberTable_attribute)) {
/* 114 */         LineNumberTable_attribute localLineNumberTable_attribute = (LineNumberTable_attribute)localAttribute;
/* 115 */         for (LineNumberTable_attribute.Entry localEntry : localLineNumberTable_attribute.line_number_table) {
/* 116 */           int k = localEntry.start_pc;
/* 117 */           int m = localEntry.line_number;
/* 118 */           Object localObject = (SortedSet)localTreeMap.get(Integer.valueOf(k));
/* 119 */           if (localObject == null) {
/* 120 */             localObject = new TreeSet();
/* 121 */             localTreeMap.put(Integer.valueOf(k), localObject);
/*     */           }
/* 123 */           ((SortedSet)localObject).add(Integer.valueOf(m));
/* 124 */           localTreeSet.add(Integer.valueOf(m));
/*     */         }
/*     */       }
/*     */     }
/* 128 */     this.lineMap = localTreeMap;
/* 129 */     this.lineList = new ArrayList(localTreeSet);
/*     */   }
/*     */ 
/*     */   private void setSource(ClassFile paramClassFile) {
/* 133 */     if (paramClassFile != this.classFile) {
/* 134 */       this.classFile = paramClassFile;
/* 135 */       this.sourceLines = splitLines(readSource(paramClassFile));
/*     */     }
/*     */   }
/*     */ 
/*     */   private String readSource(ClassFile paramClassFile) {
/* 140 */     if (this.fileManager == null)
/* 141 */       return null;
/*     */     StandardLocation localStandardLocation;
/* 144 */     if (this.fileManager.hasLocation(StandardLocation.SOURCE_PATH))
/* 145 */       localStandardLocation = StandardLocation.SOURCE_PATH;
/*     */     else {
/* 147 */       localStandardLocation = StandardLocation.CLASS_PATH;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 154 */       String str1 = paramClassFile.getName();
/*     */ 
/* 156 */       SourceFile_attribute localSourceFile_attribute = (SourceFile_attribute)paramClassFile.attributes
/* 156 */         .get("SourceFile");
/*     */ 
/* 157 */       if (localSourceFile_attribute == null) {
/* 158 */         report(this.messages.getMessage("err.no.SourceFile.attribute", new Object[0]));
/* 159 */         return null;
/*     */       }
/* 161 */       String str2 = localSourceFile_attribute.getSourceFile(paramClassFile.constant_pool);
/*     */ 
/* 163 */       String str3 = str2.endsWith(".java") ? str2
/* 163 */         .substring(0, str2
/* 163 */         .length() - 5) : str2;
/* 164 */       int i = str1.lastIndexOf("/");
/* 165 */       String str4 = i == -1 ? "" : str1.substring(0, i + 1);
/* 166 */       String str5 = (str4 + str3).replace('/', '.');
/*     */ 
/* 168 */       JavaFileObject localJavaFileObject = this.fileManager
/* 168 */         .getJavaFileForInput(localStandardLocation, str5, JavaFileObject.Kind.SOURCE);
/*     */ 
/* 171 */       if (localJavaFileObject == null) {
/* 172 */         report(this.messages.getMessage("err.source.file.not.found", new Object[0]));
/* 173 */         return null;
/*     */       }
/* 175 */       return localJavaFileObject.getCharContent(true).toString();
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 177 */       report(localConstantPoolException);
/* 178 */       return null;
/*     */     } catch (IOException localIOException) {
/* 180 */       report(localIOException.getLocalizedMessage());
/* 181 */     }return null;
/*     */   }
/*     */ 
/*     */   private static String[] splitLines(String paramString)
/*     */   {
/* 186 */     if (paramString == null) {
/* 187 */       return new String[0];
/*     */     }
/* 189 */     ArrayList localArrayList = new ArrayList();
/* 190 */     localArrayList.add("");
/*     */     try {
/* 192 */       BufferedReader localBufferedReader = new BufferedReader(new StringReader(paramString));
/*     */       String str;
/* 194 */       while ((str = localBufferedReader.readLine()) != null)
/* 195 */         localArrayList.add(str);
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 198 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private int nextLine(int paramInt) {
/* 202 */     int i = this.lineList.indexOf(Integer.valueOf(paramInt));
/* 203 */     if ((i == -1) || (i == this.lineList.size() - 1))
/* 204 */       return -1;
/* 205 */     return ((Integer)this.lineList.get(i + 1)).intValue();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.SourceWriter
 * JD-Core Version:    0.6.2
 */