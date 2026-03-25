package com.br.ecommerce.loja.service;

import com.br.ecommerce.loja.config.props.MinioProps;
import com.br.ecommerce.loja.dto.BucketFile;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class BucketService {

    private final MinioClient minioClient;
    private final MinioProps minioProps;

    public void upload(BucketFile bucketFile) throws Exception{
        var object = PutObjectArgs
                .builder()
                .bucket(minioProps.getBucketName())
                .object(bucketFile.name())
                .stream(bucketFile.is(), bucketFile.size(), -1)
                .contentType(bucketFile.type())
                .build();
        minioClient.putObject(object);
    }

    public String getUrl(String idLoja){
        try{
            var obejct = GetPresignedObjectUrlArgs
                    .builder()
                    .method(Method.GET)
                    .bucket(minioProps.getBucketName())
                    .object(retornaNomeLogo(idLoja))
                    .expiry(1, TimeUnit.DAYS)
                    .build();

            return minioClient.getPresignedObjectUrl(obejct);
        } catch(Exception e){
            return "";
        }
    }

    public void delete(String idLoja) {
        try {
            var args = RemoveObjectArgs
                    .builder()
                    .bucket(minioProps.getBucketName())
                    .object(retornaNomeLogo(idLoja))
                    .build();

            minioClient.removeObject(args);
        } catch (Exception ignored) {}
    }

    public String retornaNomeLogo(String idLoja) {
        return String.format("logo-%s", idLoja);
    }
}
