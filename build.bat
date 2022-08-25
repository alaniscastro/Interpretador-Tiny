Echo off
cls
Echo Instrucoes: 
Echo  Para compilar, use o programa da sua preferencia (javac)
Echo ....
Echo on
javac -encoding "UTF-8" mli.java interpreter\command\*.java interpreter\expr\*.java interpreter\util\*.java interpreter\value\*.java lexical\*.java syntatic\*.java
pause