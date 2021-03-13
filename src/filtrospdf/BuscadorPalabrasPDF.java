package filtrospdf;

import filters.Filter;
import filters.Pipe;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuscadorPalabrasPDF extends Filter {

    private String archivo;

    public BuscadorPalabrasPDF(Pipe in, Pipe out, String archivo){
        super(in, out);
        this.archivo = archivo;
    }

    @Override
    public void transform(){
        try {
            String[] lineas = input.read().trim().split("\n");
            List<String> keywords = find(lineas);
            for (String palabraClave: keywords) {
                output.write(palabraClave + "\n");
            }
            output.closeWriter();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private List<String> find(String[] lineas) {
        List<String> wordsPages = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(new File(archivo));
            PDFTextStripper reader = new PDFTextStripper();
            String pageText;
            for (String item : lineas) {
                StringBuilder wordsFormat = new StringBuilder(item);
                wordsFormat.append(", pages: [");
                for (int i = 1; i <= document.getNumberOfPages(); i++) {
                    reader.setStartPage(i);
                    reader.setEndPage(i);
                    pageText = reader.getText(document);
                    if (pageText != null) {
                        pageText = pageText.replaceAll("\r\n", " ");
                        if (pageText.contains(item) || pageText.contains(item.toLowerCase()) || pageText.contains(item.toUpperCase())) {
                            wordsFormat.append(i);
                            wordsFormat.append(" ");
                        }
                    }
                }
                wordsFormat.append("]");
                wordsPages.add(wordsFormat.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("KWIC Error: No se encontro el archivo a leer.");
        }
        return wordsPages;
    }
}
