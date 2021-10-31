package ro.ase.csie.gabriel.pintea.ebuss.export;


import java.io.*;
import java.util.Date;

public class ExportZ {

    File myFile;
    String fileName;
    FileOutputStream fluxOut;
    BufferedWriter bufferOut;

    public ExportZ(String name) throws FileNotFoundException {
        fileName = name;
        myFile = new File(fileName);
        fluxOut = new FileOutputStream(myFile);
        bufferOut = new BufferedWriter(new OutputStreamWriter(fluxOut));
    }

    public void close() throws IOException {
        bufferOut.close();
    }

    public void WriteLine(String line) throws IOException {
        bufferOut.write(line);
        bufferOut.newLine();
    }

    public String getDescription()
    {
        if (myFile.canRead())
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Filename = ").append(myFile.getName()).append("\n");
            sb.append("Path = ").append(myFile.getAbsolutePath()).append("\n");
            Date df = new Date(myFile.lastModified());
            sb.append("Last modified = ").append(df).append("\n");
            sb.append("Size = ").append(myFile.length()).append("\n");

            return sb.toString();
        }
        else return null;
    }
}