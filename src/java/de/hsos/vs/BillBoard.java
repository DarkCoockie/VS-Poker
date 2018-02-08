package de.hsos.vs;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * Implementierung des Billboards. 
 * Die Einträge werden in einer Liste vorgegebener Größe gespeichert.
 * Die Einträge vom Typ BillBoardEntry sind dabei vorformatiert.
 * Die Indizes freier Posterplätze werden dazu in einer Liste gespeichert.
 * Änderungen an Einträgen kann nur der Aufrufer vornehmen, der auch 
 * den Eintrag erzeugt hat.
 * 
 * @author heikerli
 */
public class BillBoard {
    private final short SIZE = 10;
    static private String servlet_ctxt;
    Set<Integer> freelist;
    private BillBoardEntry[] billboard = new BillBoardEntry[SIZE];
    
    public BillBoard(String ctxt) {
        BillBoard.servlet_ctxt = ctxt;
        freelist = new LinkedHashSet<Integer>();
        for (int i = 0; i < SIZE; i++) {
            this.billboard[i] = new BillBoardEntry (i, "<empty>", "<not set>");
            freelist.add(i);
        }
    };

    private class BillBoardEntry {
        int id;
        String text;
        String owner_ip;

        public BillBoardEntry(int i, String text, String caller_ip) {
            id = i;
            this.text = text;
            owner_ip = caller_ip;
        }
         
        public void reset () {
            this.text = "<empty>";
            owner_ip = "<not set>";
        }
        
        /**
         * Billboard-Eintrag wird als html-Tabellenzeile ausgegeben.
         * 
         * @param caller_ip
         * @return html-formatierter Eintrag
         */
        public String getEntry(String caller_ip) {
            StringBuilder result = new StringBuilder();
            result.append("<tr>"
                    + "<td>" + this.id + "</td>\n");
            result.append("<td>");
            String disable_edits = "";

            if (!belongsToCaller(caller_ip)) {
                disable_edits = " style=\"background-color: #eeeeee;\" readonly";
            }
            result.append("<input type=\"text\" size=\"100\" "
                    + "minlength=\"100\" "
                    + "maxlength=\"100\" "
                    + "id=\"input_field_" + id + "\" "
                    + "value=\"" + text  + "\"" + disable_edits + ">");
            result.append("</td>");
            result.append("<td>");
            if (belongsToCaller(caller_ip)) {
                result.append("<button onClick=\"putHttpRequest('" +  
                        BillBoard.servlet_ctxt + "',"
                        + id + ")\">Update</button>");
            }
            result.append("</td>");
            result.append("<td>");
            if (belongsToCaller(caller_ip)) {
                result.append("<button onClick=\"deleteHttpRequest('" +  
                        BillBoard.servlet_ctxt + "',"
                        + + id + ")\">Delete</button>");
            }
            result.append("</td>");
            result.append("</tr>");
            return result.toString();
        };

        private boolean belongsToCaller(String caller_ip) {
            if (caller_ip.equals(this.owner_ip))
                return true;
            return false;
        }
    }

    private int pickIndex () {
        Random rand = new Random();
        int randomNum = rand.nextInt((SIZE - 1) + 1);
        return randomNum;
    }
    /**
     * Hinzufügen eines Eintrags.
     * Aus Gründen der Einfachheit und der Performanz 
     * wird ein vorhandener Eintrag (vergleichbar einer
     * Plakatwand) überschrieben.
     * 
     * @param text Plakat-Text
     * @param poster_ip IP-Adresse des Senders 
     */
    public void createEntry (String text, String poster_ip) {
        if (freelist.isEmpty()) {
            int picke_idx = pickIndex();
            deleteEntry (picke_idx);
        }
        /* Erstbestes Element auswählen */
        int idx = freelist.iterator().next();
        freelist.remove (idx);
        /* Inhalt überschreiben */
        assert (billboard[idx].id == idx) : "Indizierung nicht synchron!"; ;
        billboard[idx].text = text;
        billboard[idx].owner_ip = poster_ip;
    }

    /**
     * Löschen eines Eintrags via id.
     * 
     * @param idx 
     * @param poster_ip 
     */
    public void deleteEntry (int idx, String poster_ip) {
        assert (billboard[idx].owner_ip.equals(poster_ip)) : "Owner falsch!"; ;
        this.deleteEntry(idx);
    }

    /**
     * Löschen eines Eintrags via id.
     * 
     * @param idx 
     * @param poster_ip 
     */
    private void deleteEntry (int idx) {
        billboard[idx].reset();
        freelist.add (idx);
    }
    
    /**
     * Aktualisierung eines Eintrags.
     * 
     * @param idx id des Eintrags
     * @param text Zu ersetzender Text
     * @param poster_ip IP-Adresse des Senders
     */
    public void updateEntry (int idx, String text, String poster_ip) {
        assert (billboard[idx].owner_ip.equals(poster_ip)) : "Owner falsch!"; ;
        billboard[idx].text = text;
    }
    
    /**
     * Lesen eines Eintrags. Der Eintrag wird als html-Tabelle
     * zurückgegeben und kann ohne weiteres in ein html-Dokument
     * eingebunden werden.
     * 
     * @param caller_ip IP-Adresse des Aufrufers
     * @return Eintrag als html-Tabelle
     */
    public String readContents(String caller_ip) {
        StringBuilder result = new StringBuilder();
        result.append("<table border=\"1\" rules=\"none\" cellspacing=\"4\" cellpadding=\"5\">\n");
        for (BillBoardEntry e : billboard) {
            result.append(e.getEntry(caller_ip));
        }
        return result.toString();
    };    
}
