package com.mierro.common.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by 黄晓滨 simba on 2017/5/20.
 * Remarks：
 */
@Controller
public class ZxingController {

    @RequestMapping("/QRCode.jpg")
    public ModelAndView handleRequest(HttpServletResponse response, @RequestParam("character") String character,
                                      Integer width, Integer height) throws IOException, WriterException {
        if (width == null){
            width = 300;
        }
        if (height == null){
            height = 300;
        }
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        String qrcodeFormat = "jpeg";
        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(character, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        MatrixToImageWriter.writeToStream(bitMatrix, qrcodeFormat, out);
        ImageIO.write(image, "jpg", out);
        try {
            out.flush();
        }finally {
            out.close();
        }
        return null;
    }
}
