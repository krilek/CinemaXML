<?xml version="1.0"?>

<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
    <head>
    <style>
    .seans{
      background-color: #98ff98;
      border: 1px;
      border-style: solid;
      border-color: red;
      padding: 5px;
      display: inline-block;
      margin: 2px;
    }
    </style>
    </head>
      <body>
        <h2>Aktualny repertuar</h2>
        <table border="1">
          <tr bgcolor="#AAA">
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
              <td>
                <xsl:value-of select="tytul"/>
              </td>
              <td>
                <xsl:value-of select="rezyser/osoba/imie"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="rezyser/osoba/nazwisko"/>
              </td>
              <td>
                <xsl:value-of select="opis"/>
              </td>
              <td>
                <xsl:value-of select="dlugosc"/>
              </td>
              <td>
                <xsl:value-of select="data_premiery"/>
              </td>
              <td>
                <xsl:value-of select="ograniczenie_wiekowe"/>
              </td>
              <xsl:variable name="gat" select="gatunki/gatunek" />
              <td>
                <xsl:value-of select="/kino/gatunki/gatunek[@id=$gat]" />
              </td>
            </tr>
          </xsl:for-each>
        </table>
        <h2>Nadchodzące seanse:</h2>
        <xsl:for-each select="kino/seanse/seans">
          <xsl:sort select="start_pokazu"/>
          <xsl:variable name="date" select="xs:date(concat(substring(date, 7, 4), '-',  substring(date, 4, 2), '-', substring(date, 1, 2)))" />
          <xsl:if test="start_pokazu[] &gt; 10"> <!-- Mamy rok 2002-01-20 -->
            <div class="seans">
          </xsl:if>
            <h4>Data pokazu: </h4>
            <p>
              <xsl:value-of select="start_pokazu" />
            </p>
            <h4>Film: </h4>
            <xsl:variable name="film" select="film[text()]" />
            <p>
              <xsl:value-of select="/kino/filmy/film[@id=$film]/tytul" />
            </p>
            <h4>Reżyser: </h4>
            <p>
              <xsl:value-of select="/kino/filmy/film[@id=$film]/rezyser/osoba/imie"/>
              <xsl:text> </xsl:text>
              <xsl:value-of select="/kino/filmy/film[@id=$film]/rezyser/osoba/nazwisko"/>
            </p>
            <xsl:variable name="sala" select="sala[text()]" />
            <h3>
              <xsl:value-of select="/kino/sale_filmowe/sala[@id=$sala]/nazwa"/>
              <xsl:text> </xsl:text>
              <small><xsl:value-of select="/kino/sale_filmowe/sala[@id=$sala]/typ"/></small>
            </h3>
          </div> 
        </xsl:for-each>

      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>