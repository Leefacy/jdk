/*     */ package sun.applet.resources;
/*     */ 
/*     */ import java.util.ListResourceBundle;
/*     */ 
/*     */ public class MsgAppletViewer_zh_TW extends ListResourceBundle
/*     */ {
/*     */   public Object[][] getContents()
/*     */   {
/*  32 */     Object[][] arrayOfObject; = { { "textframe.button.dismiss", "關閉" }, { "appletviewer.tool.title", "Applet 檢視器: {0}" }, { "appletviewer.menu.applet", "Applet" }, { "appletviewer.menuitem.restart", "重新啟動" }, { "appletviewer.menuitem.reload", "重新載入" }, { "appletviewer.menuitem.stop", "停止" }, { "appletviewer.menuitem.save", "儲存..." }, { "appletviewer.menuitem.start", "啟動" }, { "appletviewer.menuitem.clone", "複製..." }, { "appletviewer.menuitem.tag", "標記..." }, { "appletviewer.menuitem.info", "資訊..." }, { "appletviewer.menuitem.edit", "編輯" }, { "appletviewer.menuitem.encoding", "字元編碼" }, { "appletviewer.menuitem.print", "列印..." }, { "appletviewer.menuitem.props", "屬性..." }, { "appletviewer.menuitem.close", "關閉" }, { "appletviewer.menuitem.quit", "結束" }, { "appletviewer.label.hello", "您好..." }, { "appletviewer.status.start", "正在啟動 Applet..." }, { "appletviewer.appletsave.filedialogtitle", "將 Applet 序列化為檔案" }, { "appletviewer.appletsave.err1", "將 {0} 序列化為 {1}" }, { "appletviewer.appletsave.err2", "在 appletSave 中: {0}" }, { "appletviewer.applettag", "顯示的標記" }, { "appletviewer.applettag.textframe", "Applet HTML 標記" }, { "appletviewer.appletinfo.applet", "-- 無 Applet 資訊 --" }, { "appletviewer.appletinfo.param", "-- 無參數資訊 --" }, { "appletviewer.appletinfo.textframe", "Applet 資訊" }, { "appletviewer.appletprint.fail", "列印失敗。" }, { "appletviewer.appletprint.finish", "完成列印。" }, { "appletviewer.appletprint.cancel", "列印取消。" }, { "appletviewer.appletencoding", "字元編碼: {0}" }, { "appletviewer.parse.warning.requiresname", "警告: <參數名稱=... 值=...> 標記需要名稱屬性。" }, { "appletviewer.parse.warning.paramoutside", "警告: <param> 標記在 <applet> ... </applet> 之外。" }, { "appletviewer.parse.warning.applet.requirescode", "警告: <applet> 標記需要代碼屬性。" }, { "appletviewer.parse.warning.applet.requiresheight", "警告: <applet> 標記需要高度屬性。" }, { "appletviewer.parse.warning.applet.requireswidth", "警告: <applet> 標記需要寬度屬性。" }, { "appletviewer.parse.warning.object.requirescode", "警告: <object> 標記需要代碼屬性。" }, { "appletviewer.parse.warning.object.requiresheight", "警告: <object> 標記需要高度屬性。" }, { "appletviewer.parse.warning.object.requireswidth", "警告: <object> 標記需要寬度屬性。" }, { "appletviewer.parse.warning.embed.requirescode", "警告: <embed> 標記需要代碼屬性。" }, { "appletviewer.parse.warning.embed.requiresheight", "警告: <embed> 標記需要高度屬性。" }, { "appletviewer.parse.warning.embed.requireswidth", "警告: <embed> 標記需要寬度屬性。" }, { "appletviewer.parse.warning.appnotLongersupported", "警告: 不再支援 <app> 標記，請改用 <applet>:" }, { "appletviewer.usage", "用法: appletviewer <options> url(s)\n\n其中的 <options> 包括:\n  -debug                  在 Java 除錯程式中啟動 Applet 檢視器\n  -encoding <encoding>    指定 HTML 檔案使用的字元編碼\n  -J<runtime flag>        將引數傳送至 java 解譯器\n\n -J 選項不是標準選項，若有變更不另行通知。" }, { "appletviewer.main.err.unsupportedopt", "不支援的選項: {0}" }, { "appletviewer.main.err.unrecognizedarg", "無法辨識的引數: {0}" }, { "appletviewer.main.err.dupoption", "重複使用選項: {0}" }, { "appletviewer.main.err.inputfile", "未指定輸入檔案。" }, { "appletviewer.main.err.badurl", "錯誤的 URL: {0} ( {1} )" }, { "appletviewer.main.err.io", "讀取時發生 I/O 異常狀況: {0}" }, { "appletviewer.main.err.readablefile", "確認 {0} 為檔案且可讀取。" }, { "appletviewer.main.err.correcturl", "{0} 是否為正確的 URL？" }, { "appletviewer.main.prop.store", "AppletViewer 的使用者特定屬性" }, { "appletviewer.main.err.prop.cantread", "無法讀取使用者屬性檔案: {0}" }, { "appletviewer.main.err.prop.cantsave", "無法儲存使用者屬性檔案: {0}" }, { "appletviewer.main.warn.nosecmgr", "警告: 停用安全功能。" }, { "appletviewer.main.debug.cantfinddebug", "找不到除錯程式！" }, { "appletviewer.main.debug.cantfindmain", "在除錯程式中找不到主要方法！" }, { "appletviewer.main.debug.exceptionindebug", "除錯程式發生異常狀況！" }, { "appletviewer.main.debug.cantaccess", "無法存取除錯程式！" }, { "appletviewer.main.nosecmgr", "警告: 未安裝 SecurityManager！" }, { "appletviewer.main.warning", "警告: 未啟動 Applet。請確認輸入包含 <applet> 標記。" }, { "appletviewer.main.warn.prop.overwrite", "警告: 依照使用者要求，暫時覆寫系統屬性: 索引鍵: {0} 舊值: {1} 新值: {2}" }, { "appletviewer.main.warn.cantreadprops", "警告: 無法讀取 AppletViewer 屬性檔案: {0} 使用預設值。" }, { "appletioexception.loadclass.throw.interrupted", "類別載入中斷: {0}" }, { "appletioexception.loadclass.throw.notloaded", "未載入類別: {0}" }, { "appletclassloader.loadcode.verbose", "開啟 {0} 的串流以取得 {1}" }, { "appletclassloader.filenotfound", "尋找 {0} 時找不到檔案" }, { "appletclassloader.fileformat", "載入時發生檔案格式異常狀況: {0}" }, { "appletclassloader.fileioexception", "載入時發生 I/O 異常狀況: {0}" }, { "appletclassloader.fileexception", "載入時發生 {0} 異常狀況: {1}" }, { "appletclassloader.filedeath", "載入時刪除 {0}: {1}" }, { "appletclassloader.fileerror", "載入時發生 {0} 錯誤: {1}" }, { "appletclassloader.findclass.verbose.openstream", "開啟 {0} 的串流以取得 {1}" }, { "appletclassloader.getresource.verbose.forname", "AppletClassLoader.getResource 的名稱: {0}" }, { "appletclassloader.getresource.verbose.found", "找到資源: {0} 作為系統資源" }, { "appletclassloader.getresourceasstream.verbose", "找到資源: {0} 作為系統資源" }, { "appletpanel.runloader.err", "物件或代碼參數！" }, { "appletpanel.runloader.exception", "還原序列化 {0} 時發生異常狀況" }, { "appletpanel.destroyed", "已損毀 Applet。" }, { "appletpanel.loaded", "已載入 Applet。" }, { "appletpanel.started", "已啟用 Applet。" }, { "appletpanel.inited", "已起始 Applet。" }, { "appletpanel.stopped", "已停止 Applet。" }, { "appletpanel.disposed", "已處置 Applet。" }, { "appletpanel.nocode", "APPLET 標記遺漏 CODE 參數。" }, { "appletpanel.notfound", "載入: 找不到類別 {0}。" }, { "appletpanel.nocreate", "載入: 無法建立 {0}。" }, { "appletpanel.noconstruct", "載入: {0} 非公用或沒有公用建構子。" }, { "appletpanel.death", "已刪除" }, { "appletpanel.exception", "異常狀況: {0}。" }, { "appletpanel.exception2", "異常狀況: {0}: {1}。" }, { "appletpanel.error", "錯誤: {0}。" }, { "appletpanel.error2", "錯誤: {0}: {1}。" }, { "appletpanel.notloaded", "起始: 未載入 Applet。" }, { "appletpanel.notinited", "啟動: 未起始 Applet。" }, { "appletpanel.notstarted", "停止: 未啟動 Applet。" }, { "appletpanel.notstopped", "損毀: 未停止 Applet。" }, { "appletpanel.notdestroyed", "處置: 未損毀 Applet。" }, { "appletpanel.notdisposed", "載入: 未處置 Applet。" }, { "appletpanel.bail", "已中斷: 正在結束。" }, { "appletpanel.filenotfound", "尋找 {0} 時找不到檔案" }, { "appletpanel.fileformat", "載入時發生檔案格式異常狀況: {0}" }, { "appletpanel.fileioexception", "載入時發生 I/O 異常狀況: {0}" }, { "appletpanel.fileexception", "載入時發生 {0} 異常狀況: {1}" }, { "appletpanel.filedeath", "載入時刪除 {0}: {1}" }, { "appletpanel.fileerror", "載入時發生 {0} 錯誤: {1}" }, { "appletpanel.badattribute.exception", "HTML 剖析: 寬度/高度屬性的值不正確" }, { "appletillegalargumentexception.objectinputstream", "AppletObjectInputStream 需要非空值載入器" }, { "appletprops.title", "AppletViewer 屬性" }, { "appletprops.label.http.server", "Http 代理主機伺服器:" }, { "appletprops.label.http.proxy", "Http 代理主機連接埠:" }, { "appletprops.label.network", "網路存取:" }, { "appletprops.choice.network.item.none", "無" }, { "appletprops.choice.network.item.applethost", "Applet 主機" }, { "appletprops.choice.network.item.unrestricted", "不受限制" }, { "appletprops.label.class", "類別存取:" }, { "appletprops.choice.class.item.restricted", "受限制" }, { "appletprops.choice.class.item.unrestricted", "不受限制" }, { "appletprops.label.unsignedapplet", "允許未簽署的 Applet:" }, { "appletprops.choice.unsignedapplet.no", "否" }, { "appletprops.choice.unsignedapplet.yes", "是" }, { "appletprops.button.apply", "套用" }, { "appletprops.button.cancel", "取消" }, { "appletprops.button.reset", "重設" }, { "appletprops.apply.exception", "無法儲存屬性: {0}" }, { "appletprops.title.invalidproxy", "無效的項目" }, { "appletprops.label.invalidproxy", "代理主機連接埠必須是正整數值。" }, { "appletprops.button.ok", "確定" }, { "appletprops.prop.store", "AppletViewer 的使用者特定屬性" }, { "appletsecurityexception.checkcreateclassloader", "安全異常狀況: classloader" }, { "appletsecurityexception.checkaccess.thread", "安全異常狀況: thread" }, { "appletsecurityexception.checkaccess.threadgroup", "安全異常狀況: threadgroup: {0}" }, { "appletsecurityexception.checkexit", "安全異常狀況: exit: {0}" }, { "appletsecurityexception.checkexec", "安全異常狀況: exec: {0}" }, { "appletsecurityexception.checklink", "安全異常狀況: link: {0}" }, { "appletsecurityexception.checkpropsaccess", "安全異常狀況: 屬性" }, { "appletsecurityexception.checkpropsaccess.key", "安全異常狀況: 屬性存取 {0}" }, { "appletsecurityexception.checkread.exception1", "安全異常狀況: {0}，{1}" }, { "appletsecurityexception.checkread.exception2", "安全異常狀況: file.read: {0}" }, { "appletsecurityexception.checkread", "安全異常狀況: file.read: {0} == {1}" }, { "appletsecurityexception.checkwrite.exception", "安全異常狀況: {0}，{1}" }, { "appletsecurityexception.checkwrite", "安全異常狀況: file.write: {0} == {1}" }, { "appletsecurityexception.checkread.fd", "安全異常狀況: fd.read" }, { "appletsecurityexception.checkwrite.fd", "安全異常狀況: fd.write" }, { "appletsecurityexception.checklisten", "安全異常狀況: socket.listen: {0}" }, { "appletsecurityexception.checkaccept", "安全異常狀況: socket.accept: {0}:{1}" }, { "appletsecurityexception.checkconnect.networknone", "安全異常狀況: socket.connect: {0}->{1}" }, { "appletsecurityexception.checkconnect.networkhost1", "安全異常狀況: 無法從來源 {1} 連線至 {0}。" }, { "appletsecurityexception.checkconnect.networkhost2", "安全異常狀況: 無法解析主機 {0} 或 {1} 的 IP。" }, { "appletsecurityexception.checkconnect.networkhost3", "安全異常狀況: 無法解析主機 {0} 的 IP。請參閱 trustProxy 屬性。" }, { "appletsecurityexception.checkconnect", "安全異常狀況: connect: {0}->{1}" }, { "appletsecurityexception.checkpackageaccess", "安全異常狀況: 無法存取套裝程式: {0}" }, { "appletsecurityexception.checkpackagedefinition", "安全異常狀況: 無法定義套裝程式: {0}" }, { "appletsecurityexception.cannotsetfactory", "安全異常狀況: 無法設定處理站" }, { "appletsecurityexception.checkmemberaccess", "安全異常狀況: 檢查成員存取" }, { "appletsecurityexception.checkgetprintjob", "安全異常狀況: getPrintJob" }, { "appletsecurityexception.checksystemclipboardaccess", "安全異常狀況: getSystemClipboard" }, { "appletsecurityexception.checkawteventqueueaccess", "安全異常狀況: getEventQueue" }, { "appletsecurityexception.checksecurityaccess", "安全異常狀況: 安全作業: {0}" }, { "appletsecurityexception.getsecuritycontext.unknown", "不明的類別載入器類型。無法檢查 getContext" }, { "appletsecurityexception.checkread.unknown", "不明的類別載入器類型。無法檢查 read {0}" }, { "appletsecurityexception.checkconnect.unknown", "不明的類別載入器類型。無法檢查連線" } };
/*     */ 
/* 200 */     return arrayOfObject;;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.applet.resources.MsgAppletViewer_zh_TW
 * JD-Core Version:    0.6.2
 */