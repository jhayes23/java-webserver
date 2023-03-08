package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.io.IOException;
public class Htpassword {
    private final HashMap<String, String> passwords;
    private final String filename;
    public Htpassword( String filename ) throws IOException {
        System.out.println( "Password file: " + filename );
        this.filename = filename;
        this.passwords = new HashMap<>();
        this.load();
    }
    protected void parseLine( String line ) {
        String[] tokens = line.split( ":" );
        if( tokens.length == 2 ) {
            passwords.put( tokens[ 0 ], tokens[ 1 ].replace( "{SHA}", ""
            ).trim() );
        }
    }
    public boolean isAuthorized( String authInfo ) {
        // authInfo is provided in the header received from the client
        // as a Base64 encoded string.
        String credentials = new String(
                Base64.getDecoder().decode( authInfo ),
                Charset.forName( "UTF-8" )
        );
        // The string is the key:value pair username:password
        String[] tokens = credentials.split( ":" );
        if(tokens.length != 2){
            return false;
        }
        return verifyPassword(tokens[0],tokens[1]);
    }
    private boolean verifyPassword( String username, String password ) {
        // encrypt the password, and compare it to the password stored
        // in the password file (keyed by username)
        if(passwords.containsKey(username)){
            String encrypt = encryptClearPassword(password);
            return encrypt.equals(passwords.get(username));
        }

        return false;
    }
    private String encryptClearPassword( String password ) {
        // Encrypt the cleartext password (that was decoded from the Base64
        //        String
        // provided by the client) using the SHA-1 encryption algorithm
        try {
            MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
            byte[] result = mDigest.digest( password.getBytes() );
            return Base64.getEncoder().encodeToString( result );
        } catch( Exception e ) {
            return "";
        }
    }

    private void load() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                parseLine(line);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading Htpassword");
            throw new RuntimeException(e);
        }
    }
}