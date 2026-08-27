package helpers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageHelper {
    private static final Logger logger = Logger.getLogger(ImageHelper.class.getName());
    public static final int MAX_WIDTH = 1920;
    public static final int MAX_HEIGHT = 1080;
    public static final long MAX_RAW_SIZE_BEFORE_COMPRESS = 5 * 1024 * 1024; // 5 MB

    public static byte[] resizeAndCompressIfNeeded(byte[] originalBytes, String contentType) {
        if (originalBytes == null || originalBytes.length == 0) {
            return originalBytes;
        }

        // Si el contentType indica explícitamente que no es imagen (ej. video, pdf, etc.), no tocar
        if (contentType != null && !contentType.toLowerCase().startsWith("image/")) {
            return originalBytes;
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(originalBytes)) {
            BufferedImage originalImage = ImageIO.read(bais);
            if (originalImage == null) {
                // ImageIO no pudo leerlo (ej. svg, formato no soportado, video), devolver original
                return originalBytes;
            }

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            boolean exceedsDimensions = (width > MAX_WIDTH || height > MAX_HEIGHT);
            boolean exceedsSize = (originalBytes.length > MAX_RAW_SIZE_BEFORE_COMPRESS);

            // Si las dimensiones son adecuadas y no es excesivamente pesado, mantener original
            if (!exceedsDimensions && !exceedsSize) {
                return originalBytes;
            }

            int targetWidth = width;
            int targetHeight = height;

            if (exceedsDimensions) {
                double ratio = Math.min((double) MAX_WIDTH / width, (double) MAX_HEIGHT / height);
                targetWidth = (int) Math.round(width * ratio);
                targetHeight = (int) Math.round(height * ratio);
            }

            boolean hasAlpha = originalImage.getTransparency() != Transparency.OPAQUE;
            int imageType = hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

            BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, imageType);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (hasAlpha) {
                ImageIO.write(scaledImage, "png", baos);
            } else {
                // Compresión JPEG de alta calidad (85%)
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                if (writers.hasNext()) {
                    ImageWriter writer = writers.next();
                    try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                        writer.setOutput(ios);
                        ImageWriteParam param = writer.getDefaultWriteParam();
                        if (param.canWriteCompressed()) {
                            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                            param.setCompressionQuality(0.85f);
                        }
                        writer.write(null, new IIOImage(scaledImage, null, null), param);
                    } finally {
                        writer.dispose();
                    }
                } else {
                    ImageIO.write(scaledImage, "jpg", baos);
                }
            }

            byte[] compressedBytes = baos.toByteArray();
            logger.info("Imagen pre-escalada con éxito: " + originalBytes.length + " bytes -> " + compressedBytes.length + " bytes (" + targetWidth + "x" + targetHeight + ")");
            return compressedBytes;

        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo pre-escalar la imagen en Java, usando bytes originales", e);
            return originalBytes;
        }
    }
}