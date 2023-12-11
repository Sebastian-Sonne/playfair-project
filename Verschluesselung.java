import java.util.HashSet;

/**
 * Klasse um eine Textnachricht (String) mit einem Passwort (String) zu ver- und entschluesseln (Ausgabe : String)
 * 
 * @author Sebastian Sonne, Alexander Frank
 * @version v4 | 04.12.23
 */
public class Verschluesselung
{
    private static final char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private String[] sentence; 
    private String matrix;      // Verschluesselungsmatrix (in Form eines Strings)

    /**
     * (leer) Konstruktor der Klasse Verschluesselung
     */
    public Verschluesselung()
    {

    }

    /**
     * ACHTUNG  METHODENKOPF auf KEINENFALL ABAENDERN
     * 
     * verschluesselt eine Textnachricht mit einem Passwort
     * @param eingabe   zu verschluesselnde Nachricht
     * @param password  password um die eingabe zu verschluesseln
     * @return          verschluesselte eingabe
     */
    public String encryption(String eingabe, String password) {
        StringBuilder output = new StringBuilder();

        matrix = matrixErzeugen(schluesselBereinigen(password.toUpperCase()));
        eingabeAufteilen(eingabe.toUpperCase());
        
        // verschluesselt jedes Wort im zuvor gefuellten Array
        for(int i=0; i<sentence.length; i++) {
            output.append(verschluesseln(klartextZerlegen(sentence[i])));
            output.append(" ");
        }

        return output.toString();
    }

    /**
     * ACHTUNG  METHODENKOPF auf KEINENFALL ABAENDERN
     * 
     * entschluesselt eine Textnachricht mit einem Passwort
     * 
     * @param eingabe   zu entschluesselnde Nachricht
     * @param password  password um die eingabe zu entschluesseln
     * @return          entschluesselte eingabe
     */
    public String decryption(String eingabe, String password) {
        StringBuilder output = new StringBuilder();
        matrix = matrixErzeugen(schluesselBereinigen(password.toUpperCase()));
        eingabeAufteilen(eingabe.toUpperCase());

        // entschluesselt jedes Wort im zuvor gefuellten Array
        for(int i=0; i<sentence.length; i++) {
            output.append(entschluesseltesReinigen(entschluesseln(klartextZerlegen(sentence[i]))));
            output.append(" ");
        }

        return output.toString(); 
    }

    /**
     * Teil die jedes Wort der Eingabe an eine Platz im Array ein
     * @param   eingabe     aufzuteilende Eingabe
     */
    private void eingabeAufteilen(String s) {
        sentence = s.split(" ");
    }

    /**
     * Entfernt saemtliche Buchstaben aus dem Schluesselwort,
     * die mehr als nur einmal vorkommen.
     * @param s     Zu bereinigendes Schluesselwort
     * @return      Bereinigtes Schluesselwort
     */
    private String schluesselBereinigen(String s) {
        HashSet<Character> set = new HashSet<>();
        StringBuilder ergebnis = new StringBuilder();

        for (char temp : s.toCharArray()) {
            if (!set.contains(temp)) {
                set.add(temp);
                ergebnis.append(temp);
            }
        }

        return ergebnis.toString();
    }

    /**
     * Zerlegt den Klartext in Zweierbloecke und fuellt, wo noetig,
     * mit "X" auf.
     * 
     * @param text      Der zu zerlegende Text als String
     * @return          Der zerlegte Text
     */
    private String klartextZerlegen(String s)
    {
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i + 1) == s.charAt(i) && (i + 1) % 2 == 1) {
                s = s.substring(0, i + 1) + "X" + s.substring(i + 1);
            }
        }
        
        return (s.length() % 2 == 1) ? s += "X" : s;
    }

    /**
     * Erstellt eine Matrix in Form eines Strings.
     * 
     * @param s     Schluesselwort, das der Matrix zugrunde liegt
     * @return      Matrix als String
     */
    private String matrixErzeugen(String s) {
        String matrix = s;
        for (int i = 0; i < alphabet.length; i++) {
            String temp = "" + alphabet[i];
            if (!matrix.contains(temp)) {
                matrix = matrix + temp;
            }
        }
        return matrix;
    }

    /**
     * Bereinigt das entschluesselte Wort um die unnoetigen "X".
     * 
     * @param s     Das zu bereinigende Wort
     * @return das bereinigte Wort
     */
    private String entschluesseltesReinigen(String s) {
        if (s.length() < 1) return s;
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'X') {
                s = s.substring(0, i) + s.substring(i + 1);
            }
        }

        return s;
    }

    /**
     * Verschluesselt ein beliebiges Wort. Achtung: Vorher muss
     * das Schluesselwort gesetzt und die Verschluesselungsmatrix
     * erstellt sein!
     * 
     * @param s     Das zu verschluesselnde Wort
     * @return      Verschluesseltes Wort
     */
    private String verschluesseln(String s) {
        String ergebnis = "";

        for (int i = 0; i < s.length(); i = i + 2)              // Einlesen der Wortes in 2-Buchstaben-Schritten
        {
            int zeile1  = matrix.indexOf(s.charAt(i)) / 5;      // Zeilenkoordinate in der Matrix (erster Buchstabe)
            int spalte1 = matrix.indexOf(s.charAt(i)) % 5;      // Spaltenkoordinate in der Matrix (erster Buchstabe)
            int zeile2  = matrix.indexOf(s.charAt(i + 1)) / 5;  // Zeilenkoordinate in der Matrix (zweiter Buchstabe)
            int spalte2 = matrix.indexOf(s.charAt(i + 1)) % 5;  // Spaltenkoordinate in der Matrix (zweiter Buchstabe)

            if (zeile1 == zeile2)   // Fall 1 beim Verschluesseln (s. Arbeitsblatt)
            {
                spalte1 = (spalte1 + 1) % 5;
                spalte2 = (spalte2 + 1) % 5;
            }
            else if (spalte1 == spalte2)    // Fall 2 beim Verschluesseln (s. Arbeitsblatt)
            {
                zeile1 = (zeile1 + 1) % 5;
                zeile2 = (zeile2 + 1) % 5;
            }
            else    // Fall 3 beim Verschluesseln (s. Arbeitsblatt)
            {
                int temp = spalte1;
                spalte1 = spalte2;
                spalte2 = temp;
            }

            char c1 = matrix.charAt(zeile1 * 5 + spalte1);
            char c2 = matrix.charAt(zeile2 * 5 + spalte2);

            ergebnis = ergebnis + c1 + c2;
            // ergebnis = ergebnis.concat("" + c1).concat("" + c2);
        }

        return ergebnis;
    }

    /**
     * Entschluesselt ein beliebiges Wort. Achtung: Vorher muss
     * das Schluesselwort gesetzt und die Verschluesselungsmatrix
     * erstellt sein!
     * 
     * @param s     Das zu entschluesselnde Wort
     * @return      Entschluesseltes, jedoch nicht bereinigtes Wort
     */
    private String entschluesseln(String s)
    {
        String ergebnis = "";

        for (int i = 0; i < s.length(); i = i + 2)
        {
            int zeile1  = matrix.indexOf(s.charAt(i)) / 5;
            int spalte1 = matrix.indexOf(s.charAt(i)) % 5;
            int zeile2  = matrix.indexOf(s.charAt(i + 1)) / 5;
            int spalte2 = matrix.indexOf(s.charAt(i + 1)) % 5; 

            if (zeile1 == zeile2)
            {
                spalte1 = (spalte1 + 4) % 5;
                spalte2 = (spalte2 + 4) % 5;
            }
            else if (spalte1 == spalte2)
            {
                zeile1 = (zeile1 + 4) % 5;
                zeile2 = (zeile2 + 4) % 5;
            }
            else
            {
                int temp = spalte1;
                spalte1 = spalte2;
                spalte2 = temp;
            }

            char c1 = matrix.charAt(zeile1 * 5 + spalte1);
            char c2 = matrix.charAt(zeile2 * 5 + spalte2);

            ergebnis = ergebnis + c1 + c2;
        }

        return ergebnis;
    }
}
