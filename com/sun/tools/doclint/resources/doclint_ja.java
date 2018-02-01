/*   */ package com.sun.tools.doclint.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class doclint_ja extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 5 */     return new Object[][] { { "dc.anchor.already.defined", "アンカーがすでに定義されています: {0}" }, { "dc.anchor.value.missing", "アンカーに値が指定されていません" }, { "dc.attr.lacks.value", "属性に値がありません" }, { "dc.attr.not.number", "属性値が数字ではありません" }, { "dc.attr.obsolete", "属性は廃止されています: {0}" }, { "dc.attr.obsolete.use.css", "属性は廃止されています。かわりにCSSを使用してください: {0}" }, { "dc.attr.repeated", "繰り返された属性: {0}" }, { "dc.attr.unknown", "不明な属性: {0}" }, { "dc.bad.option", "無効なオプション: {0}" }, { "dc.bad.value.for.option", "オプションの値が不正です: {0} {1}" }, { "dc.empty", "@{0}の説明がありません" }, { "dc.entity.invalid", "無効なエンティティ&{0};" }, { "dc.exception.not.thrown", "例外がスローされていません: {0}" }, { "dc.invalid.anchor", "アンカーの名前が無効です: \"{0}\"" }, { "dc.invalid.param", "無効な@paramの使用" }, { "dc.invalid.return", "無効な@returnの使用" }, { "dc.invalid.throws", "無効な@throwsの使用" }, { "dc.invalid.uri", "無効なURI: \"{0}\"" }, { "dc.main.ioerror", "IOエラー: {0}" }, { "dc.main.no.files.given", "ファイルが指定されていません" }, { "dc.main.usage", "使用方法:\n    doclint [options] source-files...\n\nオプション:\n  -Xmsgs  \n    -Xmsgs:allと同じ\n  -Xmsgs:values\n    チェックする問題のカテゴリを指定します。ここでの''values''は、\n    カンマで区切られた次の値のリストです:\n      reference      Javaソース・コード要素への不正な参照を含むコメントの\n                     場所を表示します\n      syntax         コメント内の基本構文エラーを表示します\n      html           HTMLタブおよび属性の問題を表示します\n      accessibility  アクセシビリティの問題を表示します\n      missing        欠落しているドキュメントの問題を表示します\n      all            前述のすべて\n    これを否定するには、値の前に''-''を指定します\n    カテゴリは、次のいずれかで修飾できます:\n      /public /protected /package /private\n    正のカテゴリ(''-''で始まらない)の場合\n    修飾子は、そのアクセス・レベル以上に適用されます。\n    負のカテゴリ(''-''で始まる)の場合\n    修飾子は、そのアクセス・レベル以下に適用されます。\n    修飾子がない場合、カテゴリはすべてのアクセス・レベルに\n    適用されます。\n    例: -Xmsgs:all,-syntax/private\n    この場合、privateメソッドのdocコメント内の構文エラーを除き、\n    すべてのメッセージが有効化されます。\n    -Xmsgsオプションが指定されていない場合、デフォルトは、\n    -Xmsgs:all/protectedと同等になり、これは\n    すべてのメッセージが、protectedおよびpublicの宣言のみに報告されることを\n    意味します。\n  -stats\n    報告された問題に対して統計を報告します。\n  -h -help --help -usage -?\n    このメッセージが表示されます。\n\n次のjavacオプションもサポートされています\n  -bootclasspath、-classpath、-cp、-sourcepath、-Xmaxerrs、-Xmaxwarns\n\nプロジェクトの一部に対してdoclintを実行するには、プロジェクトのコンパイルされたクラスを\nクラスパス(またはブート・クラスパス)に指定し、コマンド行で\nチェックするソース・ファイルを指定します。" }, { "dc.missing.comment", "コメントなし" }, { "dc.missing.param", "{0}の@paramがありません" }, { "dc.missing.return", "@returnがありません" }, { "dc.missing.throws", "{0}の@throwsがありません" }, { "dc.no.alt.attr.for.image", "イメージの\"alt\"属性がありません" }, { "dc.no.summary.or.caption.for.table", "表の要約またはキャプションがありません" }, { "dc.param.name.not.found", "@param nameが見つかりません" }, { "dc.ref.not.found", "参照が見つかりません" }, { "dc.tag.code.within.code", "<code>内の'{@code'}" }, { "dc.tag.empty", "空の<{0}>タグ" }, { "dc.tag.end.not.permitted", "無効な終了タグ: </{0}>" }, { "dc.tag.end.unexpected", "予期しない終了タグ: </{0}>" }, { "dc.tag.header.sequence.1", "ヘッダーの指定順序が正しくありません: <{0}>" }, { "dc.tag.header.sequence.2", "ヘッダーの指定順序が正しくありません: <{0}>" }, { "dc.tag.nested.not.allowed", "ネストしたタグは使用できません: <{0}>" }, { "dc.tag.not.allowed", "ドキュメント・コメントで使用できない要素です: <{0}>" }, { "dc.tag.not.allowed.here", "ここではタグを使用できません: <{0}>" }, { "dc.tag.not.allowed.inline.element", "インライン要素<{1}>内で使用できないブロック要素です: {0}" }, { "dc.tag.not.allowed.inline.other", "ここではブロック要素を使用できません: {0}" }, { "dc.tag.not.allowed.inline.tag", "@{1}内で使用できないブロック要素です: {0}" }, { "dc.tag.not.closed", "要素が閉じられていません: {0}" }, { "dc.tag.p.in.pre", "<pre>要素内で予期しない<p>が使用されています" }, { "dc.tag.self.closing", "自己終了要素は使用できません" }, { "dc.tag.start.unmatched", "終了タグがありません: </{0}>" }, { "dc.tag.unknown", "不明なタグ: {0}" }, { "dc.text.not.allowed", "<{0}>要素ではテキストを使用できません" }, { "dc.type.arg.not.allowed", "型引数はここでは使用できません" }, { "dc.unexpected.comment", "ドキュメント・コメントはここでは必要ありません" }, { "dc.value.not.a.constant", "値が定数を参照していません" }, { "dc.value.not.allowed.here", "'{@value}'はここでは使用できません" } };
/*   */   }
/*   */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclint.resources.doclint_ja
 * JD-Core Version:    0.6.2
 */