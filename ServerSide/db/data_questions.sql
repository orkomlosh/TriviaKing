-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: data
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questions` (
  `id` int(11) NOT NULL,
  `difficulty` int(11) DEFAULT NULL,
  `question` varchar(100) DEFAULT NULL,
  `answer1` varchar(45) DEFAULT NULL,
  `answer2` varchar(45) DEFAULT NULL,
  `answer3` varchar(45) DEFAULT NULL,
  `answer4` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,1,'כמה קלוריות יש בכפית סוכר לבן?','17','40','112','270'),(2,1,'האפיפיור בנדיקטוס ה-16 הוא ממוצא:','גרמני','פולני','איטלקי','צרפתי'),(3,1,'איזה בנק נמכר השנה?','בנק דיסקונט','בנק לאומי','בנק הפועלים','בנק איגוד'),(4,1,'מי זכה בטורניר ווימבלדון 2005 גברים?',' רוג\'ר פדרר',' קרלוס מויה',' אנדי רודיק',' אנדריי אגאסי'),(5,1,'התפילה האחרונה ביום כיפור:','נעילה','מוסף','ערבית','תפילה זכה'),(6,1,'מי היה הנשיא השני של ישראל?',' יצחק בן צבי',' זלמן שז\"ר',' משה שרת',' חיים וייצמן'),(7,2,'היכן נמצאים שרירי התאומים?','בשוק','בבטן','בירך','בכתפיים'),(8,2,'מי מהבאים לא היה רמטכ\"ל?','עזר ויצמן','יעקב דורי','דן שומרון',' יצחק רבין'),(9,2,'כמה פארקים של דיסני יש בעולם?','11',' 12','9','7'),(10,2,'אוגוסטו פינושה היה שליט:',' צ\'ילה','פרגוואי','ארגנטינה','בוליביה'),(11,2,'מי היה התובע במשפטו של אייכמן?',' גדעון האוזנר',' גבריאל בך',' צבי זמיר',' חיים כהן'),(12,3,'מקרן של איזו חיה מכינים שופר?','אייל','יעל','קרנף','צבי'),(13,3,'מי אמר \"באתי, ראיתי, ניצחתי\"?','יוליוס קיסר','נפוליאון','ריצ\'רד לב הארי','חניבעל'),(14,3,'מיהו השף העירום?','ג\'יימי אוליבר','ג\'יימי גיליס','שגב משה','פראן אדרייה'),(15,3,'כמה מדינות חברות באיחוד האירופי?','25','42','30','15'),(16,3,'איך אומרים בזיליקום בעברית?','ריחן','בזיל','בזיליקום','קורנית'),(17,3,'רפיק אל-חרירי היה:','ראש ממשלת לבנון','יו\"ר הפרלמנט הלבנוני ',' 	שר החוץ המצרי','איש עסקים סעודי'),(18,3,'השיר \"לבכות לך\" נכתב ל...?','חבר של אביב גפן','יצחק שמיר',' 	יצחק רבין ','אריק איינשטיין'),(19,3,'מנכ\"ל מיקרוסופט הוא:','סטיב כאלמר',' 	ביל גייטס','סטיב ג\'ובס','רופרט מרדוק'),(20,3,'המעבורת הראשונה ששוגרה לחלל השנה היתה:','דיסקברי','אלפא','קולומביה','אטלנטיס'),(21,3,'מהו אורכו של \"שביל ישראל\"?','960ק\"מ',' 	600ק\"מ ',' 	540ק\"מ','860ק\"מ'),(22,4,'כבש את השער מעורר המחלוקת מול גרמניה בגמר 1966?','ג\'ף הרסט',' 	גארי ליניקר','בובי צ\'רלטון','בובי מור'),(23,4,'ויקטור יושצ\'נקו נבחר לנשיא:','אוקראינה','גרוזיה','ליטא','רוסיה'),(24,4,'מהי בירת אקוודור?',' 	קיטו','סנטיאגו','אסונסיון','לימה'),(25,4,'מי מהבאים אינו סלט:','רוסיני','קיסר','וולדרוף','ניסואז'),(26,4,'איזה מסוק בחיל האוויר קיבל את הכינוי \"צפע\"?','קוברה','אפאצ\'י ',' בלק הוק ','יסעור');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-14 20:26:16
