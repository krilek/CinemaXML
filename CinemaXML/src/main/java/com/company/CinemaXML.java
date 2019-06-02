package com.company;//package my.examples;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// rejestr do tworzenia implementacji DOM
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

// Implementacja DOM Level 3 Load & Save
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSParser; // Do serializacji (zapisywania) dokumentow
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSOutput;

// Konfigurator i obsluga bledow

// Do pracy z dokumentem


public class CinemaXML    {
    public static Document document;

    public static void main(String[] args) {
        try {
            /*
             * ustawienie systemowej wlasnosci (moze byc dokonane w innym
             * miejscu, pliku konfiguracyjnym w systemie itp.) konkretna
             * implementacja DOM
             */
            System.setProperty(DOMImplementationRegistry.PROPERTY,
                    "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry
                    .newInstance();

            // pozyskanie implementacji Load & Save DOM Level 3 z rejestru
            DOMImplementationLS impl = (DOMImplementationLS) registry
                    .getDOMImplementation("LS");

            // stworzenie DOMBuilder
            LSParser builder = impl.createLSParser(
                    DOMImplementationLS.MODE_SYNCHRONOUS, null);

            // pozyskanie konfiguratora - koniecznie zajrzec do dokumentacji co
            // mozna poustawiac
            DOMConfiguration config = builder.getDomConfig();

            // stworzenie DOMErrorHandler i zarejestrowanie w konfiguratorze
            DOMErrorHandler errorHandler = getErrorHandler();
            config.setParameter("error-handler", errorHandler);

            // set validation feature
            config.setParameter("validate", Boolean.TRUE);

            // set schema language
            config.setParameter("schema-type",
                    "http://www.w3.org/2001/XMLSchema");
            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));
            // set schema location
            config.setParameter("schema-location",  "../cinema.xsd");

            System.out.println("Parsowanie " + "../cinema.xml");
            // sparsowanie dokumentu i pozyskanie "document" do dalszej pracy
            document = builder.parseURI("../cinema.xml");
            Scanner s = new Scanner(System.in);
            NodeList seanse = ((Element)document.getElementsByTagName("seanse").item(0)).getElementsByTagName("seans");
            System.out.println("Dodaj nowe rezerwacje 2.0");
            System.out.println("Aktualne seanse:");
            for(int i=0;i<seanse.getLength();i++){
                Node n = seanse.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element seans = (Element)seanse.item(i);
                    System.out.println("-------------");
                    System.out.println("Seans id: " + seans.getAttribute("id"));
                    NodeList dzieci = seans.getChildNodes();
                    int filmId = -1;
                    int salaId = -1;
                    DateTime startPokazu = null;
                    for (int j=0;j<dzieci.getLength();j++){
                        if(dzieci.item(j).getNodeType() == Node.ELEMENT_NODE){
                            Element elem = (Element)dzieci.item(j);
                            switch (elem.getTagName()){
                                case "film":
                                    filmId = Integer.parseInt(elem.getTextContent());
                                    break;
                                case "sala":
                                    salaId = Integer.parseInt(elem.getTextContent());
                                    break;
                                case "start_pokazu":
                                    startPokazu = new DateTime(elem.getTextContent());
                                    break;
                            }
                        }
                    }
                    if(startPokazu!=null){
                        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
                        System.out.println("Data pokazu: "+ dtf.print(startPokazu));
                    }
                    PrintFilm(ZnajdzElementPoId(document,"film", filmId));
                    PrintSala(ZnajdzElementPoId(document,"sala", salaId));
                    System.out.println("-------------");
                }
            }
            System.out.println("Wybierz seans:");
            int wybranySeans = s.nextInt();
            Element rezerwacjePojemnik = (Element)document.getElementsByTagName("rezerwacje").item(0);
            System.out.println("Podaj pracownika id:");
            int paracownik = s.nextInt();
            System.out.println("Podaj imie i nazwisko rezerwującego:");
            String imie = s.next();
            String nazwisko = s.next();
            System.out.println("Podaj email rezerwującego:");
            String email = s.next();
            System.out.println("Podaj informacje rezerwacji:");
            ArrayList<Siedzenie> siedzenia = new ArrayList<>();
            String end;
            do{
                siedzenia.add(Siedzenie.NoweSiedzenie());
                System.out.println("Kontunuowac? Wpisz n aby zakończyć:");
                end = s.next();
            }while (!end.equals("n"));
            Element nowaRezerwacja = StworzNowaRezerwacje(wybranySeans, paracownik, imie, nazwisko, email, siedzenia);
            rezerwacjePojemnik.appendChild(nowaRezerwacja);

            // pozyskanie serializatora
            LSSerializer domWriter = impl.createLSSerializer();
            // pobranie konfiguratora dla serializatora
            config = domWriter.getDomConfig();
            config.setParameter("xml-declaration", Boolean.TRUE);

            // pozyskanie i konfiguracja Wyjscia
            LSOutput dOut = impl.createLSOutput();
            dOut.setEncoding("UTF-8");
            dOut.setByteStream(new FileOutputStream("../cinema.xml"));

            System.out.println("Serializing document... ");
            domWriter.write(document, dOut);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static Element StworzNowaRezerwacje(int seansId, int pracownikId, String imie, String nazwisko, String email, ArrayList<Siedzenie> siedzenia){
        Element rezerwacja = (Element)document.getElementsByTagName("rezerwacja").item(2);
        Element newRezerwacja = (Element)rezerwacja.cloneNode(true);
        newRezerwacja.getElementsByTagName("seans").item(0).setTextContent(String.valueOf(seansId));
        newRezerwacja.getElementsByTagName("odbierajacy_rezerwacje").item(0).setTextContent(String.valueOf(pracownikId));
        newRezerwacja.getElementsByTagName("typ_rezerwacji").item(0).setTextContent("internetowa");
        Element osoba = ((Element)((Element)newRezerwacja.getElementsByTagName("kontakt").item(0)).getElementsByTagName("osoba").item(0));
        osoba.getElementsByTagName("imie").item(0).setTextContent(imie);
        osoba.getElementsByTagName("nazwisko").item(0).setTextContent(nazwisko);
        ((Element)newRezerwacja.getElementsByTagName("kontakt").item(0)).getElementsByTagName("mail").item(0).setTextContent(email);
        Element zarezerwowane =  (Element)newRezerwacja.getElementsByTagName("zarezerwowane_siedzenia").item(0);
        newRezerwacja.removeChild(zarezerwowane);
        zarezerwowane = document.createElement("zarezerwowane_siedzenia");
        for (Siedzenie s :
                siedzenia) {
            Element siedzenie = document.createElement("siedzenie");
            Element rzad = document.createElement("rzad");
            rzad.setTextContent(s.rzad);
            Element numer_siedzenia = document.createElement("numer_siedzenia");
            numer_siedzenia.setTextContent(s.numerSiedzenia);
            siedzenie.appendChild(rzad);
            siedzenie.appendChild(numer_siedzenia);
            zarezerwowane.appendChild(siedzenie);
        }
        newRezerwacja.insertBefore(newRezerwacja.appendChild(zarezerwowane),newRezerwacja.getElementsByTagName("oplacono").item(0));
        newRezerwacja.getElementsByTagName("oplacono").item(0).setTextContent(String.valueOf(false));
        return newRezerwacja;
    }
    private static void PrintSala(Element sala) {
        System.out.println("Nazwa sali: "+ sala.getElementsByTagName("nazwa").item(0).getTextContent());
        System.out.println("Typ sali: " + sala.getElementsByTagName("typ").item(0).getTextContent());
    }

    // obsluga bledow za pomoca anonimowej klasy wewnetrznej implementujacej
    // DOMErrorHandler
    // por. SAX ErrorHandler
    public static Element ZnajdzElementPoId(Document dom, String nodeNazwa, int id){
        NodeList filmy = dom.getElementsByTagName(nodeNazwa);
        for (int i=0;i<filmy.getLength();i++){
            Node elem = filmy.item(i);
            if(elem.hasAttributes()){
                String val = ((Element)elem).getAttribute("id");
                if(id == Integer.parseInt(val)){
                    return (Element)elem;
                }
            }
        }
        return null;
    }

    public static void PrintFilm(Element filmElem){
        System.out.println("Tytuł: "+ filmElem.getElementsByTagName("tytul").item(0).getTextContent());
        Element rezyser = ((Element)((Element)filmElem.getElementsByTagName("rezyser").item(0)).getElementsByTagName("osoba").item(0));
        System.out.println("Reżyser: "+ rezyser.getElementsByTagName("imie").item(0).getTextContent() + " " + rezyser.getElementsByTagName("nazwisko").item(0).getTextContent());
    }
    public static DOMErrorHandler getErrorHandler() {
        return new DOMErrorHandler() {
            public boolean handleError(DOMError error) {
                short severity = error.getSeverity();
                if (severity == error.SEVERITY_ERROR) {
                    System.out.println("[dom3-error]: " + error.getMessage());
                }
                if (severity == error.SEVERITY_WARNING) {
                    System.out.println("[dom3-warning]: " + error.getMessage());
                }
                if (severity == error.SEVERITY_FATAL_ERROR) {
                    System.out.println("[dom3-fatal-error]: "
                            + error.getMessage());
                }
                return true;
            }
        };
    }

}
