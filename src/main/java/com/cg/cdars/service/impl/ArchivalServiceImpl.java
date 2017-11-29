package com.cg.cdars.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.cg.cdars.service.ArchivalService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.amazonaws.regions.Regions.AP_SOUTH_1;
import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ArchivalServiceImpl implements ArchivalService {
    private static final String BUCKET_NAME = "cdars";
    private static final String FOLDER_NAME = "DataSetArchive";
    private static final String PATH_SEPARATOR = "/";

    private String accessKey;
    private String secretKey;
    private AWSCredentials credentials;

    @Override
    public String archive(File target) throws Exception {
        AmazonS3 s3client = new AmazonS3Client(getAWSCredentials());
        s3client.setRegion(Region.getRegion(AP_SOUTH_1));

        s3client.putObject(new PutObjectRequest(BUCKET_NAME, FOLDER_NAME + PATH_SEPARATOR + target.getName(), target)
                .withCannedAcl(PublicRead));

        return target.getName();
    }

    @Override
    public File retrieve(String archiveSystemId) throws Exception {
        AmazonS3 s3client = new AmazonS3Client(getAWSCredentials());

        Path outputPath = Files.createTempFile(archiveSystemId, EMPTY);
        File outputFile = outputPath.toFile();

        try {
            S3Object o = s3client.getObject(BUCKET_NAME, FOLDER_NAME + PATH_SEPARATOR + archiveSystemId);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(outputFile);

            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return outputFile;
    }

    private AWSCredentials getAWSCredentials() {
        if (credentials == null) {
            synchronized (this) {
                if (credentials == null) {
                     credentials= new BasicAWSCredentials(accessKey, secretKey);
                }
            }
        }

        return credentials;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
