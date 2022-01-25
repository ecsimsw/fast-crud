# fast-crud
Automatic creation of simple CRUD API of Spring boot and JPA project.

<br>

## How to use

#### Step 1. Add the dependency.

<details>
<summary>Gradle (build.gradle)</summary>

 ``` groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

 ``` groovy
dependencies {
    implementation 'com.github.ecsimsw:fast-crud:0.0.2'
}
```
</details>

<details>
<summary>Maven (pom.xml)</summary>
 
``` xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
``` xml
<dependency>
    <groupId>com.github.ecsimsw</groupId>
    <artifactId>fast-crud</artifactId>
    <version>0.0.2</version>
</dependency>
```

</details>

<br>

#### Step 2. Put @CRUD on your entity class.

``` java
@CRUD
@Enity
class Sample {
}
```

<br>

#### Step 3. Declare JpaRepository with entity name.

``` java
public interface SampleRepository extends JpaRepository<Sample, Long> {}
```

<br>

#### Step 4. That's it. You just made basic CRUD http api bellow.

| |HttpMethod|Path|RequestBody (Json)|
|----|------|----|-----|
|save|POST|/{entityName}|O|
|findAll|GET|/{entityName}|X|
|findById|GET|/{entityName}/{id}|X|
|update|PUT|/{entityName}/{id}|O|
|delete|DELETE|/{entityName}/{id}|X|

<br>

#### Step 5. Example
``` java
@CRUD
@Entity
public class Sample {

    @GeneratedValue
    @Id
    private Long id;
    private String name;
    
    public Sample() {
    }
    
    public Sample(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

``` java
public interface SampleRepository extends JpaRepository<Sample, Long> {}
```

``` 
[POST] localhost:8080/sample 
{
    "name" : "ecsimsw"
}
[PUT] localhost:8080/sample/1 
{
    "name" : "new_name"
}
[GET] localhost:8080/sample 
[GET] localhost:8080/sample/1
[DELETE] localhost:8080/sample/1
```

<br>

## Additional features

#### Repository name

You can set your repository bean name in @CRUD with `repositoryBean` parameter.

``` java
@CRUD(repositoryBean = "anotherName")
```

``` java
public interface AnotherName extends JpaRepository<Sample, Long> {}
```

<br>

#### API root path

You can set your api `root path`  

``` java
@CRUD(rootPath = "anotherRoot")
```

```
/anotherRoot 
/anotherRoot/{id} 
```

<br>

#### Exclude method

Method can be excluded in @CRUD with `exclude` parameter.

``` java
@CRUD(exclude = {CrudMethod.UPDATE, CrudMethod.DELETE})
```
