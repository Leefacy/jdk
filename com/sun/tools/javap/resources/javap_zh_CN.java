/*   */ package com.sun.tools.javap.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class javap_zh_CN extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 5 */     return new Object[][] { { "err.bad.constant.pool", "读取{0}的常量池时出错: {1}" }, { "err.bad.innerclasses.attribute", "{0}的 InnerClasses 属性错误" }, { "err.class.not.found", "找不到类: {0}" }, { "err.crash", "出现严重的内部错误: {0}\n请建立 Bug 报告, 并包括以下信息:\n{1}" }, { "err.end.of.file", "读取{0}时出现意外的文件结尾" }, { "err.file.not.found", "找不到文件: {0}" }, { "err.incompatible.options", "选项组合错误: {0}" }, { "err.internal.error", "内部错误: {0} {1} {2}" }, { "err.invalid.arg.for.option", "选项的参数无效: {0}" }, { "err.invalid.use.of.option", "选项的使用无效: {0}" }, { "err.ioerror", "读取{0}时出现 IO 错误: {1}" }, { "err.missing.arg", "没有为{0}指定值" }, { "err.no.SourceFile.attribute", "没有 SourceFile 属性" }, { "err.no.classes.specified", "未指定类" }, { "err.not.standard.file.manager", "使用标准文件管理器时只能指定类文件" }, { "err.prefix", "错误:" }, { "err.source.file.not.found", "找不到源文件" }, { "err.unknown.option", "未知选项: {0}" }, { "main.opt.bootclasspath", "  -bootclasspath <path>    覆盖引导类文件的位置" }, { "main.opt.c", "  -c                       对代码进行反汇编" }, { "main.opt.classpath", "  -classpath <path>        指定查找用户类文件的位置" }, { "main.opt.constants", "  -constants               显示最终常量" }, { "main.opt.cp", "  -cp <path>               指定查找用户类文件的位置" }, { "main.opt.help", "  -help  --help  -?        输出此用法消息" }, { "main.opt.l", "  -l                       输出行号和本地变量表" }, { "main.opt.p", "  -p  -private             显示所有类和成员" }, { "main.opt.package", "  -package                 显示程序包/受保护的/公共类\n                           和成员 (默认)" }, { "main.opt.protected", "  -protected               显示受保护的/公共类和成员" }, { "main.opt.public", "  -public                  仅显示公共类和成员" }, { "main.opt.s", "  -s                       输出内部类型签名" }, { "main.opt.sysinfo", "  -sysinfo                 显示正在处理的类的\n                           系统信息 (路径, 大小, 日期, MD5 散列)" }, { "main.opt.v", "  -v  -verbose             输出附加信息" }, { "main.opt.version", "  -version                 版本信息" }, { "main.usage", "用法: {0} <options> <classes>\n其中, 可能的选项包括:" }, { "main.usage.summary", "用法: {0} <options> <classes>\n使用 -help 列出可能的选项" }, { "note.prefix", "注:" }, { "warn.prefix", "警告:" }, { "warn.unexpected.class", "二进制文件{0}包含{1}" } };
/*   */   }
/*   */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.resources.javap_zh_CN
 * JD-Core Version:    0.6.2
 */