package reader;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Reader<T> {
    protected BufferedReader reader;

    public abstract T read() throws IOException;

    public void log(String logMsg) {

    }
}
