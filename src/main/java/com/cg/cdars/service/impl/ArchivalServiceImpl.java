package com.cg.cdars.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cg.cdars.service.ArchivalService;

import java.io.File;

import static com.amazonaws.regions.Regions.AP_SOUTH_1;
import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;

public class ArchivalServiceImpl implements ArchivalService {
    private static final String BUCKET_NAME = "cdars";
    private static final String FOLDER_NAME = "DataSetArchive";
    private static final String PATH_SEPARATOR = "/";

    private String accessKey;
    private String secretKey;
    private AWSCredentials credentials;

    @Override
    public void archive(File target) throws Exception {
        AmazonS3 s3client = new AmazonS3Client(getAWSCredentials());
        s3client.setRegion(Region.getRegion(AP_SOUTH_1));
        s3client.putObject(new PutObjectRequest(BUCKET_NAME, FOLDER_NAME + PATH_SEPARATOR + target.getName(), target)
                .withCannedAcl(PublicRead));
    }

    @Override
    public File retrieve(String archiveRecordId) throws Exception {
        return null;
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
