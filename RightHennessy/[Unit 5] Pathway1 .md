# [Unit 5] Pathway1

- Androoid Studio 에서 데이터 베이스
    
    View > Tool Windows > Inspection Results > Database Inspector
    

## 관계형 데이터베이스

데이터베이스 : 단순히 전자 형식으로 액세스하고 쓸 수 있는 구조화된 데이터 모음

데이터 지속성 :  인터넷과 같은 다른 소스에서 데이터를 검색하지 않고도 다음번에 앱을 열 때 액세스하도록 실행 중인 앱의 데이터를 저장하는 데 흔히 사용

관계형 데이터베이스 : 데이터를 테이블과 열, 행으로 구성하는 일반적인 데이터베이스 유형

기본 키 : 데이터 테이블의 행에 일종의 고유 식별자

SQL : 구조화된 쿼리 언어, 관계형 데이터베이스에서 데이터를 읽고 조작 가능한 언어

- SELECT : 데이터 테이블에서 특정 정보를 가져오고 결과를 다양한 방법으로 필터링 및 정렬

- INSERT : 테이블에 새 행을 추가

```sql
INSERT INTO table_name
VALUES (column1, column2, ...)
```

- UPDATE : 테이블의 기존 행을 업데이트

```sql
UPDATE table_name
SET column1 = ...,
column2 = ...,
...
WHERE column_name = ...
...
```

- DELETE : 테이블의 기존 행을 삭제

```sql
DELETE FROM table_name
WHERE <column_name> = ...
```

- WHERE : 조건을 추가, 필터링

- LIMIT :  반환되는 행 수에 제한

- DISTINCT : 쿼리 결과에서 중복값 제거

- ORDER BY : 쿼리 결과를 기준으로 정렬 가능 (ASC / DESC)

```kotlin
@Entity
data class Schedule(
   @PrimaryKey val id: Int,
   @NonNull @ColumnInfo(name = "stop_name") val stopName: String,
   @NonNull @ColumnInfo(name = "arrival_time") val arrivalTime: Int
)
```

@PrimaryKey 각 행을 고유하게 식별한는 고유키 

@ColumnInfo  새 열을 추가

DAO는 데이터 액세스 객체를 나타내며 데이터 액세스 권한을 제공하는 Kotlin 클래스

@Query 주석에 전달된 문자열로 지정