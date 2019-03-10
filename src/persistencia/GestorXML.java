package persistencia;

import components.Avio;
import components.Classe;
import components.Ruta;
import components.RutaIntercontinental;
import components.RutaInternacional;
import components.RutaNacional;
import components.RutaTransoceanica;
import components.TCP;
import components.Tripulant;
import components.TripulantCabina;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import nu.xom.*;
import principal.Companyia;
import principal.GestioVolsExcepcio;
import principal.Vol;

/**
 *
 * @author cesca
 */
public class GestorXML implements ProveedorPersistencia {
        private Document doc;
    private Companyia companyia;

    public Companyia getCompanyia() {
        return companyia;
    }

    public void setCompanyia(Companyia pCompanyia) {
        companyia = pCompanyia;
    }

    public void desarDades(String nomFitxer, Companyia pCompanyia) throws GestioVolsExcepcio {
        construirModel(pCompanyia);
        desarModel(nomFitxer);
    }

    public Companyia carregarDades(String nomFitxer) throws GestioVolsExcepcio {
        carregarFitxer(nomFitxer);
        obtenirDades();
        return companyia;
    }

    /*Paràmetres: Companyia a partir de la qual volem construir el model
     *
     *Acció: 
     *Llegir els atributs de l'objecte Companyia passat per paràmetre per construir
     *un model (document XML) sobre el Document doc (atribut de GestorXML).
     *L'arrel del document XML és "companyia" i heu d'afegir-ne els valors de 
     *codi i nom com atributs. Aquesta arrel, l'heu d'afegir a doc.
     *
     *Un cop fet això, heu de recórrer l'ArrayList elements de Companyia i per 
     *a cada element, afegir un fill a doc. Cada fill tindrà com atributs els 
     *atributs de l'objecte (codi, nom, fabricant, …)
     *
     *Si es tracta d'un avio, a més, heu d'afegir fills addicionals amb els 
     *valors de les classes d'aquest avio. 
     *
     *Si es tracta d'un vol, a més, heu d'afegir fills addicionals amb els 
     *valors dels tripulants d'aquest vol. En el cas de l'atribut avio, heu d'assignar-li
     *el codi de l'avio del vol, i en el cas del cap dels TCP, el passport del cap.
     *
     *Retorn: cap
     */
    private void construirModel(Companyia pCompanyia){
        
        Element arrel = new Element("companyia");
        arrel.addAttribute(new Attribute("codi", Integer.toString(pCompanyia.getCodi())));
        arrel.addAttribute(new Attribute("nom", pCompanyia.getNom()));
        
        for (int i = 0; i < pCompanyia.getComponents().size(); i++) {
            if (pCompanyia.getComponents().get(i) instanceof Avio) {
                Element nAvio = new Element("avio");
                nAvio.addAttribute(new Attribute("codi", ((Avio)pCompanyia.getComponents().get(i)).getCodi()));
                nAvio.addAttribute(new Attribute("fabricant", ((Avio)pCompanyia.getComponents().get(i)).getFabricant()));
                nAvio.addAttribute(new Attribute("model", ((Avio)pCompanyia.getComponents().get(i)).getModel()));
                nAvio.addAttribute(new Attribute("capacitat", Integer.toString(((Avio)pCompanyia.getComponents().get(i)).getCapacitat())));
                for (int j = 0; j < ((Avio)pCompanyia.getComponents().get(i)).getClasses().size(); j++) {
                    Element nClasse = new Element("classe");
                    nClasse.addAttribute(new Attribute("nom", ((Classe)((Avio)pCompanyia.getComponents().get(i)).getClasses().get(j)).getNom()));
                    nClasse.addAttribute(new Attribute("capacitat", Integer.toString(((Classe)((Avio)pCompanyia.getComponents().get(i)).getClasses().get(j)).getCapacitat())));
                    nAvio.appendChild(nClasse);
                }
                arrel.appendChild(nAvio);
            } else if (pCompanyia.getComponents().get(i) instanceof RutaNacional) {
                Element nRutaN = new Element("rutaNacional");
                nRutaN.addAttribute(new Attribute("codi", ((RutaNacional)pCompanyia.getComponents().get(i)).getCodi()));
                nRutaN.addAttribute(new Attribute("aeroportOri", ((RutaNacional)pCompanyia.getComponents().get(i)).getAeroportOri()));
                nRutaN.addAttribute(new Attribute("aeroportDes", ((RutaNacional)pCompanyia.getComponents().get(i)).getAeroportDes()));
                nRutaN.addAttribute(new Attribute("distancia", Double.toString(((RutaNacional)pCompanyia.getComponents().get(i)).getDistancia())));
                nRutaN.addAttribute(new Attribute("pais", ((RutaNacional)pCompanyia.getComponents().get(i)).getPais()));
                arrel.appendChild(nRutaN);
            } else if (pCompanyia.getComponents().get(i) instanceof RutaInternacional) {
                Element nRutaInterN = new Element("rutaInternacional");
                nRutaInterN.addAttribute(new Attribute("codi", ((RutaInternacional)pCompanyia.getComponents().get(i)).getCodi()));
                nRutaInterN.addAttribute(new Attribute("aeroportOri", ((RutaInternacional)pCompanyia.getComponents().get(i)).getAeroportOri()));
                nRutaInterN.addAttribute(new Attribute("aeroportDes", ((RutaInternacional)pCompanyia.getComponents().get(i)).getAeroportDes()));
                nRutaInterN.addAttribute(new Attribute("distancia", Double.toString(((RutaInternacional)pCompanyia.getComponents().get(i)).getDistancia())));
                nRutaInterN.addAttribute(new Attribute("paisOri", ((RutaInternacional)pCompanyia.getComponents().get(i)).getPaisOri()));
                nRutaInterN.addAttribute(new Attribute("paisDes", ((RutaInternacional)pCompanyia.getComponents().get(i)).getPaisDes()));
                arrel.appendChild(nRutaInterN);
            } else if (pCompanyia.getComponents().get(i) instanceof RutaIntercontinental) {
                Element nRutaInterC = new Element("rutaIntercontinental");
                nRutaInterC.addAttribute(new Attribute("codi", ((RutaIntercontinental)pCompanyia.getComponents().get(i)).getCodi()));
                nRutaInterC.addAttribute(new Attribute("aeroportOri", ((RutaIntercontinental)pCompanyia.getComponents().get(i)).getAeroportOri()));
                nRutaInterC.addAttribute(new Attribute("aeroportDes", ((RutaIntercontinental)pCompanyia.getComponents().get(i)).getAeroportDes()));
                nRutaInterC.addAttribute(new Attribute("distancia", Double.toString(((RutaIntercontinental)pCompanyia.getComponents().get(i)).getDistancia())));
                nRutaInterC.addAttribute(new Attribute("paisOri", ((RutaIntercontinental)pCompanyia.getComponents().get(i)).getPaisOri()));
                nRutaInterC.addAttribute(new Attribute("paisDes", ((RutaIntercontinental)pCompanyia.getComponents().get(i)).getPaisDes()));
                nRutaInterC.addAttribute(new Attribute("continentOri", ((RutaIntercontinental)pCompanyia.getComponents().get(i)).getContinentOri()));
                nRutaInterC.addAttribute(new Attribute("continentDes", ((RutaIntercontinental)pCompanyia.getComponents().get(i)).getContinentDes()));
                arrel.appendChild(nRutaInterC);
            } else if (pCompanyia.getComponents().get(i) instanceof RutaTransoceanica) {
                Element nRutaT = new Element("rutaTransoceanica");
                nRutaT.addAttribute(new Attribute("codi", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getCodi()));
                nRutaT.addAttribute(new Attribute("aeroportOri", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getAeroportOri()));
                nRutaT.addAttribute(new Attribute("aeroportDes", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getAeroportDes()));
                nRutaT.addAttribute(new Attribute("distancia", Double.toString(((RutaTransoceanica)pCompanyia.getComponents().get(i)).getDistancia())));
                nRutaT.addAttribute(new Attribute("paisOri", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getPaisOri()));
                nRutaT.addAttribute(new Attribute("paisDes", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getPaisDes()));
                nRutaT.addAttribute(new Attribute("continentOri", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getContinentOri()));
                nRutaT.addAttribute(new Attribute("continentDes", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getContinentDes()));
                nRutaT.addAttribute(new Attribute("ocea", ((RutaTransoceanica)pCompanyia.getComponents().get(i)).getOcea()));
                arrel.appendChild(nRutaT);
            } else if (pCompanyia.getComponents().get(i) instanceof TripulantCabina) {
                Element nTripulantC = new Element("tripulantCabina");
                nTripulantC.addAttribute(new Attribute("passaport", ((TripulantCabina)pCompanyia.getComponents().get(i)).getPassaport()));
                nTripulantC.addAttribute(new Attribute("nom", ((TripulantCabina)pCompanyia.getComponents().get(i)).getNom()));
                nTripulantC.addAttribute(new Attribute("edat", Integer.toString(((TripulantCabina)pCompanyia.getComponents().get(i)).getEdat())));
                
                String pattern = "dd-MM-yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String dataAlta = df.format((((TripulantCabina)pCompanyia.getComponents().get(i)).getDataAlta()));
                
                nTripulantC.addAttribute(new Attribute("dataAlta", dataAlta));
                nTripulantC.addAttribute(new Attribute("horesVol", Integer.toString(((TripulantCabina)pCompanyia.getComponents().get(i)).getHoresVol())));
                nTripulantC.addAttribute(new Attribute("rang", ((TripulantCabina)pCompanyia.getComponents().get(i)).getRang()));
                nTripulantC.addAttribute(new Attribute("barres", Integer.toString(((TripulantCabina)pCompanyia.getComponents().get(i)).getBarres())));
                arrel.appendChild(nTripulantC);
            } else if (pCompanyia.getComponents().get(i) instanceof TCP) {
                Element nTCP = new Element("tripulantCabina");
                nTCP.addAttribute(new Attribute("passaport", ((TCP)pCompanyia.getComponents().get(i)).getPassaport()));
                nTCP.addAttribute(new Attribute("nom", ((TCP)pCompanyia.getComponents().get(i)).getNom()));
                nTCP.addAttribute(new Attribute("edat", Integer.toString(((TCP)pCompanyia.getComponents().get(i)).getEdat())));
                
                String pattern = "dd-MM-yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String dataAlta = df.format((((TCP)pCompanyia.getComponents().get(i)).getDataAlta()));
                
                nTCP.addAttribute(new Attribute("dataAlta", dataAlta));
                nTCP.addAttribute(new Attribute("horesVol", Integer.toString(((TCP)pCompanyia.getComponents().get(i)).getHoresVol())));
                nTCP.addAttribute(new Attribute("rang", ((TCP)pCompanyia.getComponents().get(i)).getRang()));
                arrel.appendChild(nTCP);
            } else if (pCompanyia.getComponents().get(i) instanceof Vol) {
                Element nVol = new Element("vol");
                nVol.addAttribute(new Attribute("codi", ((Vol)pCompanyia.getComponents().get(i)).getCodi()));
                nVol.addAttribute(new Attribute("ruta", ((Ruta)((Vol)pCompanyia.getComponents().get(i)).getRuta()).getCodi()));
                nVol.addAttribute(new Attribute("avio", ((Avio)((Vol)pCompanyia.getComponents().get(i)).getAvio()).getCodi()));
                            // cositas naranjas y sus gets
                String pattern = "dd-MM-yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String dataSortida = df.format((((Vol)pCompanyia.getComponents().get(i)).getDataSortida()));
                String dataArribada = df.format((((Vol)pCompanyia.getComponents().get(i)).getDataArribada()));
                
                nVol.addAttribute(new Attribute("dataSortida", dataSortida));
                nVol.addAttribute(new Attribute("dataArribada", dataArribada));
                nVol.addAttribute(new Attribute("horaSortida", ((Vol)pCompanyia.getComponents().get(i)).getHoraSortida().toString()));
                nVol.addAttribute(new Attribute("horaArribada", ((Vol)pCompanyia.getComponents().get(i)).getHoraArribada().toString()));
                nVol.addAttribute(new Attribute("durada", ((Vol)pCompanyia.getComponents().get(i)).getDurada()));
                
                    Set keyTripulants = ((Vol)pCompanyia.getComponents().get(i)).getTripulants().keySet();
                    Iterator<Tripulant> iteratorTripulants = keyTripulants.iterator();
                    
                    while(iteratorTripulants.hasNext()){
                        Tripulant tripulantActual=iteratorTripulants.next();
                        if (tripulantActual != null) {
                            Element nTripulant = new Element("tripulantCabina");
                            nTripulant.addAttribute(new Attribute("passaport", (tripulantActual.getPassaport())));
                            nTripulant.addAttribute(new Attribute("nom", (tripulantActual.getNom())));
                            nTripulant.addAttribute(new Attribute("edat", Integer.toString((tripulantActual.getEdat()))));

                            String dataAlta = df.format(((tripulantActual.getDataAlta())));

                            nTripulant.addAttribute(new Attribute("dataAlta", dataAlta));
                            nTripulant.addAttribute(new Attribute("horesVol", Integer.toString((tripulantActual.getHoresVol()))));
                            nTripulant.addAttribute(new Attribute("rang", (tripulantActual.getRang())));
                            if (tripulantActual instanceof TripulantCabina) {
                                nTripulant.addAttribute(new Attribute("barres", Integer.toString((((TripulantCabina)tripulantActual).getBarres()))));
                            }
                            nVol.appendChild(nTripulant);
                        }
                    }
                    
                    Element nCap = new Element("TCP");
                    nCap.addAttribute(new Attribute("passaport", ((TCP)((Vol)pCompanyia.getComponents().get(i)).getCap()).getPassaport()));
                    nCap.addAttribute(new Attribute("nom", ((TCP)((Vol)pCompanyia.getComponents().get(i)).getCap()).getNom()));
                    nCap.addAttribute(new Attribute("edat", Integer.toString(((TCP)((Vol)pCompanyia.getComponents().get(i)).getCap()).getEdat())));

                    String dataAlta = df.format((((TCP)((Vol)pCompanyia.getComponents().get(i)).getCap()).getDataAlta()));

                    nCap.addAttribute(new Attribute("dataAlta", dataAlta));
                    nCap.addAttribute(new Attribute("horesVol", Integer.toString(((TCP)((Vol)pCompanyia.getComponents().get(i)).getCap()).getHoresVol())));
                    nCap.addAttribute(new Attribute("rang", ((TCP)((Vol)pCompanyia.getComponents().get(i)).getCap()).getRang()));
                    nVol.appendChild(nCap);
                
                arrel.appendChild(nVol);
            }
            
        }
       
    }

    private void desarModel(String rutaFitxer) throws GestioVolsExcepcio {
        try {
            FileWriter fitxer = new FileWriter(rutaFitxer, false); //Obrim fitxer per sobreescriure
            fitxer.write(doc.toXML());
            fitxer.close();
        } catch (Exception e) {
            throw new GestioVolsExcepcio("GestorXML.desar");
        }
    }

    private void carregarFitxer(String rutaFitxer) throws GestioVolsExcepcio { 
        Builder builder = new Builder();
        try {
            doc = builder.build("/home/cesca/NetBeansProjects/ControlPlatsV4Solucio/"+rutaFitxer);
            System.out.println(doc.toXML());
        } catch (Exception e) {
            throw new GestioVolsExcepcio("GestorXML.carregar");
        }
    }

    /*Paràmetres: cap
     *
     *Acció: 
     *El mètode obtenirDades llegeix el fitxer del disc i el carrega sobre l'atribut 
     *doc de GestorXML.
     *
     *L'objectiu és llegir el document per assignar valors als atributs de Companyia
     *(i la resta d'objectes). Per llegir els valors dels atributs del document 
     *XML, heu de fer servir el mètode getAtributeValue(). 
     *Penseu que l'arrel conté els atributs de la companyia, per tant, al accedir 
     *a l'arrel del document ja podeu crear l'objecte Companyia amb el mètode constructor 
     *escaient de la classe companyia (fixeu-vos que s’ha afgeit un de nou).
     *
     *Un cop fet això, heu de recòrrer el document i per cada fill, haureu d'afegir un
     *element a l'ArrayList components de Companyia (nouXXX(.....)). Penseu que 
     *els mètodes de la classe companyia per afegir components, els hem modificat
     *perquè es pugui afegir un component passat er paràmetre.
     *
     *Si el fill (del document) que s'ha llegit és un avió o un vol, recordeu que a més
     *d'afegir-los a la companyia, també haureu d'afegir en el l'avió les seves classes
     *i en el vol la seva tripulació.
     *
     *Retorn: cap
     */
    private void obtenirDades() throws ParseException {
        Element companyia = doc.getRootElement();
        String nomComp = companyia.getAttributeValue("nom");
        int codiComp = Integer.parseInt(companyia.getAttributeValue("codi"));
        Companyia nCompanyia = new Companyia(codiComp, nomComp);
        
        ArrayList nComp = new ArrayList();
        
        Elements avionsCompanyia = companyia.getChildElements("avio");
        
        for (int i = 0; i < avionsCompanyia.size(); i++) {
            Element avioActual = avionsCompanyia.get(i);
            String codiAA = avioActual.getAttributeValue("codi");
            String fabricantAA = avioActual.getAttributeValue("fabricant");
            String modelAA = avioActual.getAttributeValue("model");
            int capacitatAA = Integer.parseInt(avioActual.getAttributeValue("capacitat"));
            Avio nAvio = new Avio(codiAA, fabricantAA, modelAA, capacitatAA);
                Elements classesAvio = avioActual.getChildElements("classe");
                ArrayList nArrayClasses = new ArrayList();
                for (int j = 0; j < classesAvio.size(); j++) {
                    Element classeActual = classesAvio.get(j);
                    String nomCA = classeActual.getAttributeValue("nom");
                    int capacitatCA = Integer.parseInt(classeActual.getAttributeValue("capacitat"));
                    Classe nClasse = new Classe(nomCA, capacitatCA);
                    nArrayClasses.add(nClasse);
                }
        nAvio.setClasses(nArrayClasses);
        nComp.add(nAvio);
        }
        
        Elements rutesNCompanyia = companyia.getChildElements("rutaNacional");
        
        for (int i = 0; i < rutesNCompanyia.size(); i++) {
            Element rutaNActual = rutesNCompanyia.get(i);
            String codiRNA = rutaNActual.getAttributeValue("codi");
            String paisRNA = rutaNActual.getAttributeValue("pais");
            String aeroOriRNA = rutaNActual.getAttributeValue("aeroportOri");
            String aeroDesRNA = rutaNActual.getAttributeValue("aeroportDes");
            double distanciaRNA = Double.parseDouble(rutaNActual.getAttributeValue("distancia"));
            RutaNacional nRutaNacional = new RutaNacional(codiRNA, paisRNA, aeroOriRNA, aeroDesRNA, distanciaRNA);
            nComp.add(nRutaNacional);
        }
        
        Elements rutesIntCompanyia = companyia.getChildElements("rutaInternacional");
        
        for (int i = 0; i < rutesIntCompanyia.size(); i++) {
            Element rutaIntActual = rutesIntCompanyia.get(i);
            String codiRIA = rutaIntActual.getAttributeValue("codi");
            String aeroOriRIA = rutaIntActual.getAttributeValue("aeroportOri");
            String aeroDesRIA = rutaIntActual.getAttributeValue("aeroportDes");
            String paisOriRIA = rutaIntActual.getAttributeValue("paisOri");
            String paisDesRIA = rutaIntActual.getAttributeValue("paisDes");
            double distanciaRIA = Double.parseDouble(rutaIntActual.getAttributeValue("distancia"));
            RutaInternacional nRutaInternacional = new RutaInternacional(codiRIA, aeroOriRIA, aeroDesRIA, paisOriRIA, paisDesRIA, distanciaRIA);
            nComp.add(nRutaInternacional);
        }
        
        Elements rutesICCompanyia = companyia.getChildElements("rutaIntercontinental");
        
        for (int i = 0; i < rutesICCompanyia.size(); i++) {
            Element rutaICActual = rutesICCompanyia.get(i);
            String codiRICA = rutaICActual.getAttributeValue("codi");
            String aeroOriRICA = rutaICActual.getAttributeValue("aeroportOri");
            String aeroDesRICA = rutaICActual.getAttributeValue("aeroportDes");
            String paisOriRICA = rutaICActual.getAttributeValue("paisOri");
            String paisDesRICA = rutaICActual.getAttributeValue("paisDes");
            String contOriRICA = rutaICActual.getAttributeValue("continentOri");
            String contDesRICA = rutaICActual.getAttributeValue("continentDes");
            double distanciaRICA = Double.parseDouble(rutaICActual.getAttributeValue("distancia"));
            RutaIntercontinental nRutaIntercontinental = new RutaIntercontinental(codiRICA, aeroOriRICA, aeroDesRICA, paisOriRICA, paisDesRICA, contOriRICA, contDesRICA, distanciaRICA);
            nComp.add(nRutaIntercontinental);
        }
        
        Elements rutesTransCompanyia = companyia.getChildElements("rutaTransoceanica");
        
        for (int i = 0; i < rutesTransCompanyia.size(); i++) {
            Element rutaTransActual = rutesTransCompanyia.get(i);
            String codiRTA = rutaTransActual.getAttributeValue("codi");
            String aeroOriRTA = rutaTransActual.getAttributeValue("aeroportOri");
            String aeroDesRTA = rutaTransActual.getAttributeValue("aeroportDes");
            String paisOriRTA = rutaTransActual.getAttributeValue("paisOri");
            String paisDesRTA = rutaTransActual.getAttributeValue("paisDes");
            String contOriRTA = rutaTransActual.getAttributeValue("continentOri");
            String contDesRTA = rutaTransActual.getAttributeValue("continentDes");
            String oceaRTA = rutaTransActual.getAttributeValue("ocea");
            double distanciaRTA = Double.parseDouble(rutaTransActual.getAttributeValue("distancia"));
            RutaTransoceanica nRutaTransoceanica = new RutaTransoceanica(codiRTA, aeroOriRTA, aeroDesRTA, paisOriRTA, paisDesRTA, contOriRTA, contDesRTA, oceaRTA, distanciaRTA);
            nComp.add(nRutaTransoceanica);
        }
        
        Elements tripulantCabinaCompanyia = companyia.getChildElements("tripulantCabina");
        
        for (int i = 0; i < tripulantCabinaCompanyia.size(); i++) {
            Element TripCabActual = tripulantCabinaCompanyia.get(i);
            String passaportTC = TripCabActual.getAttributeValue("passaport");
            String nomTC = TripCabActual.getAttributeValue("nom");
            int edatTC = Integer.parseInt(TripCabActual.getAttributeValue("edat"));
            int horesVTC = Integer.parseInt(TripCabActual.getAttributeValue("horesVol"));
            String rangTC = TripCabActual.getAttributeValue("rang");
            // barres es fa automaticament.
            TripulantCabina nTripulantCabina = new TripulantCabina(passaportTC, nomTC, edatTC, horesVTC, rangTC);
            String dataTC = TripCabActual.getAttributeValue("dataAlta");
            SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
            Date date = parser.parse(dataTC);
            nTripulantCabina.setDataAlta(date);
            nComp.add(nTripulantCabina);
        }
        
        Elements TCPCompanyia = companyia.getChildElements("TCP");
        
        for (int i = 0; i < TCPCompanyia.size(); i++) {
            Element TCPActual = TCPCompanyia.get(i);
            String passaportTCP = TCPActual.getAttributeValue("passaport");
            String nomTCP = TCPActual.getAttributeValue("nom");
            int edatTCP = Integer.parseInt(TCPActual.getAttributeValue("edat"));
            int horesVTCP = Integer.parseInt(TCPActual.getAttributeValue("horesVol"));
            // barres es fa automaticament.
            TCP nTCP = new TCP(passaportTCP, nomTCP, edatTCP, horesVTCP);
            String dataTCP = TCPActual.getAttributeValue("dataAlta");
            SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
            Date date = parser.parse(dataTCP);
            nTCP.setDataAlta(date);
            nComp.add(nTCP);
        }
        
        Elements VolCompanyia = companyia.getChildElements("Vol");
        
        for (int i = 0; i < VolCompanyia.size(); i++) {
            
            SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
            DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_TIME;
            
            Element VolActual = VolCompanyia.get(i);
            String codiVol = VolActual.getAttributeValue("codi");
            String dataSVol = VolActual.getAttributeValue("dataSortida");
            Date dateS = parser.parse(dataSVol);
            String dataAVol = VolActual.getAttributeValue("dataArribada");
            Date dateA = parser.parse(dataAVol);
            String horaSVol = VolActual.getAttributeValue("horaSortida");
            LocalTime horaS = LocalTime.parse(horaSVol, dateFormat);
            String horaAVol = VolActual.getAttributeValue("horaArribada");
            LocalTime horaA = LocalTime.parse(horaAVol, dateFormat);
            Vol nVol = new Vol(codiVol, dateS, dateA, horaS, horaA);
            
            for (int j = 0; j < nComp.size(); j++) {
                if (nComp.get(j) instanceof Avio) {
                    if (((Avio)nComp.get(j)).getCodi() == VolActual.getAttributeValue("avio")) {
                        nVol.setAvio(((Avio)nComp.get(j)));
                    }
                }  
                
                if (nComp.get(j) instanceof Ruta) {
                    if (((Ruta)nComp.get(j)).getCodi() == VolActual.getAttributeValue("ruta")) {
                        nVol.setRuta(((Ruta)nComp.get(j)));
                    }
                }  
            }
            
            HashMap<String, Tripulant> tripulantsVol = new HashMap<>( );
            
            Elements tripulantCabinaVolCompanyia = VolActual.getChildElements("tripulantCabina");
        
            for (int j = 0; j < tripulantCabinaVolCompanyia.size(); j++) {
                Element TripCabActual = tripulantCabinaVolCompanyia.get(j);
                String passaportTC = TripCabActual.getAttributeValue("passaport");
                String nomTC = TripCabActual.getAttributeValue("nom");
                int edatTC = Integer.parseInt(TripCabActual.getAttributeValue("edat"));
                int horesVTC = Integer.parseInt(TripCabActual.getAttributeValue("horesVol"));
                String rangTC = TripCabActual.getAttributeValue("rang");
                // barres es fa automaticament.
                TripulantCabina nTripulantCabina = new TripulantCabina(passaportTC, nomTC, edatTC, horesVTC, rangTC);
                String dataTC = TripCabActual.getAttributeValue("dataAlta");
                Date date = parser.parse(dataTC);
                nTripulantCabina.setDataAlta(date);
                
                tripulantsVol.put(TripCabActual.getAttributeValue("passaport"), nTripulantCabina);
            }
            nVol.setTripulants(tripulantsVol);
            
            Elements TCPVolCompanyia = VolActual.getChildElements("TCP");
        
            for (int j = 0; j < TCPVolCompanyia.size(); j++) {
                Element TCPActual = TCPVolCompanyia.get(j);
                String passaportTCP = TCPActual.getAttributeValue("passaport");
                String nomTCP = TCPActual.getAttributeValue("nom");
                int edatTCP = Integer.parseInt(TCPActual.getAttributeValue("edat"));
                int horesVTCP = Integer.parseInt(TCPActual.getAttributeValue("horesVol"));
                // barres es fa automaticament.
                TCP nTCP = new TCP(passaportTCP, nomTCP, edatTCP, horesVTCP);
                String dataTCP = TCPActual.getAttributeValue("dataAlta");
                Date date = parser.parse(dataTCP);
                nTCP.setDataAlta(date);
                
                nVol.setCap(nTCP);
            }
            
            nComp.add(nVol);
        }
        
    }
}
