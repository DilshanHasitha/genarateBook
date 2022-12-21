package com.mycompany.myapp.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.mycompany.myapp.config.ApplicationProperties;
import com.mycompany.myapp.domain.Images;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class AmazonClient {

    private final Logger log = LoggerFactory.getLogger(AmazonClient.class);

    private AmazonS3 s3client;
    private final ApplicationProperties appProperties;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    public AmazonClient(ApplicationProperties appProperties) {
        this.appProperties = appProperties;
    }

    @PostConstruct
    private void initAmazonClient() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client =
            AmazonS3ClientBuilder
                .standard()
                .withRegion("ap-south-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public void tet(MultipartFile file) throws IOException {
        // Set the pre-signed URL to expire after one hour.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        // Generate the pre-signed URL.
        System.out.println("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(this.bucketName, this.accessKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration);
        URL url = this.s3client.generatePresignedUrl(generatePresignedUrlRequest);

        // Create the connection and use it to upload the new object using the pre-signed URL.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        OutputStream steam = connection.getOutputStream();
        //        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        steam.write(file.getBytes());
        steam.close();

        // Check the HTTP response code. To complete the upload and make the object available,
        // you must interact with the connection object in some way.
        connection.getResponseCode();
        System.out.println("HTTP response code: " + connection.getResponseCode());

        // Check to make sure that the object was uploaded successfully.
        S3Object object = s3client.getObject(this.bucketName, this.accessKey);
        System.out.println("Object " + object.getKey() + " created in bucket " + object.getBucketName());
    }

    public void uploadFileByte(byte[] fileByte) throws IOException {
        // Set the pre-signed URL to expire after one hour.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        // Generate the pre-signed URL.
        System.out.println("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(this.bucketName, this.accessKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration);

        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        responseHeaders.setContentDisposition("attachment; filename =\"" + java.time.LocalDate.now() + "\"");
        generatePresignedUrlRequest.setResponseHeaders(responseHeaders);

        URL url = this.s3client.generatePresignedUrl(generatePresignedUrlRequest);

        // Create the connection and use it to upload the new object using the pre-signed URL.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        OutputStream steam = connection.getOutputStream();
        //        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        steam.write(fileByte);
        steam.close();

        // Check the HTTP response code. To complete the upload and make the object available,
        // you must interact with the connection object in some way.
        connection.getResponseCode();
        System.out.println("HTTP response code: " + connection.getResponseCode());

        // Check to make sure that the object was uploaded successfully.
        S3Object object = s3client.getObject(this.bucketName, this.accessKey);
        System.out.println("Object " + object.getKey() + " created in bucket " + object.getBucketName());
    }

    public Images uploadFileTos3bucket(Images image, String prefix) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(image.getImageBlob());
        InputStream inputStreamThumbnail = new ByteArrayInputStream(image.getImageBlob());
        InputStream inputStreamStoreSize = new ByteArrayInputStream(image.getImageBlob());
        ObjectMetadata s3ObjectMetadata = new ObjectMetadata();
        s3ObjectMetadata.setContentLength(image.getImageBlob().length);
        // content is a passed in InputStream
        byte[] resultByte = DigestUtils.md5(image.getImageBlob());
        String streamMD5 = new String(Base64.encodeBase64(resultByte));
        s3ObjectMetadata.setContentMD5(streamMD5);

        String uniqueID = "AD" + prefix + System.currentTimeMillis() / 1000;
        String fileExtension = (image.getImageBlobContentType().contains("/")) ? "." + image.getImageBlobContentType().split("/")[1] : "";
        String fileName = uniqueID + fileExtension;

        this.s3client.putObject(
                new PutObjectRequest(this.bucketName, fileName, inputStream, s3ObjectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );

        //Generate Thumbnails
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails
            .of(inputStreamThumbnail)
            .scale(0.5)
            .outputFormat((image.getImageBlobContentType().contains("/")) ? image.getImageBlobContentType().split("/")[1] : "")
            .toOutputStream(os);

        byte[] thumbnailBytes = os.toByteArray();
        InputStream thumbnail = new ByteArrayInputStream(thumbnailBytes);

        ObjectMetadata s3ObjectMetadataThumbnail = new ObjectMetadata();
        s3ObjectMetadataThumbnail.setContentLength(thumbnailBytes.length);
        // content is a passed in InputStream
        byte[] resultByteThumbnail = DigestUtils.md5(thumbnailBytes);
        String streamMD5Thumbnail = new String(Base64.encodeBase64(resultByteThumbnail));
        s3ObjectMetadataThumbnail.setContentMD5(streamMD5Thumbnail);

        this.s3client.putObject(
                new PutObjectRequest(this.bucketName, "0.5/" + uniqueID + fileExtension, thumbnail, s3ObjectMetadataThumbnail)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );

        //Generate Sized Thumbnails
        ByteArrayOutputStream storeSizeOS = new ByteArrayOutputStream();
        Thumbnails
            .of(inputStreamStoreSize)
            .size(1024, 1024)
            .keepAspectRatio(true)
            .crop(Positions.CENTER)
            .outputFormat((image.getImageBlobContentType().contains("/")) ? image.getImageBlobContentType().split("/")[1] : "")
            .toOutputStream(storeSizeOS);

        byte[] storeImageBytes = storeSizeOS.toByteArray();
        InputStream storeImage = new ByteArrayInputStream(storeImageBytes);

        ObjectMetadata s3ObjectMetadataStoreImage = new ObjectMetadata();
        s3ObjectMetadataStoreImage.setContentLength(storeImageBytes.length);
        // content is a passed in InputStream
        byte[] resultByteStoreImage = DigestUtils.md5(storeImageBytes);
        String streamMD5StoreImage = new String(Base64.encodeBase64(resultByteStoreImage));
        s3ObjectMetadataStoreImage.setContentMD5(streamMD5StoreImage);

        this.s3client.putObject(
                new PutObjectRequest(this.bucketName, "storeImages/" + uniqueID + fileExtension, storeImage, s3ObjectMetadataStoreImage)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );

        image.setImageBlob(null);
        image.setImageURL("https://" + this.bucketName + "." + this.endpointUrl + "/storeImages/" + fileName);
        image.setOriginalURL("https://" + this.bucketName + "." + this.endpointUrl + "/" + fileName);
        image.setLowResURL("https://" + this.bucketName + "." + this.endpointUrl + "/0.5/" + fileName);

        inputStream.close();

        return image;
    }

    public List<Images> uploadMultiFileTos3bucket(List<byte[]> image, String prefix) throws IOException {
        List<Images> imageList = new ArrayList<>();
        for (byte[] base64 : image) {
            InputStream inputStream = new ByteArrayInputStream(base64);
            ObjectMetadata s3ObjectMetadata = new ObjectMetadata();
            s3ObjectMetadata.setContentLength(base64.length);
            // content is a passed in InputStream
            byte[] resultByte = DigestUtils.md5(base64);
            String streamMD5 = new String(Base64.encodeBase64(resultByte));
            s3ObjectMetadata.setContentMD5(streamMD5);

            String uniqueID = "MULTI" + prefix + System.currentTimeMillis() / 1000;
            String fileExtension = ".jpeg";
            String fileName = uniqueID + fileExtension;

            this.s3client.putObject(
                    new PutObjectRequest(this.bucketName, "storeImages/" + fileName, inputStream, s3ObjectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
                );

            Images img = new Images();
            img.setImageBlob(null);
            img.setImageURL("https://" + this.bucketName + "." + this.endpointUrl + "/storeImages/" + fileName);
            imageList.add(img);
            inputStream.close();
        }

        return imageList;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private BufferedImage cropImageSquare(byte[] image) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return originalImage;
        }

        // Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
            xc - (squareSize / 2), // x coordinate of the upper-left corner
            yc - (squareSize / 2), // y coordinate of the upper-left corner
            squareSize, // width
            squareSize // height
        );

        return croppedImage;
    }

    public byte[] resizeOTF(String imageURL) throws IOException {
        //Generate Sized Thumbnails
        ByteArrayOutputStream storeSizeOS = new ByteArrayOutputStream();
        URL url = new URL(imageURL);
        Thumbnails.of(url).size(200, 200).keepAspectRatio(true).crop(Positions.CENTER).outputFormat("jpeg").toOutputStream(storeSizeOS);

        return storeSizeOS.toByteArray();
    }
}
