//package org.cancogenvirusseq.singularity.repository;
//
//import static org.cancogenvirusseq.singularity.config.db.FlywayConfig.createFlyway;
//import static org.cancogenvirusseq.singularity.config.db.R2DBCConfiguration.createPsqlConnectionFactory;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.util.UUID;
//import lombok.val;
//import org.cancogenvirusseq.singularity.config.db.PostgresProperties;
//import org.cancogenvirusseq.singularity.repository.model.Archive;
//import org.cancogenvirusseq.singularity.repository.model.ArchiveMeta;
//import org.cancogenvirusseq.singularity.repository.model.ArchiveSetQuery;
//import org.cancogenvirusseq.singularity.repository.model.ArchiveStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.r2dbc.core.DatabaseClient;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import reactor.test.StepVerifier;
//
//@Testcontainers
//public class ArchiveCustomRepoTests {
//  @Container
//  public PostgreSQLContainer postgreSQLContainer =
//      new PostgreSQLContainer("postgres:10-alpine")
//          .withDatabaseName("singularity")
//          .withUsername("test")
//          .withPassword("test");
//
//  private ArchivesCustomRepo repo;
//
//  @BeforeEach
//  public void setUp() {
//    val postgresProps = getPostgresProperties();
//
//    // run migrations on test container db
//    val flyway = createFlyway(postgresProps);
//    flyway.migrate();
//
//    // setup db client for repo
//    val databaseClient =
//        DatabaseClient.builder()
//            .connectionFactory(createPsqlConnectionFactory(postgresProps))
//            .build();
//
//    repo = new ArchivesCustomRepo(databaseClient);
//  }
//
//  @Test
//  public void testSaveAndFindArchiveAll() {
//    val newArchiveAll =
//        Archive.builder()
//            .status(ArchiveStatus.COMPLETE)
//            .objectId(UUID.randomUUID())
//            .timestamp(1625761918L)
//            .meta(ArchiveMeta.builder().numOfDownloads(4).numOfSamples(100).build())
//            .build();
//
//    val saveAndFindMono =
//        repo.save(newArchiveAll).flatMap(a -> repo.findArchiveAllById(a.getId())).log();
//
//    StepVerifier.create(saveAndFindMono)
//        .assertNext(
//            foundArchive -> {
//              // we need to update ID since its generated by DB
//              val expectedArchive = newArchiveAll.toBuilder().id(foundArchive.getId()).build();
//              assertEquals(expectedArchive, foundArchive);
//            })
//        .verifyComplete();
//  }
//
//  @Test
//  public void testSaveAndFindArchiveSetQuery() {
//    val newArchiveSetQuery =
//        ArchiveSetQuery.builder()
//            .status(ArchiveStatus.COMPLETE)
//            .objectId(UUID.randomUUID())
//            .setQueryHash("ASDF")
//            .timestamp(1625761918L)
//            .meta(ArchiveMeta.builder().numOfDownloads(4).numOfSamples(100).build())
//            .build();
//
//    val saveAndFindMono =
//        repo.save(newArchiveSetQuery).flatMap(a -> repo.findArchiveSetQueryById(a.getId())).log();
//
//    StepVerifier.create(saveAndFindMono)
//        .assertNext(
//            foundArchive -> {
//              // we need to update ID since its generated by DB
//              val expectedArchive = newArchiveSetQuery.toBuilder().id(foundArchive.getId()).build();
//              assertEquals(expectedArchive, foundArchive);
//            })
//        .verifyComplete();
//  }
//
//  private PostgresProperties getPostgresProperties() {
//    return PostgresProperties.builder()
//        .port(postgreSQLContainer.getFirstMappedPort())
//        .host(postgreSQLContainer.getHost())
//        .database(postgreSQLContainer.getDatabaseName())
//        .password(postgreSQLContainer.getPassword())
//        .username(postgreSQLContainer.getUsername())
//        .build();
//  }
//}
