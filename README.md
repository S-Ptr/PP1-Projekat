# PP1-Projekat
Projekat za MicroJava programski jezik. Nivo A
## Specifikacija
Osnovni iskazi, aritmetički izrazi, rad sa nizovima prostih tipova, i rad sa matricama prostih tipova(realizovano dinamickom alokacijom.
## Uputstvo za pokretanje
Preporucuje se pokretanje iz komandne linije terminala, unutar foldera projekta, koristeci PowerShell. Pokretanje iz build.xml, unutar Eclipse okruzenja, je isto opcija, doduse nece komanda read raditi u tom slucaju.
Komanda za generisanje leksera se naziva lexerGen u Ant build fajlu. Alternativno moze se pokrenuti sledeca komanda u terminalu, unutar glavnog foldera:

```java -jar lib/JFlex.jar -d src/rs/ac/bg/etf/pp1/ spec/mjlexer.lex```

Za generisanje parsera potrebno je izabrati repackage unutar build.xml fajla ili izvrsiti sledecu komandu:

```java -jar lib/cup_v10k.jar -destdir src/rs/ac/bg/etf/pp1/ -parser MJParser spec/mjparser.cup```

Za generisanje koda potrebno je pokrenuti Compiler.java klasu kao java aplikaciju. Ime programa je potrebno navesti u odgovarajucoj pathname promenljivoj. Izvrsni kod se nalazi u program.obj fajlu. Za pokretanje koda neophodno je u konzoli izvrsiti sledecu komandu:

```java -cp lib\mj-runtime.jar rs.etf.pp1.mj.runtime.Run test\program.obj```

Ako zelite prikaz izgenerisanog bajtkoda, trebate disasembler pokrenuti preko sledece komande:

```java -cp lib\mj-runtime.jar rs.etf.pp1.mj.runtime.disasm test\program.obj```

Svim komandama se moza dodati >test/izlaz.out 2>test/izlaz.err na kraj, radi ispisa izlaza programa u fajl sa .out ekstenzijom, i mogucih gresaka u .err fajl.

