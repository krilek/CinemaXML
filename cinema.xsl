<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
    <h2>Aktualny repertuar</h2>
    <table border="1">
      <tr bgcolor="#9acd32">
        <th>Tytuł</th>
        <th>Reżyser</th>
        <th>Opis</th>
        <th>Długość</th>
        <th>Data Premiery</th>
        <th>Ograniczenie wiekowe</th>
        <th>Gatunek</th>
      </tr>
      <xsl:for-each select="kino/filmy/film">
        <tr>
          <td><xsl:value-of select="tytul"/></td>
          <td><xsl:value-of select="rezyser/osoba/imie"/><xsl:text> </xsl:text><xsl:value-of select="rezyser/osoba/nazwisko"/></td>
          <td><xsl:value-of select="opis"/></td>
          <td><xsl:value-of select="dlugosc"/></td>
          <td><xsl:value-of select="data_premiery"/></td>
          <td><xsl:value-of select="ograniczenie_wiekowe"/></td>
          <xsl:variable name="gat" select="gatunki/gatunek" />

          <td><xsl:value-of select="/kino/gatunki/gatunek[@id=$gat]"></td>

        </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>