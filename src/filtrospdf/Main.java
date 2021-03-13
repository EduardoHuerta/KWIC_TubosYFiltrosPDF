package filtrospdf;

import filters.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        try{
            String path = "";
            JFileChooser jFC2 = new JFileChooser();
            jFC2.setDialogTitle("KWIC - Seleccione el archivo de texto de palabras a buscar en el PDF");
            jFC2.setCurrentDirectory(new File("src"));
            int res2 = jFC2.showOpenDialog(null);
            if (res2 == JFileChooser.APPROVE_OPTION) {
                path = jFC2.getSelectedFile().getPath();
            }else {
                path = "src/inputFinder.txt";
            }

            String pathPDF= " ";
            JFileChooser jFC = new JFileChooser();
            jFC.setDialogTitle("KWIC - Seleccione el archivo PDF deseado");
            jFC.setCurrentDirectory(new File("src"));
            int res = jFC.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                pathPDF = jFC.getSelectedFile().getPath();
            }else {
                pathPDF = "src/ejemplo.pdf";
            }

            Pipe inToPdf =  new Pipe();

            Pipe pdfToAlpha = new Pipe();
            Pipe alphaToOutput = new Pipe();

            Filter input = new Input(path, inToPdf);
            Filter buscaPalabrasPdf = new BuscadorPalabrasPDF(inToPdf,pdfToAlpha, pathPDF);
            Filter alphabetizer = new Alphabetizer(pdfToAlpha, alphaToOutput);
            Filter output = new Output(alphaToOutput);

            input.start();
            buscaPalabrasPdf.start();
            alphabetizer.start();
            output.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
