package com.maximdim.qfx2qif;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Qfx2Qif {

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage: " + Qfx2Qif.class.getSimpleName() + " inFile [outFile]");
            System.exit(1);
        }

        File inFile = new File(args[0]);
        if (!inFile.exists() || !inFile.canRead()) {
            System.out.println("Can't read from " + inFile.getAbsolutePath());
            System.exit(1);
        }
        File outFile;
        if (args.length == 2) {
            outFile = new File(args[1]);
        } else {
            outFile = new File(inFile.getParent(), inFile.getName() + "-converted.qif");
        }

        Qfx2Qif app = new Qfx2Qif();

        try(InputStream is = new FileInputStream(inFile); OutputStream os = new FileOutputStream(outFile)) {
            int count = app.parse(is, os);
            System.out.println(count + " records written into " + outFile.getAbsolutePath());
        }
    }

    public int parse(InputStream is, OutputStream os) throws IOException {
        Document doc = Jsoup.parse(is, StandardCharsets.US_ASCII.name(), "");

        Writer w = new OutputStreamWriter(os);
        w.write("!Type:Bank\n");

        int count = 0;
        for (Element el : doc.body().select("STMTTRN")) {
            // 20181128020000[-5:EST]
            String dtPosted =  el.select("DTPOSTED").first().childNode(0).outerHtml().replace("\n", "");
            String name =  el.select("NAME").text();
            String amount =  el.select("TRNAMT").first().childNode(0).outerHtml().replace("\n", "").trim();

            //System.out.println(dtPosted + " : " + name + " : " + amount);
            String year = dtPosted.substring(0, 4);
            String month = dtPosted.substring(4, 6);
            String day = dtPosted.substring(6, 8);

            w.write("D" + month + "/" + day + "/" + year + "\n");
            w.write("P" + name + "\n");
            w.write("T" + amount + "\n");
            w.write("^\n");

            count++;
        }
        w.flush();
        return count;
    }

}
