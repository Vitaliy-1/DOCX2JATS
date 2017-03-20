# DOCX2JATS
Java project, aimed to facilitate DOCX to JATS XML transformation for scientific articles

## What it does?
Downloads docx file, unpacks it to default users' temp directory, parses OOXML and transformes it to JATS XML. For the best result docx document should include good markup: 
1. Titles, subtitles etc  
2. Bibliography as separete list at the end of the documens. It is better to explicitly point title for bibliography list, for example as "references" or "список використаної літератури".
3. In-text citations should be pointed as [1], [2] or [3,4,5]. For now are supported only citations in square brackets.
4. For best result bibliography list should be written in AMA or Vancouver citation style.
5. Examples of well-formed docx files for parsing are presened in the root directory of the project (article1.docx, article2.docx).

## How to use?
Download and unpack the latest release: https://github.com/Vitaliy-1/DOCX2JATS/releases and
run as executable jar file. Please note that jar file must be in one parent directory with stylesheets folder
