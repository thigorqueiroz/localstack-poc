package com.thigorqueiroz.localstack.poc

import cloud.localstack.Localstack
import cloud.localstack.awssdkv2.TestUtils
import cloud.localstack.docker.LocalstackDockerExtension
import cloud.localstack.docker.annotation.LocalstackDockerProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.ListBucketsRequest
import java.util.*

@ExtendWith(LocalstackDockerExtension::class)
@LocalstackDockerProperties(services = ["sqs", "s3"])
class PocApplicationTests {

@Test
fun `Create a bucket and validate it` (){
   val bucketName = "test-bucket" + UUID.randomUUID()
    val clientS3AsyncV2 = TestUtils.getClientS3AsyncV2()
    val createBucketRequest = CreateBucketRequest.builder().bucket(bucketName).build()
    val createBucketResponse = clientS3AsyncV2.createBucket(createBucketRequest).get()
    Assertions.assertNotNull(createBucketResponse)
    val listBucketsRequest = ListBucketsRequest.builder().build()
    val listBucketsResponse = clientS3AsyncV2.listBuckets(listBucketsRequest).get()

    val bucket = listBucketsResponse.buckets().first { it.name().equals(bucketName) }
    Assertions.assertNotNull(bucket)
    Assertions.assertEquals(bucket.name(), bucketName)
}
}
