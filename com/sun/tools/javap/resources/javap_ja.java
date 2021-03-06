/*   */ package com.sun.tools.javap.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class javap_ja extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 5 */     return new Object[][] { { "err.bad.constant.pool", "{0}の定数プールの読取り中にエラーが発生しました: {1}" }, { "err.bad.innerclasses.attribute", "{0}のInnerClasses属性が不正です" }, { "err.class.not.found", "クラスが見つかりません: {0}" }, { "err.crash", "重大な内部エラーが発生しました: {0}\n次の情報を含むbugレポートをファイルしてください:\n{1}" }, { "err.end.of.file", "{0}の読取り中に予期しないファイルの終わりが検出されました" }, { "err.file.not.found", "ファイルが見つかりません: {0}" }, { "err.incompatible.options", "オプションの組合せが不正です: {0}" }, { "err.internal.error", "内部エラー: {0} {1} {2}" }, { "err.invalid.arg.for.option", "オプションの引数が無効です: {0}" }, { "err.invalid.use.of.option", "オプションの使用が無効です: {0}" }, { "err.ioerror", "{0}の読取り中にIOエラーが発生しました: {1}" }, { "err.missing.arg", "{0}に値が指定されていません" }, { "err.no.SourceFile.attribute", "SourceFile属性がありません" }, { "err.no.classes.specified", "クラスが指定されていません" }, { "err.not.standard.file.manager", "標準ファイル・マネージャを使用している場合はクラス・ファイルのみ指定できます" }, { "err.prefix", "エラー:" }, { "err.source.file.not.found", "ソース・ファイルが見つかりません" }, { "err.unknown.option", "不明なオプション: {0}" }, { "main.opt.bootclasspath", "  -bootclasspath <path>    ブートストラップ・クラス・ファイルの場所をオーバーライドする" }, { "main.opt.c", "  -c                       コードを逆アセンブルする" }, { "main.opt.classpath", "  -classpath <path>        ユーザー・クラス・ファイルを検索する場所を指定する" }, { "main.opt.constants", "  -constants               final定数を表示する" }, { "main.opt.cp", "  -cp <path>               ユーザー・クラス・ファイルを検索する場所を指定する" }, { "main.opt.help", "  -help  --help  -?        この使用方法のメッセージを出力する" }, { "main.opt.l", "  -l                       行番号とローカル変数表を出力する" }, { "main.opt.p", "  -p  -private             すべてのクラスとメンバーを表示する" }, { "main.opt.package", "  -package                 package/protected/publicクラスおよび\n                           メンバーのみを表示する(デフォルト)" }, { "main.opt.protected", "  -protected               protected/publicクラスおよびメンバーのみを表示する" }, { "main.opt.public", "  -public                  publicクラスおよびメンバーのみを表示する" }, { "main.opt.s", "  -s                       内部タイプ署名を出力する" }, { "main.opt.sysinfo", "  -sysinfo                 処理しているクラスのシステム情報(パス、サイズ、日付、MD5ハッシュ)\n                           を表示する" }, { "main.opt.v", "  -v  -verbose             追加情報を出力する" }, { "main.opt.version", "  -version                 バージョン情報" }, { "main.usage", "使用方法: {0} <options> <classes>\n使用可能なオプションには次のものがあります:" }, { "main.usage.summary", "使用方法: {0} <options> <classes>\n使用可能なオプションのリストについては、-helpを使用します" }, { "note.prefix", "注:" }, { "warn.prefix", "警告:" }, { "warn.unexpected.class", "バイナリ・ファイル{0}に{1}が含まれています" } };
/*   */   }
/*   */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.resources.javap_ja
 * JD-Core Version:    0.6.2
 */