package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.service.AmazonClient;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class BucketController {

    private final AmazonClient amazonClient;

    public BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        try {
            this.amazonClient.tet(file);
            return "done";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "sdsa";
    }

    @PostMapping("/uploadFileByte")
    public String uploadFile(@RequestPart(value = "file") byte[] file) {
        try {
            this.amazonClient.uploadFileByte(file);
            return "done Byte";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "done byte";
    }

    @PostMapping("/uploadFileByteNonSigned")
    public Images uploadFileByteNonSigned(@RequestPart(value = "imageObject") Images imageObject, String prefix) {
        Images returnObject = null;
        try {
            returnObject = this.amazonClient.uploadFileTos3bucket(imageObject, prefix);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    @PostMapping("/uploadMultiFileByteNonSigned")
    public List<Images> uploadMultiFileByteNonSigned(@RequestPart(value = "imageObject") List<byte[]> imageObject, String prefix) {
        List<Images> returnObject = null;
        try {
            returnObject = this.amazonClient.uploadMultiFileTos3bucket(imageObject, prefix);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    @PostMapping("/open/otfResize")
    public String resizeOTF(String imageURL) throws UnsupportedEncodingException {
        byte[] returnObject = null;
        try {
            returnObject = this.amazonClient.resizeOTF(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(returnObject), "UTF-8");
    }

    //    @RequestMapping(path = "/detectLabels", method = RequestMethod.POST)
    //    public List<RecognitionLabel> detectLabels(@RequestPart(value = "file") MultipartFile file) {
    //
    //        List<RecognitionLabel> labels = null;
    //
    //        RekognitionClient rekognition = RekognitionClient.builder()
    //            .region(Region.AP_SOUTH_1)
    //            .build();
    //
    //        try{
    //            DetectLabelsResponse detectLabelsResponse =
    //                rekognition.detectLabels(DetectLabelsRequest.builder()
    //                    .image(Image.builder().bytes(SdkBytes.fromByteArray(file.getBytes()))
    //                        .build()).build());
    //
    //            labels = detectLabelsResponse.labels().stream()
    //                .map((label) -> new RecognitionLabel(label.name(), label.confidence()))
    //                .collect(Collectors.toList());
    //        }catch(Exception ex){
    //            ex.printStackTrace();
    //        }finally {
    //            return labels;
    //        }
    //
    //    }

    private class RecognitionLabel {

        private String name;
        private float confidence;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getConfidence() {
            return confidence;
        }

        public void setConfidence(float confidence) {
            this.confidence = confidence;
        }

        public RecognitionLabel(String name, float confidence) {
            this.name = name;
            this.confidence = confidence;
        }
    }
}
