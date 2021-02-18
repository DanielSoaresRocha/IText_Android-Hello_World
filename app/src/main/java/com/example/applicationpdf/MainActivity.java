package com.example.applicationpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    private Button gerarPDF;
    private final int REQUEST_CODE = 3434;

    public static final String DEST = "/sdcard/teste.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permicao garantiad", Toast.LENGTH_SHORT).show();
            //File write logic here
        }

        try{
        File file = new File(DEST);
        if (!file.exists()) {
            file.createNewFile();
        }
        file.getParentFile().mkdirs();
        manipulatePdf(DEST);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void inicializar() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        float[] columnWidths = {1, 3, 3, 3, 3, 3};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));

        Cell cell = new Cell(1, 6)
                .add(new Paragraph("Relatório teste"))
                .setFontSize(13)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        table.addHeaderCell(cell);

        for (int i = 0; i < 2; i++) {
            Cell[] headerFooter = new Cell[]{
                    new Cell()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Sessão")),
                    new Cell()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Data")),
                    new Cell()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Horário Inicial")),
                    new Cell()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Horário Final")),
                    new Cell()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Taxa de acerto")),
                    new Cell()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Experimentador"))
            };

            for (Cell hfCell : headerFooter) {
                if (i == 0) {
                    table.addHeaderCell(hfCell);
                }
            }
        }

        Date data = new Date();
        Calendar cal = Calendar.getInstance(new Locale("BR"));
        cal.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        cal.setTime(data);

        String dataFormatada = cal.get(Calendar.DAY_OF_MONTH) + "/" +(cal.get(Calendar.MONTH)+1) + "/" +cal.get(Calendar.YEAR);
        String hora = (cal.get(Calendar.HOUR)-1)+":" +cal.get(Calendar.MINUTE);

        for (int counter = 0; counter < 100; counter++) {
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(counter + 1))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(dataFormatada)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(hora)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(hora)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("50%")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("Daniel")));
        }

        doc.add(table);

        doc.close();
        Toast.makeText(this, DEST+"is saved to", Toast.LENGTH_SHORT).show();
    }

}