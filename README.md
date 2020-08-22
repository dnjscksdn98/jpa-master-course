## 🚀 JPA Entity Relationships

### ✔ 연관관계 설정 시 주의 사항
- 데이터베이스 내의 데이터 중복을 방지하기 위해 한쪽 엔티티만을 연관관계의 주인으로 설정
- -ToOne 연관 관계의 기본 ```FetchType```은 ```Eager``` 이므로 염두해 두기

### ✔ @OneToOne

- 기본적으로 ```FetchType``` 은 ```Eager```로 설정되어 있음
- 연관관계의 주인을 설정할 때, 주인이 아닌 쪽에 ```mappedBy = {변수명}```을 설정해주면 됨

**Student 클래스**

```java
@OneToOne(fetch = FetchType.LAZY)
private Passport passport;
```

**Passport 클래스**

```java
@OneToOne(fetch = FetchType.LAZY, mappedBy = "passport")
private Student student;
```

### ✔ @ManyToOne, @OneToMany

- 기본적으로 ```@ManyToOne```의 ```FetchType``` 은 ```Eager```로 설정되어 있음
- 기본적으로 ```@OneToMany```의 ```FetchType``` 은 ```Lazy```로 설정되어 있음
- 연관관계의 주인은 ```@ManyToOne``` 쪽에 설정, 즉 ```mappedBy = {변수명}```는 ```@OneToMany``` 쪽에 설정

**Review 클래스**

```java
@ManyToOne(fetch = FetchType.LAZY)
private Course course;
```

**Course 클래스**

```java
@OneToMany(mappedBy = "course")
private List<Review> reviews = new ArrayList<>();
```

### ✔ @ManyToMany

- 기본적으로 ```@ManyToMany```의 ```FetchType``` 은 ```Lazy```로 설정되어 있음
- 기본적으로 두 엔티티의 주키를 가지고 두개의 ```JoinTable```을 생성해줌
(COURSE_STUDENTS, STUDENT_COURSES)
- 그러나 이것은 좋지 않은 데이터베이스 설계이므로 둘 중에 한쪽을 연관관계의 주인으로 설정해줌으로써 문제 해결 가능
- ```@JoinTable```은 연관관계의 주인 쪽에 설정
- ```joinColumns```는 주인 쪽의 주키 설정
- ```inverseJoinColumns```는 주인이 아닌 쪽의 주키 설정

**Student 클래스(연관관계 주인)**

```java
@ManyToMany
@JoinTable(
    name = "STUDENT_COURSE",
    joinColumns = @JoinColumn(name = "STUDENT_ID"),
    inverseJoinColumns = @JoinColumn(name = "COURSE_ID")
)
private List<Course> courses = new ArrayList<>();
```

**Course 클래스**

```java
@ManyToMany(mappedBy = "courses")
private List<Student> students = new ArrayList<>();
```

## 🚀 JPA Inheritance Hierarchies and Mappings

### ✔ SINGLE_TABLE

**부모 클래스**

```java
@Entity
@Getter
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EMPLOYEE_TYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Employee[%s]", this.name);
    }
}
```

**자식 클래스**

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartTimeEmployee extends Employee {

    private BigDecimal hourlyWage;

    @Builder
    public PartTimeEmployee(String name, BigDecimal hourlyWage) {
        super(name);
        this.hourlyWage = hourlyWage;
    }
}
```

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FullTimeEmployee extends Employee {

    private BigDecimal salary;

    @Builder
    public FullTimeEmployee(String name, BigDecimal salary) {
        super(name);
        this.salary = salary;
    }
}
```

- ```@Inheritance```의 ```strategy```의 기본값은 ```SINGLE_TABLE``` 입니다.
- ```SINGLE_TABLE```은 상속 관계에 있는 테이블들을 모두 합쳐서 하나의 테이블로 저장합니다. 즉, 부모 테이블만 생성됩니다.
- 기본적으로 ```DTYPE``` 이라는 새로운 칼럼이 생성되고, 이 칼럼은 어떠한 자식 클래스인지 구분해줍니다. 해당 칼럼명은 ```@DiscriminatorColumn``` 어노테이션으로 재정의 할 수 있습니다.
- 데이터를 조회할 때 쿼리는 매우 단순하므로 성능면에서는 좋으나, 테이블이 생성될 때 ```null``` 값이 생성되므로 데이터베이스 설계면에서는 비효율적입니다.

### ✔ TABLE_PER_CLASS

**부모 클래스**

```java
@Entity
@Getter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Employee {...}
```

- 상속 관계에 있는 클래스에 대해 모두 개별적인 테이블을 생성해줍니다.
- 데이터를 조회할 때, 즉 부모 클래스를 조회할 때 ```UNION```을 사용하고 성능도 준수한 편입니다.
- 그러나 자식 클래스가 부모 클래스의 모든 칼럼을 상속 받으므로 공통 칼럼에 대한 중복 현상이 발생합니다.

### ✔ JOINED

```java
@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Employee {...}
```

- 부모 클래스의 테이블은 자신의 칼럼만을 가지고, 자식 클래스의 테이블은 부모 클래스의 ID를 참조하는 외래키랑 자신의 칼럼만을 가집니다.
- 그래서 데이터베이스의 설계면에서는 가장 효율이 좋습니다.
- 부모 클래스를 조회할 경우, 각각의 자식 클래스에 대해 ```JOIN``` 연산을 수행하므로 성능면에서는 떨어집니다.

### ✔ Mapped Super Class

- 부모 클래스

```java
@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Employee {...}
```

- ```@MappedSuperClass```를 사용하면 해당 클래스를 엔티티로 매핑할 수 없으므로 ```@Entity``` 어노테이션을 사용할 수 없습니다.
- 이로 인해서 부모 클래스로 데이터를 조회할 수 없게 됩니다.
- 데이터베이스 안에서는 자식 클래스의 테이블만 생성되고 ```TABLE_PER_CLASS```와 같은 형태로 생성됩니다.

### ✔ How to choose?

데이터베이스 디자인과 무결성을 고려한다면 ```JOINED``` 방식을 추천합니다.

그러나 성능을 더 고려한다면 ```SINGLE_TABLE```을 추천합니다.

반면에 ```TABLE_PER_CLASS```하고 ```Mapped Super Class```를 사용하게 된다면 각 테이블마다 중복되는 칼럼이 생성되므로 비추천합니다.

## 🚀 트랜잭션이란?

> 데이터베이스의 상태를 변환시키는 하나의 논리적인 작업 단위를 구성하는 연산들의 집합입니다.

예를 들어 A 계좌에서 B 계좌로 일정 금액을 이체한다고 가정합시다.

1. A 계좌의 잔액을 확인한다.
2. A 계좌의 금액에서 이체할 금액을 빼고 다시 저장한다.
3. B 계좌의 잔액을 확인한다.
4. B 계좌의 금액에서 이체할 금액을 더하고 다시 저장한다.

이러한 과정들이 모두 합쳐져서 "계좌 이체" 라는 하나의 작업 단위를 구성하게 됩니다.

### ✔ 하나의 트랜잭션은 Commit 되거나 Rollback 됩니다

**Commit 연산**

하나의 논리적 단위(트랜잭션)에 대한 작업이 성공적으로 끝나 데이터베이스가 다시 일관된 상태에 있을 때, 이 트랜잭션이 행한 갱신 연산이 완료된 것을 트랜잭션 관리자에게 알려주는 연산입니다.

**Rollback 연산**

하나의 트랜잭션 처리가 비정상적으로 종료되어 데이터베이스의 일관성을 깨뜨렸을 때, 이 트랜잭션의 일부가 정상적으로 처리되었더라도 트랜잭션의 원자성을 구현하기 위해 이 트랜잭션이 행한 모든 연산을 취소(Undo)하는 연산입니다.

Rollback 시에는 해당 트랜잭션을 재시작하거나 폐기합니다.

### ✔ 트랜잭션의 성질(ACID)

**원자성(Atomicity), All or Nothing**

트랜잭션의 모든 연산들은 정상적으로 수행 완료되거나 아니면 전혀 어떠한 연산도 수행되지 않은 상태를 보장해야 합니다.

**일관성(Consistency)**

트랜잭션 완료 후에도 데이터베이스가 일관된 상태로 유지되어야 합니다.

**독립성(Isolation)**

하나의 트랜잭션이 실행하는 도중에 변경한 데이터는 이 트랜잭션이 완료될 때까지 다른 트랜잭션이 참조하지 못합니다.

**지속성(Durability)**

성공적으로 수행된 트랜잭션은 영원히 반영되어야 합니다.

### ✔ 트랜잭션 격리 수준

**격리 수준(Isolation Level)이란?**

> 트랜잭션에서 일관성이 없는 데이터를 허용하도록 하는 수준

**격리 수준의 필요성**

데이터베이스는 ACID 속성에 따라 트랜잭션이 원자적이면서도 독립적인 수행을 하도록 합니다. 그래서 ```Locking``` 이라는 개념이 등장하게 됐습니다. ```Locking```은 하나의 트랜잭션이 데이터베이스를 다루는 동안 다른 트랜잭션이 관여하지 못하게 막는 것입니다. 하지만 무조건적인 ```Locking``` 으로 인해 동시에 수행되는 많은 트랜잭션들을 순서대로 처리하는 방식으로 구현되면 데이터베이스의 성능은 떨어지게 됩니다. 반대로 응답성을 높이기 위해 ```Locking``` 범위를 줄인다면 잘못된 값이 처리 될 여지가 있습니다. 그래서 최대한 효율적인 ```Locking``` 방법이 필요합니다.

### ✔ 트랜잭션 격리 수준의 종류

**Read Uncommitted(Level 0)**

- ```SELECT``` 문장이 수행되는 동안 해당 데이터에 ```Shared Lock``` 이 걸리지 않는 레벨
- 트랜잭션에 처리중인 혹은 아직 커밋되지 않은 데이터를 다른 트랜잭션이 읽는 것을 허용합니다.
- 따라서 어떤 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 아직 완료되지 않은(Uncommited 또는 Dirty) 트랜잭션이지만 변경된 데이터인 B를 읽을 수 있습니다.
- 그래서 데이터베이스의 일관성을 유지할 수 없습니다.

**Read Committed(Level 1)**

- ```SELECT``` 문장이 수행되는 동안 해당 데이터에 ```Shared Lock```이 걸리는 레벨
- 트랜잭션이 수행되는 동안 다른 트랜잭션이 접근할 수 없어 대기하게 됩니다.
- 즉, Commit이 이루어진 트랜잭션만 조회할 수 있습니다.
- 따라서 어떤 사용자가 A 라는 데이터를 B 라는 데이터로 변경하는 동안 다른 사용자는 해당 데이터에 접근할 수 없습니다.

**Repeatable Read(Level 2)**

- 트랜잭션이 완료될 때까지 ```SELECT``` 문장이 사용하는 모든 데이터에 ```Shared Lock``` 이 걸리는 레벨
- 트랜잭션이 범위 내에서 조회한 데이터의 내용이 항상 동일함을 보장합니다.
- 따라서 다른 트랜잭션이 그 영역에 해당되는 데이터가 Commit 이 되기 전에 수정이 불가능합니다.

**Serializable(Level 3)**

- 트랜잭션이 완료될 때까지 ```SELECT``` 문장이 사용하는 모든 데이터에 ```Shared Lock``` 이 걸리는 레벨
- 완벽한 읽기 일관성 모드를 제공합니다.
- 따라서 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정 및 입력이 불가능합니다.

### ✔ 낮은 단계의 격리 수준 이용시 발생하는 현상

<a><img src="https://github.com/WeareSoft/tech-interview/raw/master/contents/images/isolation-level.png"></a>

**Dirty Read**

- Commit 되지 않은 수정 중인 데이터를 다른 트랜잭션에서 읽을 수 있도록 허용할 때 발생하는 현상입니다.
- 어떤 트랜잭션에서 아직 실행이 끝나지 않은 다른 트랜잭션에 의한 변경 사항을 보게 되는 현상입니다.

**Non-Repeatable Read**

- 한 트랜잭션에서 같은 쿼리를 두 번 수행할 때 그 사이에 다른 트랜잭션이 값을 수정 또는 삭제함으로써 두 쿼리의 결과가 상이하게 나타나는 비 일관성 현상입니다.

- **예시**

1. 트랜잭션 1 - ```SELECT * FROM person WHERE id = 10;```
2. 트랜잭션 2 - ```UPDATE person SET age = 30 WHERE id = 10;```
3. 트랜잭션 1 - ```SELECT * FROM person WHERE id = 10;```

**Phantom Read**

- 한 트랜잭션 안에서 일정 범위의 레코드를 두 번 이상 읽을 때, 첫 번째 쿼리에서 없던 레코드가 두 번째 쿼리에서 나타나는 현상입니다.
- 이는 트랜잭션 도중 새로운 레코드가 삽입되는 것을 허용했기 때문에 나타납니다.

- **예시**

1. 트랜잭션 1 - ```SELECT * FROM person WHERE age BETWEEN 5 AND 55;```
2. 트랜잭션 2 - ```INSERT INTO person VALUES(13, 'Alex', 25);```
3. 트랜잭션 1 - ```SELECT * FROM person WHERE age BETWEEN 5 AND 55;```

### ✔ How to choose?

```Serializable``` 을 선택하게 되면 문제가 되는 현상들은 발생하지 않지만 성능이 굉장히 떨어집니다. 왜냐하면 병렬적으로 트랜잭션이 실행되더라도 모두 Commit 이 될 때까지 기다려야하므로 사실상 직렬적으로 실행된다고 볼 수가 있습니다. 

대부분의 애플리케이션에서는 ```Read Committed``` 을 사용합니다. Locking 적절히 사용하므로 성능면에서도 준수한 편이고 데이터베이스의 일관성에 대해서도 준수한 편입니다.

### ✔ Spring @Transactional vs JPA @Transactional

만약 하나의 트랜잭션에서 하나의 데이터베이스나 MQ를 사용하게 되면 JPA의 ```@Transactional```을 사용해도 무방합니다. 그러나 하나의 트랜잭션에서 하나 이상의 데이터베이스나 MQ를 사용해서 수정이나 읽기 연산 등을 수행하게 되면 Spring의 ```@Transactional``` 을 사용해주어야 합니다.

**격리 수준 설정**

```@Transactional(isolation = Isolation.READ_COMMITTED)```

또는

- ```READ_UNCOMMITTED``` = 1
- ```READ_COMMITTED``` = 2
- ```REPEATABLE_READ``` = 4
- ```SERIALIZABLE``` = 8

```spring.jpa.properties.hibernate.connection.isolation=2```

## 🚀 1차 캐시와 2차 캐시

<a><img src="https://i.ibb.co/S6vYyW7/jpa-cache-flowchart.png" width="700"></a>

영속성 컨텍스트(Persistence Context)의 내부에는 엔티티를 보관하는 저장소가 있는데 이것을 ```1차 캐시(First Level Cache)```라고 부릅니다. 1차 캐시는 트랜잭션이 시작하고 종료할 때까지만 유효합니다. 즉, 트랜잭션 단위의 캐시입니다. 따라서 애플리케이션 전체로 보면 데이터베이스 접근 횟수를 획기적으로 줄이지는 못합니다.

JPA 구현체들은 애플리케이션 단위의 캐시를 지원하는데 이것을 ```공유 캐시``` 또는 ```2차 캐시(Second Level Cache)``` 라고 부릅니다. 2차 캐시를 적용하면 애플리케이션 조회 성능을 향상할 수 있습니다.

### ✔ 1차 캐시

1차 캐시는 영속성 컨텍스트 내부에 있습니다. 엔티티 매니저로 조회하거나 변경하는 모든 엔티티는 1차 캐시에 저장됩니다. 트랜잭션을 Commit 하거나 Flush 를 하게 되면 1차 캐시에 있는 엔티티의 변경 사항들을 데이터베이스에 반영합니다.

JPA를 스프링 프레임워크 같은 컨테이너 위에서 실행하면 트랜잭션을 시작할 때 영속성 컨텍스트를 생성하고 트랜잭션을 종료할 때 영속성 컨텍스트도 종료합니다.

1차 캐시는 활성화하거나 비활성화할 수 있는 옵션이 아니고 영속성 컨텍스트 자체가 사실상 1차 캐시입니다.

### ✔ 2차 캐시

2차 캐시는 애플리케이션 단위의 캐시입니다. 따라서 애플리케이션을 종료할 때까지 캐시가 유지됩니다. 2차 캐시를 사용하면 엔티티 매니저를 통해 데이터를 조회할 때 우선 2차 캐시에서 찾고 없으면 데이터베이스에서 찾게 됩니다. 그래서 2차 캐시를 적절히 활용하면 데이터베이스 조회 횟수를 획기적으로 줄일 수 있습니다.

2차 캐시는 동시성을 극대화하려고 캐시한 객체를 직접 반환하지 않고 복사본을 만들어서 반환합니다. 만약 캐시한 객체를 그대로 반환하면 여러 곳에서 같은 객체를 동시에 수정하는 문제가 발생할 수 있는데 이 문제를 해결하려면 객체에 락을 걸어야 하는데 이렇게 하면 동시성이 떨어질 수 있습니다. 락에 비하면 객체를 복사하는 비용은 아주 저렴하므로 2차 캐시는 복사본을 반환하게 됩니다.

### ✔ Hibernate & EhCache

Hibernate 가 지원하는 캐시는 크게 3가지가 있습니다.

**1. 엔티티 캐시**
	
 - 엔티티 단위로 캐시합니다. 식별자(ID)로 엔티티를 조회하거나 컬렉션이 아닌 연관 관계에 있는 엔티티를 조회할 때 사용합니다.
    
**2. 컬렉션 캐시**

- 엔티티와 연관된 컬렉션을 캐시합니다. 컬렉션이 엔티티를 담고 있으면 식별자 값만 캐시합니다.
    
**3. 쿼리 캐시**

- 쿼리와 파라미터 정보를 키로 사용해서 캐시합니다. 결과가 엔티티면 식별자 값만 캐시합니다.
    
### ✔ 환경설정하기

**의존성 설정**

```
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-ehcache</artifactId>
</dependency>
```

**프로퍼티 설정**

>```spring.jpa.properties.hibernate.cache.use_second_level_cache = true```
> 💨 2차 캐시 활성화합니다.
> ```spring.jpa.properties.hibernate.cache.region.factory_class```
> 💨 2차 캐시를 처리할 클래스를 지정합니다.
> ```spring.jpa.properties.hibernate.generate_statistics = true```
> 💨 하이버네이트가 여러 통계정보를 출력하게 해주는데 캐시 적용 여부를 확인할 수 있습니다.
> - L2C puts: 2차 캐시에 데이터 추가
> - L2C hits: 2차 캐시의 데이터 사용
> - L2C miss: 2차 캐시에 해당 데이터 조회 실패 -> 데이터베이스 접근

```yml
spring:
  jpa:
    properties:
      hibernate:
        generate_statistics: true
        format_sql: true

        cache:
          use_second_level_cache: true
          region:
            factory_class: org.hibernate.cache.ehcache.EhCacheRegionFactory

      javax:
        persistence:
          sharedCache:
            mode: ENABLE_SELECTIVE
            
logging:
  level:
    net:
      sf:
        ehcache: debug
```

**엔티티 캐시와 컬렉션 캐시**

> ```@Cacheable```
> 💨 엔티티 캐시 적용시 사용하는 어노테이션
>
> ```@Cache```
> 💨 하이버네이트 전용입니다. 캐시와 관련된 더 세밀한 설정을 할 때 사용합니다. 또한 컬렉션 캐시를 적용할 때에도 사용합니다.

```java
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class Course {

    @Id 
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "course")
    private List<Review> reviews = new ArrayList<>();
    ...
}
```

### ✔ @Cache

**CacheConcurrencyStrategy 속성**

> ```READ_ONLY```
> 💨 자주 조회하고 수정 작업을 하지 않는 데이터에 적합합니다.
>
> ```READ_WRITE```
> 💨 조회 및 수정 작업을 하는 데이터에 적합합니다. Phantom Read 가 발생할 수 있으므로 SERIALIZABLE 격리 수준에서는 사용할 수 없습니다. 
>
> ```NONSTRICT_READ_WRITE```
> 💨 거의 수정 작업을 하지 않는 데이터에 적합합니다.