/*     */ package com.sun.xml.internal.rngom.xml.util;
/*     */ 
/*     */ public class Naming
/*     */ {
/*     */   private static final int CT_NAME = 1;
/*     */   private static final int CT_NMSTRT = 2;
/*     */   private static final String nameStartSingles = ":_ΆΌϚϜϞϠՙەऽলਫ਼ઍઽૠଽஜೞะຄຊຍລວະຽᄀᄉᄼᄾᅀᅌᅎᅐᅙᅣᅥᅧᅩᅵᆞᆨᆫᆺᇫᇰᇹὙὛὝιΩ℮〇";
/*     */   private static final String nameStartRanges = "AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩";
/*     */   private static final String nameSingles = "-.़়्ֿٰׄািৗਂ਼ਾਿ઼଼ௗൗัັ༹༵༷༾༿ྗྐྵ゙゚⃡·ːˑ·ـๆໆ々";
/*     */   private static final String nameRanges = "ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ";
/* 112 */   private static final byte[][] charTypeTable = new byte[256][];
/*     */ 
/*     */   private static void setCharType(char c, int type)
/*     */   {
/* 129 */     int hi = c >> '\b';
/* 130 */     if (charTypeTable[hi] == null)
/* 131 */       charTypeTable[hi] = new byte[256];
/* 132 */     charTypeTable[hi][(c & 0xFF)] = ((byte)type);
/*     */   }
/*     */ 
/*     */   private static void setCharType(char min, char max, int type) {
/* 136 */     byte[] shared = null;
/*     */     do {
/* 138 */       if ((min & 0xFF) == 0) {
/* 139 */         for (; min + 'ÿ' <= max; min = (char)(min + 'Ā')) {
/* 140 */           if (shared == null) {
/* 141 */             shared = new byte[256];
/* 142 */             for (int i = 0; i < 256; i++)
/* 143 */               shared[i] = ((byte)type);
/*     */           }
/* 145 */           charTypeTable[(min >> '\b')] = shared;
/* 146 */           if (min + 'ÿ' == max)
/* 147 */             return;
/*     */         }
/*     */       }
/* 150 */       setCharType(min, type);
/* 151 */       min = (char)(min + '\001'); } while (min != max);
/*     */   }
/*     */ 
/*     */   private static boolean isNameStartChar(char c) {
/* 155 */     return charTypeTable[(c >> '\b')][(c & 0xFF)] == 2;
/*     */   }
/*     */ 
/*     */   private static boolean isNameStartCharNs(char c) {
/* 159 */     return (isNameStartChar(c)) && (c != ':');
/*     */   }
/*     */ 
/*     */   private static boolean isNameChar(char c) {
/* 163 */     return charTypeTable[(c >> '\b')][(c & 0xFF)] != 0;
/*     */   }
/*     */ 
/*     */   private static boolean isNameCharNs(char c) {
/* 167 */     return (isNameChar(c)) && (c != ':');
/*     */   }
/*     */ 
/*     */   public static boolean isName(String s) {
/* 171 */     int len = s.length();
/* 172 */     if (len == 0)
/* 173 */       return false;
/* 174 */     if (!isNameStartChar(s.charAt(0)))
/* 175 */       return false;
/* 176 */     for (int i = 1; i < len; i++)
/* 177 */       if (!isNameChar(s.charAt(i)))
/* 178 */         return false;
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isNmtoken(String s) {
/* 183 */     int len = s.length();
/* 184 */     if (len == 0)
/* 185 */       return false;
/* 186 */     for (int i = 0; i < len; i++)
/* 187 */       if (!isNameChar(s.charAt(i)))
/* 188 */         return false;
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isNcname(String s) {
/* 193 */     int len = s.length();
/* 194 */     if (len == 0)
/* 195 */       return false;
/* 196 */     if (!isNameStartCharNs(s.charAt(0)))
/* 197 */       return false;
/* 198 */     for (int i = 1; i < len; i++)
/* 199 */       if (!isNameCharNs(s.charAt(i)))
/* 200 */         return false;
/* 201 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isQname(String s) {
/* 205 */     int len = s.length();
/* 206 */     if (len == 0)
/* 207 */       return false;
/* 208 */     if (!isNameStartCharNs(s.charAt(0)))
/* 209 */       return false;
/* 210 */     for (int i = 1; i < len; i++) {
/* 211 */       char c = s.charAt(i);
/* 212 */       if (!isNameChar(c)) {
/* 213 */         if (c == ':') { i++; if ((i < len) && (isNameStartCharNs(s.charAt(i)))) {
/* 214 */             for (i++; i < len; i++)
/* 215 */               if (!isNameCharNs(s.charAt(i)))
/* 216 */                 return false;
/* 217 */             return true;
/*     */           } }
/* 219 */         return false;
/*     */       }
/*     */     }
/* 222 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 113 */     for (int i = 0; i < "-.़়्ֿٰׄািৗਂ਼ਾਿ઼଼ௗൗัັ༹༵༷༾༿ྗྐྵ゙゚⃡·ːˑ·ـๆໆ々".length(); i++)
/* 114 */       setCharType("-.़়्ֿٰׄািৗਂ਼ਾਿ઼଼ௗൗัັ༹༵༷༾༿ྗྐྵ゙゚⃡·ːˑ·ـๆໆ々".charAt(i), 1);
/* 115 */     for (int i = 0; i < "ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ".length(); i += 2)
/* 116 */       setCharType("ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ".charAt(i), "ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ".charAt(i + 1), 1);
/* 117 */     for (int i = 0; i < ":_ΆΌϚϜϞϠՙەऽলਫ਼ઍઽૠଽஜೞะຄຊຍລວະຽᄀᄉᄼᄾᅀᅌᅎᅐᅙᅣᅥᅧᅩᅵᆞᆨᆫᆺᇫᇰᇹὙὛὝιΩ℮〇".length(); i++)
/* 118 */       setCharType(":_ΆΌϚϜϞϠՙەऽলਫ਼ઍઽૠଽஜೞะຄຊຍລວະຽᄀᄉᄼᄾᅀᅌᅎᅐᅙᅣᅥᅧᅩᅵᆞᆨᆫᆺᇫᇰᇹὙὛὝιΩ℮〇".charAt(i), 2);
/* 119 */     for (int i = 0; i < "AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩".length(); i += 2) {
/* 120 */       setCharType("AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩".charAt(i), "AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩".charAt(i + 1), 2);
/*     */     }
/* 122 */     byte[] other = new byte[256];
/* 123 */     for (int i = 0; i < 256; i++)
/* 124 */       if (charTypeTable[i] == null)
/* 125 */         charTypeTable[i] = other;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.xml.util.Naming
 * JD-Core Version:    0.6.2
 */