/*   */ package com.sun.tools.doclint.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class doclint_zh_CN extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 5 */     return new Object[][] { { "dc.anchor.already.defined", "锚定点已定义: {0}" }, { "dc.anchor.value.missing", "没有为锚定点指定值" }, { "dc.attr.lacks.value", "属性缺少值" }, { "dc.attr.not.number", "属性值不是数字" }, { "dc.attr.obsolete", "属性已过时: {0}" }, { "dc.attr.obsolete.use.css", "属性已过时, 请改用 CSS: {0}" }, { "dc.attr.repeated", "属性重复: {0}" }, { "dc.attr.unknown", "未知属性: {0}" }, { "dc.bad.option", "选项错误: {0}" }, { "dc.bad.value.for.option", "选项的值错误: {0} {1}" }, { "dc.empty", "@{0} 没有说明" }, { "dc.entity.invalid", "实体 &{0}; 无效" }, { "dc.exception.not.thrown", "未抛出异常错误: {0}" }, { "dc.invalid.anchor", "锚定点的名称无效: \"{0}\"" }, { "dc.invalid.param", "@param 的用法无效" }, { "dc.invalid.return", "@return 的用法无效" }, { "dc.invalid.throws", "@throws 的用法无效" }, { "dc.invalid.uri", "URI 无效: \"{0}\"" }, { "dc.main.ioerror", "IO 错误: {0}" }, { "dc.main.no.files.given", "未指定文件" }, { "dc.main.usage", "用法:\n    doclint [options] source-files...\n\n选项:\n  -Xmsgs  \n    与 -Xmsgs:all 相同\n  -Xmsgs:values\n    指定要检查的问题的类别, 其中 ''values''\n    是任意以下内容的以逗号分隔的列表:\n      reference      显示包含对 Java 源代码元素\n                     错误引用的注释的位置\n      syntax         显示注释中的基本语法错误\n      html           显示 HTML 标记和属性问题\n      accessibility  显示可访问性的问题\n      missing        显示缺少文档的问题\n      all            所有以上内容\n    在值之前使用 ''-'' 可使用其反值\n    可以使用以下一项来限定类别:\n      /public /protected /package /private\n    对于正类别 (不以 ''-'' 开头)\n    限定符适用于该访问级别及更高级别。\n    对于负类别 (以 ''-'' 开头)\n    限定符适用于该访问级别及更低级别。\n    如果没有限定符, 则该类别适用于\n    所有访问级别。\n    例如, -Xmsgs:all,-syntax/private\n    这将在专用方法的文档注释中\n    启用除语法错误之外的所有消息。\n    如果未提供 -Xmsgs 选项, 则默认值\n    等同于 -Xmsgs:all/protected, 表示\n    仅报告受保护和公共声明中的\n    所有消息\n  -stats\n    报告所报告问题的统计信息。\n  -h -help --help -usage -?\n    显示此消息。\n\n还支持以下 javac 选项\n  -bootclasspath, -classpath, -cp, -sourcepath, -Xmaxerrs, -Xmaxwarns\n\n要在项目的一部分上运行 doclint, 请将项目中已编译的类\n放在类路径 (或引导类路径) 上, 然后在命令行上指定\n要检查的源文件。" }, { "dc.missing.comment", "没有注释" }, { "dc.missing.param", "{0}没有 @param" }, { "dc.missing.return", "没有 @return" }, { "dc.missing.throws", "{0}没有 @throws" }, { "dc.no.alt.attr.for.image", "图像没有 \"alt\" 属性" }, { "dc.no.summary.or.caption.for.table", "表没有概要或标题" }, { "dc.param.name.not.found", "@param name 未找到" }, { "dc.ref.not.found", "找不到引用" }, { "dc.tag.code.within.code", "'{@code'} 在 <code> 中" }, { "dc.tag.empty", "<{0}> 标记为空" }, { "dc.tag.end.not.permitted", "无效的结束标记: </{0}>" }, { "dc.tag.end.unexpected", "意外的结束标记: </{0}>" }, { "dc.tag.header.sequence.1", "使用的标题超出序列: <{0}>" }, { "dc.tag.header.sequence.2", "使用的标题超出序列: <{0}>" }, { "dc.tag.nested.not.allowed", "不允许使用嵌套标记: <{0}>" }, { "dc.tag.not.allowed", "文档注释中不允许使用元素: <{0}>" }, { "dc.tag.not.allowed.here", "此处不允许使用标记: <{0}>" }, { "dc.tag.not.allowed.inline.element", "内嵌元素 <{1}> 中不允许使用块元素: {0}" }, { "dc.tag.not.allowed.inline.other", "此处不允许使用块元素: {0}" }, { "dc.tag.not.allowed.inline.tag", "@{1} 中不允许使用块元素: {0}" }, { "dc.tag.not.closed", "元素未关闭: {0}" }, { "dc.tag.p.in.pre", "<pre> 元素内部意外地使用了 <p>" }, { "dc.tag.self.closing", "不允许使用自关闭元素" }, { "dc.tag.start.unmatched", "缺少结束标记: </{0}>" }, { "dc.tag.unknown", "未知标记: {0}" }, { "dc.text.not.allowed", "<{0}> 元素中不允许使用文本" }, { "dc.type.arg.not.allowed", "此处不允许使用类型参数" }, { "dc.unexpected.comment", "此处未预期文档注释" }, { "dc.value.not.a.constant", "值不引用常量" }, { "dc.value.not.allowed.here", "此处不允许使用 '{@value}'" } };
/*   */   }
/*   */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclint.resources.doclint_zh_CN
 * JD-Core Version:    0.6.2
 */