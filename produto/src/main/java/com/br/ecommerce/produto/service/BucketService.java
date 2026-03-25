package com.br.ecommerce.produto.service;

import com.br.ecommerce.produto.config.props.MinioProps;
import com.br.ecommerce.produto.dto.BucketFile;
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

    public String getUrl(Long idProduto){
        try{
            var obejct = GetPresignedObjectUrlArgs
                    .builder()
                    .method(Method.GET)
                    .bucket(minioProps.getBucketName())
                    .object(retornaNomeProduto(idProduto))
                    .expiry(1, TimeUnit.DAYS)
                    .build();

            return minioClient.getPresignedObjectUrl(obejct);
        } catch(Exception e){
            return "";
        }
    }

    public void delete(Long idProduto) {
        try {
            var args = RemoveObjectArgs
                    .builder()
                    .bucket(minioProps.getBucketName())
                    .object(retornaNomeProduto(idProduto))
                    .build();

            minioClient.removeObject(args);
        } catch (Exception ignored) {}
    }

    public String retornaNomeProduto(Long idProduto) {
        return String.format("produto-%d", idProduto);
    }
}
