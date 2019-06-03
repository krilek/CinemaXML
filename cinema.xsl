<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="utf-8" />
  <xsl:template match="/">
    <html>
    <head>
    <title>CinemaXML</title>
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
    .seans:hover{
      background-color: #FFff98;
    }
    .hot{
      background-color: #EE0000;
    }
    .hot:after{
      color: yellow;
      content: 'Polecamy!';
      display: inline-block;
      margin-left: 3px;
    }
   table {
  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

table td, table th {
  border: 1px solid #ddd;
  padding: 8px;
}

table tr:nth-child(even){background-color: #f2f2f2;}

table tr:hover {background-color: #ddd;}

table th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #4CAF50;
  color: white;
}
    </style>
    </head>
      <body>
        <h2>Aktualny repertuar</h2>
        <table>
          <tr>
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
            <div class="seans">
            <h4>Data pokazu: </h4>
            <p>
              <xsl:value-of select="start_pokazu" />
            </p>
            <h4>Film: </h4>
            <xsl:variable name="film" select="film[text()]" />
            <xsl:choose>
              <xsl:when test="/kino/filmy/film[@id=$film]/tytul = 'Kill Bill'">
                <p class="hot">
                  <xsl:value-of select="/kino/filmy/film[@id=$film]/tytul" />
                </p>
              </xsl:when>
              <xsl:otherwise>
                <p>
                  <xsl:value-of select="/kino/filmy/film[@id=$film]/tytul" />
                </p>
              </xsl:otherwise>
            </xsl:choose>
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