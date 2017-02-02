package com.mwm.loyal.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageUtils {
    public static BufferedImage compressPhoto(String zpzl, InputStream in, int newWidth, int newHeight) {
        BufferedImage imgsrc = null;
        try {
            imgsrc = ImageIO.read(in);
            int width = imgsrc.getWidth(null);
            int height = imgsrc.getHeight(null);
//����������
            if (newWidth != 0) {
                if (newWidth >= width) {
                    if (newHeight < height) {
                        width = width * newHeight / height;
                        height = newHeight;
                    }
                } else if (newHeight >= height) {
                    height = height * newWidth / width;
                    width = newWidth;
                } else if (height > width) {
                    width = width * newHeight / height;
                    height = newHeight;
                } else {
                    height = height * newWidth / width;
                    width = newWidth;
                }
            }
//���������Ž���
            width = newWidth;
            height = newHeight;
            BufferedImage newimg = new BufferedImage(width, height, 1);
            newimg.getGraphics().drawImage(imgsrc, 0, 0, width, height, null);
            BufferedImage localBufferedImage1 = newimg;
            return localBufferedImage1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException localIOException2) {
                localIOException2.printStackTrace();
            }
        }
    }

    public static BufferedImage compressPhotoToText(InputStream in,
                                                    int newWidth, int newHeight, String pressText, String fontName,
                                                    int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
        BufferedImage imgsrc = null;
        try {
            imgsrc = ImageIO.read(in);
            int width = imgsrc.getWidth(null);
            int height = imgsrc.getHeight(null);
            if (newWidth != 0) {
                if (newWidth >= width) {
                    if (newHeight < height) {
                        width = width * newHeight / height;
                        height = newHeight;
                    }
                } else if (newHeight >= height) {
                    height = height * newWidth / width;
                    width = newWidth;
                } else if (height > width) {
                    width = width * newHeight / height;
                    height = newHeight;
                } else {
                    height = height * newWidth / width;
                    width = newWidth;
                }
            }
            BufferedImage newimg = new BufferedImage(width, height, 1);
            Graphics2D g = newimg.createGraphics();
            g.drawImage(imgsrc, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // ��ָ���������ˮӡ����
            for (int i = 0; i < 5; i++) {
                g
                        .drawString(pressText,
                                (width - (getLength(pressText) * fontSize)) / 5
                                        * i + x, (height - fontSize) / 5 * i
                                        + y);
            }
            g.dispose();
            BufferedImage localBufferedImage1 = newimg;
            return localBufferedImage1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException localIOException2) {
                localIOException2.printStackTrace();
            }
        }
    }

    public static BufferedImage rotateImage(BufferedImage bufferedimage,
                                            int degree) {
        // System.out.println("==="+degree);
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int new_w = 0;
        int new_h = 0;
        int a_w = 0;
        int a_h = 0;
        int x = 0;
        int y = 0;
        int type = bufferedimage.getColorModel().getTransparency();
        if (degree % 180 == 0) {
            new_w = w;
            new_h = h;
            a_w = w / 2;
            a_h = h / 2;
        } else if (degree % 90 == 0) {
            new_w = h;
            new_h = w;
            a_w = h / 2;
            a_h = w / 2;
        } else {
            new_w = w;
            new_h = h;
            a_w = w / 2;
            a_h = h / 2;
        }
        x = new_w / 2 - w / 2;
        y = new_h / 2 - h / 2;
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(new_w, new_h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), a_w, a_h);
        graphics2d.translate(x, y);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    public static void printRandImg(HttpServletRequest request,
                                    HttpServletResponse response) {
        int width = 65, height = 20;
        try {
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            Random random = new Random();
            g.setColor(getRandColor(200, 250));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Times   New   Roman", Font.PLAIN, 18));
            g.setColor(getRandColor(160, 200));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(10);
                int yl = random.nextInt(10);
                g.drawLine(x, y, x + xl, y + yl);
            }
            char c[] = new char[62];
            for (int i = 97, j = 0; i < 123; i++, j++) {
                c[j] = (char) i;
            }
            for (int o = 65, p = 26; o < 91; o++, p++) {
                c[p] = (char) o;
            }
            for (int m = 48, n = 52; m < 58; m++, n++) {
                c[n] = (char) m;
            }
            String sRand = "";
            for (int i = 0; i < 4; i++) {
                // int x = random.nextInt(62);
                // String rand = String.valueOf(c[x]);
                // sRand += rand;
                // g.setColor(new Color(20 + random.nextInt(110), 20 + random
                // .nextInt(110), 20 + random.nextInt(110)));
                // g.drawString(rand, 13 * i + 6, 16);
                int x = random.nextInt(62);
                String rand = String.valueOf(c[x]).toUpperCase();
                if ("I".equals(rand) || "O".equals(rand) || "0".equals(rand))
                    rand = "W";
                sRand += rand;
                g.setColor(new Color(20 + random.nextInt(110), 20 + random
                        .nextInt(110), 20 + random.nextInt(110)));
                g.drawString(rand, 13 * i + 6, 16);
            }
            // ����֤�����SESSION
            request.getSession().setAttribute("rand", sRand.toUpperCase());
            g.dispose();
            ImageIO.setUseCache(true);
            ServletOutputStream stream = response.getOutputStream();
            ImageIO.write(image, "JPEG", stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static byte[] getImageFromURL(String urlPath) {
        byte[] data = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            // conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                data = readInputStream(is);
            } else {
                data = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return data;
    }

    /**
     * ��ȡInputStream���ݣ�תΪbyte[]��������
     *
     * @param is InputStream����
     * @return ����byte[]����
     */

    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    //public void outputFileImage(String zpcc,String zpgg,String xzjd,String pressText,byte[] zp)
    public static void outputHtmlImg(HttpServletResponse response, InputStream pi) {
        response.reset();
        response.setContentType("image/jpeg");
        BufferedImage imgout;
        BufferedImage imgin;
        int zpccint = 20;
        int zpggint = 20;
        int xzjdint = 0;
        try {
            OutputStream sos = response.getOutputStream();
            imgin = ImageUtils.compressPhoto("", pi, zpccint, zpggint);
            imgout = ImageUtils.rotateImage(imgin, xzjdint);
            ImageIO.write(imgout, "jpg", sos);
            imgout.flush();
            if (imgin != null) {
                imgin.flush();
            }
            pi.close();
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] imgToByte(byte[] zp, String zpcc, String zpgg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage imgout = null;
        BufferedImage imgin = null;
        int zpccint = 20;
        int zpggint = 20;
        if (!"".equals(zpcc)) {
            zpccint = Integer.parseInt(zpcc);
        }
        if (!"".equals(zpgg)) {
            zpggint = Integer.parseInt(zpgg);
        }
        try {
            InputStream pi = null;
            if (zp != null) {
                pi = StreamUtil.byteTOInputStream(zp);
                if (!zpcc.equals("")) {
                    imgin = ImageUtils.compressPhoto("", pi, zpccint, zpggint);
                    imgout = imgin;
                    ImageIO.write(imgout, "jpg", out);
                    imgout.flush();
                    imgin.flush();
                }
                pi.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    //����ͼƬ����������
    public static byte[] imgToByteNew(byte[] zp, String zpcc, String zpgg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int zpccint = 0;
        int zpggint = 0;
        if (!"".equals(zpcc)) {
            zpccint = Integer.parseInt(zpcc);
        }
        if (!"".equals(zpgg)) {
            zpggint = Integer.parseInt(zpgg);
        }
        try {
            InputStream pi = null;
            if (zp != null) {
                pi = StreamUtil.byteTOInputStream(zp);
                BufferedImage bi = ImageIO.read(pi);
                Image itemp = bi.getScaledInstance(zpccint, zpggint, bi.SCALE_DEFAULT);
                BufferedImage newimg = new BufferedImage(zpccint, zpggint, 1);
                newimg.getGraphics().drawImage(itemp, 0, 0, zpccint, zpggint, null);
                ImageIO.write(newimg, "jpg", out);
                //ImageIO.write((BufferedImage) newimg, "jpg", new File("c:/3.jpg"));
                itemp.flush();
                pi.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * ����ͼ�񣨰��߶ȺͿ�����ţ�
     *
     * @param zp   Դͼ��
     * @param zpcc ���ź�Ŀ��
     * @param zpgg ���ź�ĸ߶�
     * @param bb   ��������ʱ�Ƿ���Ҫ���ף�trueΪ����; falseΪ������;
     */
    public static byte[] imgToByte(byte[] zp, String zpcc, String zpgg, boolean bb) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            double ratio = 0.0; // ���ű���
            int width = Integer.parseInt(zpcc);
            int height = Integer.parseInt(zpgg);
            InputStream pi = StreamUtil.byteTOInputStream(zp);
            BufferedImage bi = ImageIO.read(pi);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // �������
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//����
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            //ImageIO.write((BufferedImage) itemp, "jpg", new File("c:/3.jpg"));
            ImageIO.write((BufferedImage) itemp, "jpg", out);
            itemp.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static byte[] imgToByte(byte[] zp, String zpcc, String zpgg, String xzjd, String pressText) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage imgout = null;
        BufferedImage imgin = null;
        int zpccint = 20;
        int zpggint = 20;
        int xzjdint = 0;
        if (!"".equals(zpcc)) {
            zpccint = Integer.parseInt(zpcc);
        }
        if (!"".equals(zpgg)) {
            zpggint = Integer.parseInt(zpgg);
        }
        if (!"".equals(xzjd)) {
            xzjdint = Integer.parseInt(xzjd);
        }
        try {
            InputStream pi = null;
            if (zp != null) {
                pi = StreamUtil.byteTOInputStream(zp);
                if (zpcc != null && !zpcc.equals("")) {
                    if (pressText != null && !pressText.equals("")) {
                        imgin = ImageUtils.compressPhotoToText(pi, zpccint, zpggint, pressText, "����", Font.BOLD, Color.white, 80, 0, 0, 0.5f);
                    }
                    imgin = ImageUtils.compressPhoto("", pi, zpccint, zpggint);
                    if (xzjdint != 0)
                        imgout = ImageUtils.rotateImage(imgin, xzjdint);
                    else
                        imgout = imgin;
                    ImageIO.write(imgout, "jpg", out);
                    imgout.flush();
                    imgin.flush();
                }
                pi.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


//	ByteArrayOutputStream out = new ByteArrayOutputStream();
//	ImageIO.write(img, "PNG", out);
//	out.toByteArray()

    //���ˮӡ
    public static byte[] addImageWaterMark(byte[] imageByte, String str) throws Exception {
        // TODO Auto-generated method stub
        if (null != imageByte) {
            try {
                // ��ȡ�����ֽ���
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);
                // ���ͼƬ����
                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                // ��õ�ǰ��ͼƬ�ĸ߶ȺͿ��
                int thisHeight = bufferedImage.getHeight();
                int thisWidth = bufferedImage.getWidth();
                // �������������
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // ��byteת��Ϊ������
                InputStream inputStream = StreamUtil.byteTOInputStream(imageByte);
                bufferedImage = ImageUtils.compressPhotoToText2(inputStream, thisWidth, thisHeight, str, "����", Font.BOLD, Color.black, 30, 1.0f);
                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                imageByte = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                throw new Exception(e);

            }

        }
        return imageByte;
    }


    public static void main(String[] args) {
        try {
            byte[] img = image2byte("C:/1.jpg");
            img = addImageWaterMark(img, "���ȣ�106.1234567��ά�ȣ�85.123456");
            byte2image(img, "C:/2.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    // byte���鵽ͼƬ
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals(""))
            return;
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(
                    new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in "
                    + path);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }

    public static BufferedImage compressPhotoToText2(InputStream in,
                                                     int newWidth, int newHeight, String pressText, String fontName,
                                                     int fontStyle, Color color, int fontSize, float alpha) {
        BufferedImage imgsrc = null;
        try {
            newHeight -= fontSize;

            imgsrc = ImageIO.read(in);
            int width = imgsrc.getWidth(null);
            int height = imgsrc.getHeight(null);
            if (newWidth != 0) {
                if (newWidth >= width) {
                    if (newHeight < height) {
                        width = width * newHeight / height;
                        height = newHeight;
                    }
                } else if (newHeight >= height) {
                    height = height * newWidth / width;
                    width = newWidth;
                } else if (height > width) {
                    width = width * newHeight / height;
                    height = newHeight;
                } else {
                    height = height * newWidth / width;
                    width = newWidth;
                }
            }
            BufferedImage newimg = new BufferedImage(width, height, 1);
            Graphics2D g = newimg.createGraphics();
            g.drawImage(imgsrc, 0, 0, width, height, null);

            BufferedImage waterMarkImg = new BufferedImage(width, fontSize, 1);
            g = waterMarkImg.createGraphics();
            g.setBackground(Color.WHITE);
            g.fillRect(0, 0, waterMarkImg.getWidth(), waterMarkImg.getHeight());

            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            FontMetrics fm = g.getFontMetrics();
            int fontH = fm.getAscent();
            int fontW = fm.stringWidth(pressText) / 2;
            g.drawString(pressText, width / 2 - fontW, fontH - 1);//newHeight-fontH //height-(fontH/4)
            g.dispose();

            int[] imageArrayOne = new int[newimg.getWidth() * newimg.getHeight()];
            imageArrayOne = newimg.getRGB(0, 0, newimg.getWidth(), newimg.getHeight(), imageArrayOne, 0, newimg.getWidth());
            int[] imageArrayTwo = new int[waterMarkImg.getWidth() * waterMarkImg.getHeight()];
            imageArrayTwo = waterMarkImg.getRGB(0, 0, waterMarkImg.getWidth(), waterMarkImg.getHeight(), imageArrayTwo, 0, waterMarkImg.getWidth());

            BufferedImage localBufferedImage1 = new BufferedImage(width, height + fontSize, BufferedImage.TYPE_INT_RGB);
            localBufferedImage1.setRGB(0, 0, width, height, imageArrayOne, 0, width);
            localBufferedImage1.setRGB(0, height, width, fontSize, imageArrayTwo, 0, width);

            return localBufferedImage1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException localIOException2) {
                localIOException2.printStackTrace();
            }
        }
    }

    /**
     * ����text�ĳ��ȣ�һ�������������ַ���
     *
     * @param text
     * @return
     */
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if ((text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }
}
