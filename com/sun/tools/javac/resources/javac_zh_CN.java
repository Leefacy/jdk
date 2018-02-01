/*   */ package com.sun.tools.javac.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class javac_zh_CN extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 5 */     return new Object[][] { { "javac.err.dir.not.found", "找不到目录: {0}" }, { "javac.err.empty.A.argument", "-A 需要一个参数; 使用 ''-Akey'' 或 ''-Akey=value''" }, { "javac.err.error.writing.file", "写入{0}时出错; {1}" }, { "javac.err.file.not.directory", "不是目录: {0}" }, { "javac.err.file.not.file", "不是文件: {0}" }, { "javac.err.file.not.found", "找不到文件: {0}" }, { "javac.err.invalid.A.key", "注释处理程序选项 ''{0}'' 中的关键字不是以点分隔的标识符序列" }, { "javac.err.invalid.arg", "无效的参数: {0}" }, { "javac.err.invalid.flag", "无效的标记: {0}" }, { "javac.err.invalid.profile", "配置文件无效: {0}" }, { "javac.err.invalid.source", "无效的源发行版: {0}" }, { "javac.err.invalid.target", "无效的目标发行版: {0}" }, { "javac.err.no.source.files", "无源文件" }, { "javac.err.no.source.files.classes", "无源文件或类名" }, { "javac.err.profile.bootclasspath.conflict", "概要信息和引导类路径选项不能同时使用" }, { "javac.err.req.arg", "{0}需要参数" }, { "javac.fullVersion", "{0}完整版本 \"{1}\"" }, { "javac.msg.bug", "编译器 ({0}) 中出现异常错误。如果在 Bug Database (http://bugs.java.com) 中没有找到该错误, 请通过 Java Bug 报告页 (http://bugreport.java.com) 建立该 Java 编译器 Bug。请在报告中附上您的程序和以下诊断信息。谢谢。" }, { "javac.msg.io", "\n\n发生输入/输出错误。\n有关详细信息, 请参阅以下堆栈跟踪。\n" }, { "javac.msg.plugin.not.found", "找不到插件: {0}" }, { "javac.msg.plugin.uncaught.exception", "\n\n插件抛出未捕获的异常错误。\n有关详细信息, 请参阅以下堆栈跟踪。\n" }, { "javac.msg.proc.annotation.uncaught.exception", "\n\n注释处理程序抛出未捕获的异常错误。\n有关详细信息, 请参阅以下堆栈跟踪。\n" }, { "javac.msg.resource", "\n\n系统资源不足。\n有关详细信息, 请参阅以下堆栈跟踪。\n" }, { "javac.msg.usage", "用法: {0} <options> <source files>\n-help 用于列出可能的选项" }, { "javac.msg.usage.header", "用法: {0} <options> <source files>\n其中, 可能的选项包括:" }, { "javac.msg.usage.nonstandard.footer", "这些选项都是非标准选项, 如有更改, 恕不另行通知。" }, { "javac.opt.A", "传递给注释处理程序的选项" }, { "javac.opt.AT", "从文件读取选项和文件名" }, { "javac.opt.J", "直接将 <标记> 传递给运行时系统" }, { "javac.opt.Werror", "出现警告时终止编译" }, { "javac.opt.X", "输出非标准选项的提要" }, { "javac.opt.Xbootclasspath.a", "置于引导类路径之后" }, { "javac.opt.Xbootclasspath.p", "置于引导类路径之前" }, { "javac.opt.Xdoclint", "为 javadoc 注释中的问题启用建议的检查" }, { "javac.opt.Xdoclint.custom", "\n        为 javadoc 注释中的问题启用或禁用特定检查,\n        其中 <group> 为 accessibility, html, missing, reference 或 syntax 之一。\n        <access> 为 public, protected, package 或 private 之一。" }, { "javac.opt.Xdoclint.subopts", "(all|none|[-]<group>)[/<access>]" }, { "javac.opt.Xlint", "启用建议的警告" }, { "javac.opt.Xlint.suboptlist", "启用或禁用特定的警告" }, { "javac.opt.Xstdout", "重定向标准输出" }, { "javac.opt.arg.class", "<类>" }, { "javac.opt.arg.class.list", "<class1>[,<class2>,<class3>...]" }, { "javac.opt.arg.directory", "<目录>" }, { "javac.opt.arg.dirs", "<目录>" }, { "javac.opt.arg.encoding", "<编码>" }, { "javac.opt.arg.file", "<文件名>" }, { "javac.opt.arg.flag", "<标记>" }, { "javac.opt.arg.key.equals.value", "关键字[=值]" }, { "javac.opt.arg.number", "<编号>" }, { "javac.opt.arg.path", "<路径>" }, { "javac.opt.arg.pathname", "<路径名>" }, { "javac.opt.arg.plugin", "\"名称参数\"" }, { "javac.opt.arg.profile", "<配置文件>" }, { "javac.opt.arg.release", "<发行版>" }, { "javac.opt.bootclasspath", "覆盖引导类文件的位置" }, { "javac.opt.classpath", "指定查找用户类文件和注释处理程序的位置" }, { "javac.opt.d", "指定放置生成的类文件的位置" }, { "javac.opt.deprecation", "输出使用已过时的 API 的源位置" }, { "javac.opt.diags", "选择诊断模式" }, { "javac.opt.encoding", "指定源文件使用的字符编码" }, { "javac.opt.endorseddirs", "覆盖签名的标准路径的位置" }, { "javac.opt.extdirs", "覆盖所安装扩展的位置" }, { "javac.opt.g", "生成所有调试信息" }, { "javac.opt.g.lines.vars.source", "只生成某些调试信息" }, { "javac.opt.g.none", "不生成任何调试信息" }, { "javac.opt.headerDest", "指定放置生成的本机标头文件的位置" }, { "javac.opt.help", "输出标准选项的提要" }, { "javac.opt.implicit", "指定是否为隐式引用文件生成类文件" }, { "javac.opt.maxerrs", "设置要输出的错误的最大数目" }, { "javac.opt.maxwarns", "设置要输出的警告的最大数目" }, { "javac.opt.moreinfo", "输出类型变量的扩展信息" }, { "javac.opt.nogj", "语言中不接受泛型" }, { "javac.opt.nowarn", "不生成任何警告" }, { "javac.opt.parameters", "生成元数据以用于方法参数的反射" }, { "javac.opt.pkginfo", "指定 package-info 文件的处理" }, { "javac.opt.plugin", "要运行的插件的名称和可选参数" }, { "javac.opt.prefer", "指定读取文件, 当同时找到隐式编译类的源文件和类文件时" }, { "javac.opt.print", "输出指定类型的文本表示" }, { "javac.opt.printProcessorInfo", "输出有关请求处理程序处理哪些注释的信息" }, { "javac.opt.printRounds", "输出有关注释处理循环的信息" }, { "javac.opt.printflat", "在内部类转换之后输出抽象语法树" }, { "javac.opt.printsearch", "输出有关搜索类文件的位置的信息" }, { "javac.opt.proc.none.only", "控制是否执行注释处理和/或编译。" }, { "javac.opt.processor", "要运行的注释处理程序的名称; 绕过默认的搜索进程" }, { "javac.opt.processorpath", "指定查找注释处理程序的位置" }, { "javac.opt.profile", "请确保使用的 API 在指定的配置文件中可用" }, { "javac.opt.prompt", "在每次出错后停止" }, { "javac.opt.retrofit", "更新使用泛型的现有类文件" }, { "javac.opt.s", "发出 java 源而不是类文件" }, { "javac.opt.scramble", "在字节码中混淆专用标识符" }, { "javac.opt.scrambleall", "在字节码中混淆程序包可见标识符" }, { "javac.opt.source", "提供与指定发行版的源兼容性" }, { "javac.opt.sourceDest", "指定放置生成的源文件的位置" }, { "javac.opt.sourcepath", "指定查找输入源文件的位置" }, { "javac.opt.target", "生成特定 VM 版本的类文件" }, { "javac.opt.verbose", "输出有关编译器正在执行的操作的消息" }, { "javac.opt.version", "版本信息" }, { "javac.version", "{0} {1}" }, { "javac.warn.profile.target.conflict", "配置文件{0}对于目标发行版 {1} 无效" }, { "javac.warn.source.target.conflict", "源发行版 {0} 需要目标发行版 {1}" }, { "javac.warn.target.default.source.conflict", "目标发行版 {0} 与默认的源发行版 {1} 冲突" } };
/*   */   }
/*   */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.resources.javac_zh_CN
 * JD-Core Version:    0.6.2
 */