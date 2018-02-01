/*   */ package com.sun.tools.javac.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class javac_ja extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 5 */     return new Object[][] { { "javac.err.dir.not.found", "ディレクトリがありません: {0}" }, { "javac.err.empty.A.argument", "-Aには引数が必要です。''-Akey''または''-Akey=value''を使用してください" }, { "javac.err.error.writing.file", "{0}の書込みエラーです。{1}" }, { "javac.err.file.not.directory", "ディレクトリではありません: {0}" }, { "javac.err.file.not.file", "ファイルではありません: {0}" }, { "javac.err.file.not.found", "ファイルが見つかりません: {0}" }, { "javac.err.invalid.A.key", "注釈プロセッサ・オプション''{0}''のキーに指定されている一連の識別子が、ドットで区切られていません" }, { "javac.err.invalid.arg", "{0}は無効な引数です" }, { "javac.err.invalid.flag", "{0}は無効なフラグです" }, { "javac.err.invalid.profile", "無効なプロファイル: {0}" }, { "javac.err.invalid.source", "{0}は無効なソース・リリースです" }, { "javac.err.invalid.target", "{0}は無効なターゲット・リリースです" }, { "javac.err.no.source.files", "ソース・ファイルがありません" }, { "javac.err.no.source.files.classes", "ソース・ファイルまたはクラス名がありません" }, { "javac.err.profile.bootclasspath.conflict", "profileとbootclasspathオプションは同時に使用できません" }, { "javac.err.req.arg", "{0}には引数が必要です" }, { "javac.fullVersion", "{0}フル・バージョン\"{1}\"" }, { "javac.msg.bug", "コンパイラで例外が発生しました({0})。Bug Database (http://bugs.java.com)で重複がないかをご確認のうえ、Java bugレポート・ページ(http://bugreport.java.com)でJavaコンパイラに対するbugの登録をお願いいたします。レポートには、そのプログラムと下記の診断内容を含めてください。ご協力ありがとうございます。" }, { "javac.msg.io", "\n\n入出力エラーが発生しました。\n詳細は次のスタック・トレースで調査してください。\n" }, { "javac.msg.plugin.not.found", "プラグインが見つかりません: {0}" }, { "javac.msg.plugin.uncaught.exception", "\n\nプラグインで捕捉されない例外がスローされました。\n詳細は次のスタック・トレースで調査してください。\n" }, { "javac.msg.proc.annotation.uncaught.exception", "\n\n注釈処理で捕捉されない例外がスローされました。\n詳細は次のスタック・トレースで調査してください。\n" }, { "javac.msg.resource", "\n\nシステム・リソースが不足しています。\n詳細は次のスタック・トレースで調査してください。\n" }, { "javac.msg.usage", "使用方法: {0} <options> <source files>\n使用可能なオプションのリストについては、-helpを使用します" }, { "javac.msg.usage.header", "使用方法: {0} <options> <source files>\n使用可能なオプションには次のものがあります。" }, { "javac.msg.usage.nonstandard.footer", "これらは非標準オプションであり予告なしに変更されることがあります。" }, { "javac.opt.A", "注釈プロセッサに渡されるオプション" }, { "javac.opt.AT", "ファイルからの読取りオプションおよびファイル名" }, { "javac.opt.J", "<flag>を実行システムに直接渡す" }, { "javac.opt.Werror", "警告が発生した場合にコンパイルを終了する" }, { "javac.opt.X", "非標準オプションの概要を出力する" }, { "javac.opt.Xbootclasspath.a", "ブートストラップ・クラス・パスに追加する" }, { "javac.opt.Xbootclasspath.p", "ブートストラップ・クラス・パスに付加する" }, { "javac.opt.Xdoclint", "javadocコメントの問題に関する推奨チェックを有効にする" }, { "javac.opt.Xdoclint.custom", "\n        javadocコメントの問題に関する特定のチェックを有効または無効にします。\n        ここで、<group>はaccessibility、html、missing、referenceまたはsyntaxのいずれかで、\n        <access>はpublic、protected、packageまたはprivateのいずれかです。" }, { "javac.opt.Xdoclint.subopts", "(all|none|[-]<group>)[/<access>]" }, { "javac.opt.Xlint", "推奨の警告を有効にする" }, { "javac.opt.Xlint.suboptlist", "特定の警告を有効または無効にする" }, { "javac.opt.Xstdout", "標準出力をリダイレクトする" }, { "javac.opt.arg.class", "<class>" }, { "javac.opt.arg.class.list", "<class1>[,<class2>,<class3>...]" }, { "javac.opt.arg.directory", "<directory>" }, { "javac.opt.arg.dirs", "<dirs>" }, { "javac.opt.arg.encoding", "<encoding>" }, { "javac.opt.arg.file", "<filename>" }, { "javac.opt.arg.flag", "<flag>" }, { "javac.opt.arg.key.equals.value", "key[=value]" }, { "javac.opt.arg.number", "<number>" }, { "javac.opt.arg.path", "<path>" }, { "javac.opt.arg.pathname", "<pathname>" }, { "javac.opt.arg.plugin", "\"name args\"" }, { "javac.opt.arg.profile", "<profile>" }, { "javac.opt.arg.release", "<release>" }, { "javac.opt.bootclasspath", "ブートストラップ・クラス・パスの場所をオーバーライドする" }, { "javac.opt.classpath", "ユーザー・クラス・ファイルおよび注釈プロセッサを検索する位置を指定する" }, { "javac.opt.d", "生成されたクラス・ファイルを格納する位置を指定する" }, { "javac.opt.deprecation", "非推奨のAPIが使用されているソースの場所を出力する" }, { "javac.opt.diags", "診断モードの選択" }, { "javac.opt.encoding", "ソース・ファイルが使用する文字エンコーディングを指定する" }, { "javac.opt.endorseddirs", "推奨規格パスの場所をオーバーライドする" }, { "javac.opt.extdirs", "インストール済拡張機能の場所をオーバーライドする" }, { "javac.opt.g", "すべてのデバッグ情報を生成する" }, { "javac.opt.g.lines.vars.source", "いくつかのデバッグ情報のみを生成する" }, { "javac.opt.g.none", "デバッグ情報を生成しない" }, { "javac.opt.headerDest", "生成されたネイティブ・ヘッダー・ファイルを格納する場所を指定する" }, { "javac.opt.help", "標準オプションの概要を出力する" }, { "javac.opt.implicit", "暗黙的に参照されるファイルについてクラス・ファイルを生成するかどうかを指定する" }, { "javac.opt.maxerrs", "出力するエラーの最大数を設定する" }, { "javac.opt.maxwarns", "出力する警告の最大数を設定する" }, { "javac.opt.moreinfo", "型変数の拡張情報を出力する" }, { "javac.opt.nogj", "言語の汎用性を受け付けない" }, { "javac.opt.nowarn", "警告を発生させない" }, { "javac.opt.parameters", "メソッド・パラメータにリフレクション用のメタデータを生成します" }, { "javac.opt.pkginfo", "package-infoファイルの処理を指定する" }, { "javac.opt.plugin", "実行されるプラグインの名前とオプション引数" }, { "javac.opt.prefer", "暗黙的にコンパイルされるクラスについて、ソース・ファイルとクラス・ファイルの両方が見つかった際どちらを読み込むか指定する" }, { "javac.opt.print", "指定した型のテキスト表示を出力する" }, { "javac.opt.printProcessorInfo", "プロセッサが処理を依頼される注釈についての情報を印刷する" }, { "javac.opt.printRounds", "注釈処理の往復についての情報を印刷する" }, { "javac.opt.printflat", "内部クラスの変換後に抽象構文ツリーを出力する" }, { "javac.opt.printsearch", "クラス・ファイルの検索位置情報を出力する" }, { "javac.opt.proc.none.only", "注釈処理やコンパイルを実行するかどうかを制御します。" }, { "javac.opt.processor", "実行する注釈プロセッサの名前。デフォルトの検出処理をバイパス" }, { "javac.opt.processorpath", "注釈プロセッサを検索する位置を指定する" }, { "javac.opt.profile", "使用されているAPIが指定したプロファイルで使用可能かどうかを確認します" }, { "javac.opt.prompt", "各エラーで停止する" }, { "javac.opt.retrofit", "既存クラス・ファイルを汎用型で組み替える" }, { "javac.opt.s", "クラス・ファイルのかわりにjavaソースを発行する" }, { "javac.opt.scramble", "バイトコードのprivate識別子にスクランブルをかける" }, { "javac.opt.scrambleall", "バイトコードのpackage可視識別子にスクランブルをかける" }, { "javac.opt.source", "指定されたリリースとソースの互換性を保つ" }, { "javac.opt.sourceDest", "生成されたソース・ファイルを格納する場所を指定する" }, { "javac.opt.sourcepath", "入力ソース・ファイルを検索する位置を指定する" }, { "javac.opt.target", "特定のVMバージョン用のクラス・ファイルを生成する" }, { "javac.opt.verbose", "コンパイラの動作についてメッセージを出力する" }, { "javac.opt.version", "バージョン情報" }, { "javac.version", "{0} {1}" }, { "javac.warn.profile.target.conflict", "プロファイル{0}はターゲット・リリース{1}に対して有効ではありません" }, { "javac.warn.source.target.conflict", "ソース・リリース{0}にはターゲット・リリース{1}が必要です" }, { "javac.warn.target.default.source.conflict", "ターゲット・リリース{0}がデフォルトのソース・リリース{1}と競合しています" } };
/*   */   }
/*   */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.resources.javac_ja
 * JD-Core Version:    0.6.2
 */