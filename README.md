# DOCX2JATS
Java project, aimed to facilitate DOCX to JATS XML transformation for scientific articles

## What it does?
Downloads docx file, unpacks it to default users' temp directory, parses OOXML and transformes it to JATS XML. For the best result docx document should include good markup: 
1. Titles, subtitles etc  
2. Bibliography as separete list at the end of the documens. Title for bibliography should be explicitly pointed as "references" or "список використаної літератури". 
3. In-text citations should be pointed as [1], [2] or [3,4,5]. For now are supported only citations in square brackets.
4. In-text references for tables and figures are parsed if they mark as "tabl 1.", "table 1", "fig. 1", "figure" or cyrillic analogs.
5. Table and Figure titles for parsing are needed to be situated right above the table or figure. Example: "Table 1. Example table title". For captions there is a need to place them after table and figure and start caption with `*`. Example: "`*` Thhis table caption that will be parsed"
6. For best result bibliography list should be written in AMA or Vancouver citation style. Supports journal articles, books, chapters and conferences. 
7. Examples of well-formed docx files for parsing are presened in the root directory of the project (article1.docx, article2.docx).

## How to use?
Download and unpack the latest release: https://github.com/Vitaliy-1/DOCX2JATS/releases and
run as executable jar file. Please note that jar file and article docx file must be in one parent directory with stylesheets folder.
Archive contains 1.jar file and stylesheets folder, which need to be unziped into one directory. Because I am not good programmer, there is a need to place article in docx format in this folder before making transformation. Suppose archive is unzipped on the drive C in the jats folder. Input article article1.docx is also situated there. From windows cmd user need to go to this folder and enter:
`java -jar 1.jar article1.docx article1.xml`
