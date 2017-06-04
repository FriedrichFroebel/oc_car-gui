<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--
    Minimalistic HTML report template for Checkstyle based on the text template
    (https://github.com/checkstyle/contribution/blob/master/xsl/checkstyle-text.xsl).
    
    Changes:
      * Add some basic HTML structure and line breaks.
      * Use unordered lists for the results.
      * Shorten filenames to the structure present inside the project directory.
      * Use a monospaced font for the error messages.
-->

<!-- The basic template. -->
<xsl:template match="/">
    <html>
        <head>
            <title>Coding Style Check Results</title>
            <style type="text/css">
            code {
                font-family: monospace;
            }
            </style>
        </head>
        <body>
            <h2>Coding Style Check Results</h2>
            <ul style="list-style-type: none;">
                <li>Total files checked: <xsl:number level="any" value="count(descendant::file)"/></li>
                <li>Files with errors: <xsl:number level="any" value="count(descendant::file[error])"/></li>
                <li>Total errors: <xsl:number level="any" value="count(descendant::error)"/></li>
                <li>Errors per file: <xsl:number level="any" value="count(descendant::error) div count(descendant::file)"/></li>
            </ul>

            <xsl:apply-templates/>
        </body>
    </html>
</xsl:template>

<!-- Only display files with errors. All backslashes are converted to normal slashes. -->
<!-- Important note: The split delimiter has to be changed to fit the current repository. -->
<xsl:template match="file[error]">
    <h4><xsl:value-of select="substring-after(translate(@name,'\','/'),'oc_car-gui/')"/></h4>
    <ul>
        <xsl:apply-templates select="error"/>
    </ul>
</xsl:template>

<!-- The error list entry. -->
<!-- Format: Line:Column - Message -->
<xsl:template match="error">
    <li><xsl:value-of select="@line"/>:<xsl:value-of select="@column"/><xsl:text> - </xsl:text><code><xsl:value-of select="@message"/></code></li>
</xsl:template>

</xsl:stylesheet>
