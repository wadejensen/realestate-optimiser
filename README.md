# Realestate optimiser

#### Scala.js on Node.js
A scala.js project deployed to Node.js (because reasons), for determining the optimal place to live.
All common components are shared between the back and front ends. 
The project is setup to be cross-compiled for both Javascript and JVM targets, 
however due to coupling with the Node-only express middleware framework, it is not technically possible to 
deploy to a JVM target.

This project template can easily be adapted for cross-compilation.

In order to get the scala.io Node.js, and express bindings / dependencies working we need to do a little work.
The dependencies are not hosted in a Maven-like repository, so instead they are built from scratch with SBT 
and added to your local ivy cache (one of the first places SBT searches for dependencies before calling out to 
Maven, Bintray, etc).

#### Building dependencies
```
## Scala.js core
git clone https://github.com/scalajs-io/core.git 
cd core
# Build the dependency and public to ~/.ivy/local/package-name
sbt clean publish-local
cd ..
 

# Node bindings
git clone https://github.com/scalajs-io/nodejs.git
cd nodejs
# Build the dependency and public to ~/.ivy/local/package-name
sbt clean publish-local
cd ..
 
# Express bindings
git clone https://github.com/scalajs-io/express.git
cd express
sbt clean publish-local
cd ..
 
```

The version numbers for all of the scalajs-io dependencies need to match. Latest version is `0.4.3` at time of writing.
If they do not, you need to edit of the `build.sbt` file such that the scalaJsIoVersion 
variable used in libraryDependencies is the same for all repos, and in each case,
and rebuild/ republish the dependencies. 

In order for the Node.js binding to work, you will naturally need node installed.
In order for the express binding to work, you will need express installed (`npm install express`).

#### Build and deploy scala.js project


This is an example on how to cross compile code to Scala.js and Scala JVM.

The sbt root project contains "js" and "jvm" projects which both independently compile to run on their respective runtime targets.
You can deploy the "js" project artifact to Node.js, **and/or** the "jvm" artifact to a Java Virtual Machine as desired.

To compile the meta-project:
```
> sbt
sbt:Foo root project> compile
```
```
[info] Updating fooJVM...
[info] Done updating.
[info] Updating ...
[info] Done updating.
[info] Updating fooJS...
[info] Done updating.
[info] Compiling 2 Scala sources to /path/to/project/jvm/target/scala-2.12/classes ...
[info] Done compiling.
[info] Compiling 2 Scala sources to /path/to/project/js/target/scala-2.12/classes ...
[info] Done compiling.
[success] Total time: 7 s, completed 12/02/2018 7:43:57 AM

```

To run the compiled JS artifact from sbt:
```
    sbt> fooJS/run
```
```    
    [info] Fast optimizing /path/to/project/js/target/scala-2.12/foo-fastopt.js
    [info] Running au.com.wjensen.Main
    Hello world scala.js
    Using shared library code: 2^2 = ...
    4
```

To deploy in production using Node.js:
```
node /path/to/project/js/target/scala-2.12/foo-fastopt.js
```
```
Hello world scala.js
Using shared library code: 2^2 = ...
4

```

To run the compiled Scala artifact from sbt:
```    
    sbt> fooJVM/run
```
```    
    [info] Packaging /path/to/project/jvm/target/scala-2.12/foo_2.12-0.1-SNAPSHOT.jar ...
    [info] Done packaging.
    [info] Running au.com.wjensen.Main 
    Hello world Scala JVM
    Using shared library code: 2^2 = ...
    4
```

To deploy in production with only the JVM as a dependency, we need to first use the sbt-assembly plugin 
to build a fat jar which contains the Scala runtime and any other required dependencies.
Build the fat jar:
```
sbt> fooJVM/assembly
[info] Including: scala-library.jar
[info] Checking every *.class/*.jar file's SHA-1.
[info] Merging files...
[warn] Merging 'META-INF/MANIFEST.MF' with strategy 'discard'
[warn] Strategy 'discard' was applied to a file
[info] SHA-1: 485ef11a1b172695241597f0eaccb8b4ebe96471
[info] Packaging /path/to/project/jvm/target/scala-2.12/foo-assembly-0.1-SNAPSHOT.jar ...
[info] Done packaging.
[success] Total time: 4 s, completed 12/02/2018 7:49:45 AM
```

Run on the JVM:
```
java -jar /path/to/project/jvm/target/scala-2.12/foo-assembly-0.1-SNAPSHOT.jar

```
```
Hello world Scala JVM
Using shared library code: 2^2 = ...
4
```

More information can be found in the
[Scala.js documentation](http://www.scala-js.org/doc/sbt/cross-building.html) and in the [sbt-scalajs](http://www.scala-js.org/api/sbt-scalajs/latest/#org.scalajs.sbtplugin.cross.CrossProject) plugin documentation, including common pitfalls.

