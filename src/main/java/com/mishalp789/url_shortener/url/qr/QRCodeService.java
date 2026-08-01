package com.mishalp789.url_shortener.url.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    @Value("${app.base-url}")
    private String baseUrl;

    public byte[] generate(String identifier){
        try{
            String url = baseUrl + "/r/" + identifier;
            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(
                    url,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(
                    matrix,
                    "PNG",
                    outputStream
            );
            return outputStream.toByteArray();
        }catch (Exception ex){
            throw new RuntimeException("Unable to generate QR Code",ex);
        }
    }


}
