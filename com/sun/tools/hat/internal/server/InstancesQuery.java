/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaClass;
/*    */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ class InstancesQuery extends QueryHandler
/*    */ {
/*    */   private boolean includeSubclasses;
/*    */   private boolean newObjects;
/*    */ 
/*    */   public InstancesQuery(boolean paramBoolean)
/*    */   {
/* 50 */     this.includeSubclasses = paramBoolean;
/*    */   }
/*    */ 
/*    */   public InstancesQuery(boolean paramBoolean1, boolean paramBoolean2) {
/* 54 */     this.includeSubclasses = paramBoolean1;
/* 55 */     this.newObjects = paramBoolean2;
/*    */   }
/*    */ 
/*    */   public void run() {
/* 59 */     JavaClass localJavaClass = this.snapshot.findClass(this.query);
/*    */     String str;
/* 61 */     if (this.newObjects)
/* 62 */       str = "New instances of ";
/*    */     else
/* 64 */       str = "Instances of ";
/* 65 */     if (this.includeSubclasses)
/* 66 */       startHtml(str + this.query + " (including subclasses)");
/*    */     else {
/* 68 */       startHtml(str + this.query);
/*    */     }
/* 70 */     if (localJavaClass == null) {
/* 71 */       error("Class not found");
/*    */     } else {
/* 73 */       this.out.print("<strong>");
/* 74 */       printClass(localJavaClass);
/* 75 */       this.out.print("</strong><br><br>");
/* 76 */       Enumeration localEnumeration = localJavaClass.getInstances(this.includeSubclasses);
/* 77 */       long l1 = 0L;
/* 78 */       long l2 = 0L;
/* 79 */       while (localEnumeration.hasMoreElements()) {
/* 80 */         JavaHeapObject localJavaHeapObject = (JavaHeapObject)localEnumeration.nextElement();
/* 81 */         if ((!this.newObjects) || (localJavaHeapObject.isNew()))
/*    */         {
/* 83 */           printThing(localJavaHeapObject);
/* 84 */           this.out.println("<br>");
/* 85 */           l1 += localJavaHeapObject.getSize();
/* 86 */           l2 += 1L;
/*    */         }
/*    */       }
/* 88 */       this.out.println("<h2>Total of " + l2 + " instances occupying " + l1 + " bytes.</h2>");
/*    */     }
/* 90 */     endHtml();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.InstancesQuery
 * JD-Core Version:    0.6.2
 */