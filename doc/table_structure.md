```application
CREATE TABLE `application` (
  `application_id` int(11) unsigned NOT NULL,
  `student_id` int(11) unsigned NOT NULL,
  `school_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`application_id`),
  KEY `school_id` (`school_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `application_ibfk_1` FOREIGN KEY (`school_id`) REFERENCES `school` (`school_id`) ON DELETE CASCADE,
  CONSTRAINT `application_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

```school
CREATE TABLE `school` (
  `school_id` int(11) unsigned NOT NULL,
  `school_name` varchar(128) NOT NULL DEFAULT '',
  `capacity` int(11) NOT NULL,
  `school_group` char(1) NOT NULL DEFAULT '',
  `weight` float NOT NULL,
  `applicant` int(11) NOT NULL,
  PRIMARY KEY (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

```student
CREATE TABLE `student` (
  `student_id` int(11) unsigned NOT NULL,
  `student_name` varchar(20) NOT NULL DEFAULT '',
  `csat_score` int(11) NOT NULL,
  `school_records_score` int(11) NOT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```