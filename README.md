# Library Management System

This project is configured without Maven `pom.xml`. All required runtime dependencies are provided as local JAR files in the `lib` folder and included manually via the classpath.

## Folder Structure

- `lib/` — downloaded JDBC and logging dependencies
- `src/main/java/` — Java source files
- `src/main/resources/` — configuration and SQL scripts
- `out/` — compiled classes
- `run.bat` — Windows launcher script

## Required JARs

Place these files in `lib/`:

- `mysql-connector-j-8.4.0.jar`
- `jbcrypt-0.4.jar`
- `slf4j-api-2.0.16.jar`
- `logback-classic-1.5.8.jar`
- `logback-core-1.5.8.jar`

## Database Setup

1. Create MySQL database `library_management`.
2. Update `src/main/resources/config.properties` with your local DB username/password.
3. Run the SQL file:

```sql
SOURCE src/main/resources/sql/library_schema.sql;
```

## Compile (manual classpath)

```powershell
javac -cp "lib\mysql-connector-j-8.4.0.jar;lib\jbcrypt-0.4.jar;lib\slf4j-api-2.0.16.jar;lib\logback-classic-1.5.8.jar;lib\logback-core-1.5.8.jar" -d out $(Get-ChildItem -Path src\main\java -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

## Run

```powershell
java -cp "lib\mysql-connector-j-8.4.0.jar;lib\jbcrypt-0.4.jar;lib\slf4j-api-2.0.16.jar;lib\logback-classic-1.5.8.jar;lib\logback-core-1.5.8.jar;out" com.library.Main
```

Or simply run:

```powershell
run.bat
```
