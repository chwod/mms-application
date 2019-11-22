-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: localhost    Database: robot
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `robot`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `robot` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `robot`;

--
-- Table structure for table `configuration`
--

DROP TABLE IF EXISTS `configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuration` (
  `name` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `value` varchar(45) DEFAULT NULL,
  `val` bigint(20) DEFAULT NULL,
  `sequence` int(11) NOT NULL DEFAULT '0',
  `status` char(1) NOT NULL DEFAULT 'Y',
  `description` varchar(200) DEFAULT NULL,
  `create_Time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`title`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration`
--

LOCK TABLES `configuration` WRITE;
/*!40000 ALTER TABLE `configuration` DISABLE KEYS */;
INSERT INTO `configuration` VALUES ('ROBOT_SELF','BIRTHDAY_DAY',NULL,1,0,'Y','robot self info by birthday : day','2017-11-11 18:13:00'),('ROBOT_SELF','BIRTHDAY_MONTH',NULL,10,0,'Y','robot self info by birthday : month','2017-11-11 18:13:00'),('ROBOT_SELF','BIRTHDAY_YEAR',NULL,2017,0,'Y','robot self info by birthday : year','2017-11-11 18:12:59');
/*!40000 ALTER TABLE `configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `experience`
--

DROP TABLE IF EXISTS `experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `experience` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parse_word` varchar(50) NOT NULL,
  `property` varchar(20) NOT NULL,
  `content` varchar(100) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experience`
--

LOCK TABLES `experience` WRITE;
/*!40000 ALTER TABLE `experience` DISABLE KEYS */;
/*!40000 ALTER TABLE `experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `knowledge`
--

DROP TABLE IF EXISTS `knowledge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `knowledge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(32) NOT NULL,
  `event_id` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `property` varchar(20) NOT NULL,
  `content` varchar(50) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `knowledge_sessionid_eventid_index` (`session_id`,`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `knowledge`
--

LOCK TABLES `knowledge` WRITE;
/*!40000 ALTER TABLE `knowledge` DISABLE KEYS */;
INSERT INTO `knowledge` VALUES (1,'ACE5207A4229D87AEDD172AA2111D6D7','5e89d6d6-30f6-4415-9f30-a90fa7c359bd','北京','IS','首都','2019-11-19 14:03:50'),(2,'ACE5207A4229D87AEDD172AA2111D6D7','5e89d6d6-30f6-4415-9f30-a90fa7c359bd','首都','IS','北京','2019-11-19 14:03:50'),(3,'ACE5207A4229D87AEDD172AA2111D6D7','5e89d6d6-30f6-4415-9f30-a90fa7c359bd','北京','BELONG','中国','2019-11-19 14:03:50'),(4,'ACE5207A4229D87AEDD172AA2111D6D7','5e89d6d6-30f6-4415-9f30-a90fa7c359bd','首都','BELONG','中国','2019-11-19 14:03:50'),(5,'6C1F2741BAC26AC4AA11456272E8FDB8','934d4c24-3f2b-4003-bb5d-48dfd67711e1','国家','IS','中国','2019-11-20 14:56:00'),(6,'6C1F2741BAC26AC4AA11456272E8FDB8','934d4c24-3f2b-4003-bb5d-48dfd67711e1','中国','IS','国家','2019-11-20 14:56:00');
/*!40000 ALTER TABLE `knowledge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `talk_log`
--

DROP TABLE IF EXISTS `talk_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `talk_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(32) NOT NULL,
  `talk_word` varchar(60) NOT NULL,
  `parse_word` varchar(50) NOT NULL,
  `response_word` varchar(500) NOT NULL,
  `server_port` int(11) NOT NULL,
  `server_name` varchar(50) NOT NULL,
  `scheme` varchar(10) NOT NULL,
  `request_uri` varchar(100) NOT NULL,
  `query_string` varchar(200) DEFAULT NULL,
  `protocal` varchar(10) NOT NULL,
  `method` varchar(5) NOT NULL,
  `local_port` int(11) NOT NULL,
  `local_addr` varchar(20) NOT NULL,
  `content_length` int(11) NOT NULL,
  `content_type` varchar(50) NOT NULL,
  `response_code` int(11) DEFAULT NULL,
  `remote_addr` varchar(40) DEFAULT NULL,
  `remote_port` int(11) DEFAULT NULL,
  `remote_host` varchar(100) DEFAULT NULL,
  `remote_user` varchar(40) DEFAULT NULL,
  `remote_locale` varchar(30) DEFAULT NULL,
  `referer` varchar(400) DEFAULT NULL,
  `user_agent` varchar(200) DEFAULT NULL,
  `accept_language` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `talk_log_sessionid_index` (`session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `talk_log`
--

LOCK TABLES `talk_log` WRITE;
/*!40000 ALTER TABLE `talk_log` DISABLE KEYS */;
INSERT INTO `talk_log` VALUES (1,'ACE5207A4229D87AEDD172AA2111D6D7','你好','l','你好',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',20,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',52687,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-19 14:03:44'),(2,'ACE5207A4229D87AEDD172AA2111D6D7','北京是中国的首都','ns-v-ns-uj-d','OK',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',74,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',52687,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-19 14:03:50'),(3,'ACE5207A4229D87AEDD172AA2111D6D7','中国的首都是哪里？','ns-uj-d-v-r-x','null',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',83,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',52687,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-19 14:03:59'),(4,'ACE5207A4229D87AEDD172AA2111D6D7','中国的首都是哪里？','ns-uj-d-v-r-x','北京',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',83,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',52741,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-19 14:10:08'),(5,'ACE5207A4229D87AEDD172AA2111D6D7','知道北京是什么地方吗？','v-ns-v-r-n-y-x','知道,北京是首都，不过我不知道它是不是你说的地方？',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',101,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',52762,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-19 14:12:44'),(6,'ACE5207A4229D87AEDD172AA2111D6D7','是','v','好的，我知道了.',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',11,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',52778,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-19 14:15:49'),(7,'ACE5207A4229D87AEDD172AA2111D6D7','知道北京吗？','v-ns-y-x','知道,北京是首都',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',56,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',52778,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-19 14:16:00'),(8,'6C1F2741BAC26AC4AA11456272E8FDB8','你好','l','你好',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',20,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57842,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:55:43'),(9,'6C1F2741BAC26AC4AA11456272E8FDB8','知道北京吗？','v-ns-y-x','知道,北京是首都',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',56,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57842,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:55:52'),(10,'6C1F2741BAC26AC4AA11456272E8FDB8','哪个国家的首都？','r-n-uj-d-x','中国,它是国家吗?',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',74,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57842,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:55:57'),(11,'6C1F2741BAC26AC4AA11456272E8FDB8','是','v','好的，我知道了.',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',11,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57842,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:56:00'),(12,'6C1F2741BAC26AC4AA11456272E8FDB8','知道北京吗？','v-ns-y-x','知道,北京是首都',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',56,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57842,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:56:04'),(13,'6C1F2741BAC26AC4AA11456272E8FDB8','哪个国家的首都？','r-n-uj-d-x','中国',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',74,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57842,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:56:10'),(14,'6C1F2741BAC26AC4AA11456272E8FDB8','中国的首都是哪里？','ns-uj-d-v-r-x','北京',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',83,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57842,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:56:16'),(15,'6C1F2741BAC26AC4AA11456272E8FDB8','是','v','好的，我知道了.',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',11,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57873,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:57:52'),(16,'6C1F2741BAC26AC4AA11456272E8FDB8','是','v','好的，我知道了.',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',11,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',57873,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 14:58:17'),(17,'21025EB0E4DBE7E90711A08DF6859BA7','是','v','什么是啊？',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',11,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',58375,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 16:47:19'),(18,'21025EB0E4DBE7E90711A08DF6859BA7','北京市，简称“京”，','ns-x-v-x-ns-x-x','理解不了这句话，你能解释一下吗？',8080,'localhost','http','/',NULL,'HTTP/1.1','POST',8080,'0:0:0:0:0:0:0:1',92,'application/x-www-form-urlencoded; charset=UTF-8',200,'0:0:0:0:0:0:0:1',58456,'0:0:0:0:0:0:0:1',NULL,'中文 (中国)','http://localhost:8080/','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36','zh-CN,zh;q=0.9','2019-11-20 16:52:41');
/*!40000 ALTER TABLE `talk_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-23  2:15:06
