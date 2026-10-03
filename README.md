Simple test of KeePassJava2.

Each module (`V2.2.3`, `V2.2.4`, `V2.2.6`, `V3.0.0`) depends on that release of
KeePassJava2 from Maven Central and has a `Main` that opens `Database-4.1-123.kdbx`.

## Run it with Maven

The point of this project is to prove that the jars published on Maven Central
actually work, not copies built and installed locally. So
[.mvn/maven.config](.mvn/maven.config) makes Maven use a repository inside this
project (`.m2/repository`) in place of `~/.m2`. A local KeePassJava2 build, which installs into
`~/.m2`, can't be picked up by mistake.

That only applies to `mvn` on the command line. An IDE may use its own
local repository setting and resolve the jars from `~/.m2`, which proves nothing.

```
mvn clean package
cd V3.0.0
mvn exec:java -Dexec.mainClass=org.linguafranca.pwdb.Main
```

The modules for 3.x need Java 11 or later.

Downloaded jars are kept in `.m2/repository` (which git ignores). To check
against Central from scratch, delete it first:

```
rm -rf .m2
```
