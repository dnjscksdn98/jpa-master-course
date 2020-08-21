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