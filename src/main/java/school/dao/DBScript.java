package school.dao;

public class DBScript {
    public static String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS COURSE(\n" +
            "    NAME VARCHAR(128),\n" +
            "    CREDIT INT,\n" +
            "    PRIMARY KEY (NAME)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS SEMESTER(\n" +
            "    NAME VARCHAR(128),\n" +
            "    PRIMARY KEY (NAME)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS STUDENT(\n" +
            "    ID VARCHAR(128),\n" +
            "    NAME VARCHAR(128),\n" +
            "    PRIMARY KEY (ID)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS ENROLLMENT(\n" +
            "    STUDENT_ID VARCHAR(128),\n" +
            "    SEMESTER VARCHAR(128),\n" +
            "    COURSE VARCHAR(128),\n" +
            "    PRIMARY KEY (STUDENT_ID,SEMESTER,COURSE)\n" +
            ");";
}
