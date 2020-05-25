# Primavera
스프링부트를 이용한 커뮤니티 사이트 개발

[![Build Status](https://travis-ci.org/csj4032/primavera.svg?branch=master)](https://travis-ci.org/csj4032/primavera)
[![Coverage Status](https://coveralls.io/repos/github/csj4032/primavera/badge.svg)](https://coveralls.io/github/csj4032/primavera)

## Technical Specification
* Java 14 (Switch expressions, text blocks)
* Gradle 6.4.1
* IntelliJ IDEA (2020.1)
* MariaDB 10.3.14
* Springboot 2.3.0.RELEASE (https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes)
* Springboot Security 2.1.6.RELEASE
* Thymeleaf
* Mybatis
* JPA
* Lombok
* Lucy-xss
* Flyway
* Bootstrap

## Requirements Specification
* Social 정보를 이용한 회원 가입
* Social 정보를 이용한 로그인, 로그아웃, 탈퇴
* 게시글 등록, 수정, 삭제, 조회 기능
* 게시글 답글 등록, 수정, 삭제 기능
* 게시글 댓글 등록, 수정, 삭제 기능 및 대댓글 등록, 수정, 삭제
* 게시글 관련 첨부파일 등록, 삭제
* 게시글 편집 기능

## Launch

## chap00
* Domain

## chap01
* Spring Boot Start, Hello World, Configuration

## chap02
* Spring Boot Test, AOP

## chap03
* Spring Boot DataSource, JdbcTemplate, Hikari

## chap04
* Spring Transaction, Mybatis, Logback

## chap05
* Validation, AJAX

## chap06
* Thymeleaf, Sign in

## chap07
* Filter, Lucy-xss-filter

## chap08
* Spring Security

## chap09
* Spring OAuth2 Social Login

## chap10
* Post, Pagination, WYSIHTML5

## chap11
* Hierarchy Article Contents, Reply

## chap12
* Spring Security Annotation, File Upload

## chap13
* JPA, ModelMapper

## chap14
* WebFlux, Spring Config Server

## chap15
* Excel Import Export
* Sentry & Slack

## hello
* Hystrix, Openfeign, Turbine

## Library Version
* gradle 라이브러리 버전 정보 정리
* lombok = '1.18.6'
* logback = '1.2.3'

## ERD
![ERD](https://github.com/csj4032/primavera/blob/master/primavera.png)

## MYBATIS SQL

```sql

CREATE DATABASE primavera DEFAULT CHARACTER SET utf8mb4;

CREATE USER 'primavera'@'localhost' IDENTIFIED BY 'primavera';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER ON primavera.* TO 'primavera'@'localhost';

CREATE TABLE IF NOT EXISTS USER (
    ID       BIGINT(20)   NOT NULL AUTO_INCREMENT,
    EMAIL    VARCHAR(50)  NOT NULL,
    PASSWORD VARCHAR(100) NOT NULL,
    NICKNAME VARCHAR(45)  NOT NULL,
    STATUS   CHAR(1)      NOT NULL DEFAULT 'A',
    REG_DT   DATETIME     NOT NULL,
    MOD_DT   DATETIME              DEFAULT NULL,
    PRIMARY KEY (ID),
    UNIQUE KEY EMAIL_UNIQUE (EMAIL)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS USER_CONNECTION (
    ID           BIGINT(20)   NOT NULL AUTO_INCREMENT,
    EMAIL        VARCHAR(50)  NOT NULL,
    PROVIDER     TINYINT(11)  NOT NULL,
    PROVIDER_ID  VARCHAR(45)   NOT NULL,
    DISPLAY_NAME VARCHAR(45)  DEFAULT NULL,
    PROFILE_URL  VARCHAR(200) DEFAULT NULL,
    IMAGE_URL    VARCHAR(200) DEFAULT NULL,
    ACCESS_TOKEN VARCHAR(200) NOT NULL,
    EXPIRE_TIME  BIGINT(20)   DEFAULT NULL,
    PRIMARY KEY (ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS USER_ROLE (
    USER_ID BIGINT(20) NOT NULL,
    ROLE_ID INT(11)    NOT NULL,
    PRIMARY KEY (USER_ID, ROLE_ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS ROLE (
    ID   INT(11)    NOT NULL AUTO_INCREMENT,
    TYPE TINYINT(3) NOT NULL,
    PRIMARY KEY (ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS POST (
    ID        BIGINT(20)   NOT NULL AUTO_INCREMENT,
    WRITER_ID BIGINT(20)   NOT NULL,
    SUBJECT   VARCHAR(200) NOT NULL,
    CONTENTS  TEXT         NOT NULL,
    STATUS    TINYINT(3)   NOT NULL,
    REG_DT    DATETIME DEFAULT NULL,
    MOD_DT    DATETIME DEFAULT NULL,
    PRIMARY KEY (ID),
    KEY FK_WRITER_ID (WRITER_ID),
    CONSTRAINT FK_POST_WRITER_ID FOREIGN KEY (WRITER_ID) REFERENCES USER (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS ARTICLE (
    ID BIGINT(20) NOT NULL AUTO_INCREMENT,
    P_ID BIGINT(20) NOT NULL DEFAULT 0,
    REFERENCE BIGINT(20) NOT NULL,
    STEP INT(11) NOT NULL,
    LEVEL INT(11) NOT NULL,
    AUTHOR BIGINT(20) NOT NULL,
    SUBJECT VARCHAR(200) NOT NULL,
    STATUS TINYINT(3) NOT NULL,
    HIT BIGINT(20) NOT NULL DEFAULT 0,
    RECOMMEND BIGINT(20) NOT NULL DEFAULT 0,
    DISAPPROVE BIGINT(20) NOT NULL DEFAULT 0,
    REG_DT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    MOD_DT TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (ID),
    KEY FK_WRITER_ID_IDX (AUTHOR),
    CONSTRAINT FK_ARTICLE_AUTHOR_ID FOREIGN KEY (AUTHOR) REFERENCES USER (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ARTICLE_ATTACHMENT (
    ID BIGINT(20) NOT NULL AUTO_INCREMENT,
    ARTICLE_ID BIGINT(20) NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    PATH VARCHAR(200) NOT NULL,
    SIZE INT(11) NOT NULL,
    PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE  IF NOT EXISTS ARTICLE_COMMENT (
    ID BIGINT(20) NOT NULL AUTO_INCREMENT,
    ARTICLE_ID BIGINT(20) NOT NULL,
    LEVEL INT(11) NOT NULL,
    STEP INT(11) NOT NULL,
    COMMENT TEXT NOT NULL,
    AUTHOR VARCHAR(45) NOT NULL,
    STATUS TINYINT(3) NOT NULL,
    REG_DT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    MOD_DT TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (ID)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ARTICLE_CONTENT (
    ID         BIGINT(20) NOT NULL AUTO_INCREMENT,
    ARTICLE_ID BIGINT(20) DEFAULT NULL,
    CONTENTS   text       DEFAULT NULL,
    PRIMARY KEY (ID),
    KEY FK_AUTHOR_IDX (ARTICLE_ID),
    CONSTRAINT FK_ARTICLE_ID FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLE (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS WINNER (
    ID int(11) NOT NULL AUTO_INCREMENT,
    USER_ID int(45) NOT NULL,
    WINNER enum('WINNER','LOSER') NOT NULL,
    REG_DT datetime NOT NULL,
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO ROLE (TYPE)
VALUES (1);
INSERT INTO ROLE (TYPE)
VALUES (2);
INSERT INTO ROLE (TYPE)
VALUES (3);

INSERT INTO `USER`(EMAIL, PASSWORD, NICKNAME, STATUS, REG_DT)
VALUES ('Genius Choi', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Genius', 1, NOW());
INSERT INTO `USER_CONNECTION`(EMAIL, PROVIDER, PROVIDER_ID, DISPLAY_NAME, PROFILE_URL, IMAGE_URL, ACCESS_TOKEN, EXPIRE_TIME)
VALUES ('Genius Choi', 1, 123456789, 'Genius', 'PROFILE', 'IMAGE', '1234567890', -1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (1, 1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (1, 2);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (1, 3);

INSERT INTO `USER`(`EMAIL`, `PASSWORD`, `NICKNAME`, `STATUS`, `REG_DT`)
VALUES ('Son Heung-min', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Son', 1, NOW());
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (2, 1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (2, 2);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (2, 3);

INSERT INTO `USER`(`EMAIL`, `PASSWORD`, `NICKNAME`, `STATUS`, `REG_DT`)
VALUES ('Lionel Messi', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Messi', 1, NOW());
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (3, 1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (3, 2);

INSERT INTO `USER`(`EMAIL`, `PASSWORD`, `NICKNAME`, `STATUS`, `REG_DT`)
VALUES ('Cristiano Ronaldo', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Ronaldo', 1,NOW());
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (4, 1);

```

## JPA SQL

```sql
CREATE TABLE `ARTICLE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `P_ID` bigint(20) NOT NULL DEFAULT 0,
  `REFERENCE` bigint(20) NOT NULL,
  `STEP` int(11) NOT NULL,
  `LEVEL` int(11) NOT NULL,
  `AUTHOR` bigint(20) NOT NULL,
  `SUBJECT` varchar(200) NOT NULL,
  `STATUS` tinyint(3) NOT NULL,
  `HIT` bigint(20) DEFAULT 0,
  `RECOMMEND` bigint(20) DEFAULT 0,
  `DISAPPROVE` bigint(20) DEFAULT 0,
  `CONTENT_ID` bigint(20) DEFAULT NULL,
  `REG_DT` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `MOD_DT` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ARTICLE_ATTACHMENT` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ARTICLE_ID` bigint(20) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `PATH` varchar(200) NOT NULL,
  `SIZE` int(11) NOT NULL,
  `REG_DT` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `MOD_DT` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ARTICLE_COMMENT` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ARTICLE_ID` bigint(20) NOT NULL,
  `LEVEL` int(11) NOT NULL,
  `STEP` int(11) NOT NULL,
  `COMMENT` text NOT NULL,
  `AUTHOR` varchar(45) NOT NULL,
  `STATUS` tinyint(3) NOT NULL,
  `REG_DT` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `MOD_DT` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ARTICLE_CONTENT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTENTS` text DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `POST` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `WRITER_ID` bigint(20) NOT NULL,
  `SUBJECT` varchar(200) NOT NULL,
  `CONTENTS` text NOT NULL,
  `STATUS` tinyint(3) NOT NULL,
  `REG_DT` timestamp NULL DEFAULT NULL,
  `MOD_DT` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ROLE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` int(11) NOT NULL COMMENT 'ADMINISTRATOR(1, "최고관리자"),\nMANAGER(2, "관리자"),\nUSER(3, "사용자");',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `USER` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONNECTION_ID` bigint(20) DEFAULT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `PASSWORD` varchar(200) NOT NULL,
  `NICKNAME` varchar(45) NOT NULL,
  `STATUS` char(1) NOT NULL DEFAULT 'A',
  `REG_DT` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `MOD_DT` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `EMAIL_UNIQUE` (`EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `USER_CONNECTION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(50) NOT NULL,
  `PROVIDER` int(11) NOT NULL,
  `PROVIDER_ID` varchar(45) NOT NULL,
  `DISPLAY_NAME` varchar(45) NOT NULL,
  `PROFILE_URL` varchar(200) DEFAULT NULL,
  `IMAGE_URL` varchar(200) DEFAULT NULL,
  `ACCESS_TOKEN` varchar(200) NOT NULL,
  `EXPIRE_TIME` bigint(20) DEFAULT NULL,
  `REG_DT` timestamp NULL DEFAULT NULL,
  `MOD_DT` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `USER_ROLE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `WINNER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(45) NOT NULL,
  `WINNER` enum('WINNER','LOSER') NOT NULL,
  `REG_DT` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO ROLE (TYPE)
VALUES (1);
INSERT INTO ROLE (TYPE)
VALUES (2);
INSERT INTO ROLE (TYPE)
VALUES (3);

INSERT INTO `USER`(EMAIL, PASSWORD, NICKNAME, STATUS, REG_DT)
VALUES ('Genius Choi', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Genius', 1, NOW());
INSERT INTO `USER_CONNECTION`(EMAIL, PROVIDER, PROVIDER_ID, DISPLAY_NAME, PROFILE_URL, IMAGE_URL, ACCESS_TOKEN, EXPIRE_TIME)
VALUES ('Genius Choi', 1, 123456789, 'Genius', 'PROFILE', 'IMAGE', '1234567890', -1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (1, 1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (1, 2);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (1, 3);

INSERT INTO `USER`(`EMAIL`, `PASSWORD`, `NICKNAME`, `STATUS`, `REG_DT`)
VALUES ('Son Heung-min', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Son', 1, NOW());
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (2, 1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (2, 2);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (2, 3);

INSERT INTO `USER`(`EMAIL`, `PASSWORD`, `NICKNAME`, `STATUS`, `REG_DT`)
VALUES ('Lionel Messi', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Messi', 1, NOW());
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (3, 1);
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (3, 2);

INSERT INTO `USER`(`EMAIL`, `PASSWORD`, `NICKNAME`, `STATUS`, `REG_DT`)
VALUES ('Cristiano Ronaldo', '{bcrypt}$2a$10$7UEHLpn1r4gZY2qxiZFJ5.7wa3Hdz8IXgxUtFogy0Ac10fh7TG4V.', 'Ronaldo', 1,NOW());
INSERT INTO `USER_ROLE`(`USER_ID`, ROLE_ID)
VALUES (4, 1);
```
