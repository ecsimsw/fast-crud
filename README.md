# fast-crud
매번 반복되는 Spring boot & JPA 의 기본적인 CRUD API 를 자동화 할 수 있지 않을까?

latest version : 1.0.1

## 기능들
- 어노테이션 표시로 엔티티의 Create, Read, Update, Delete API 가 생성된다.
- 자동 생성된 API의 Root path 를 설정할 수 있다.
- CRUD 중 추가할, 또는 제외할 기능을 선택할 수 있다.

## 미리보기
``` java
@CRUD(repositoryType = SampleRepository.class)
@Entity
class Sample {
}
```

Entity 의 @CRUD 를 읽어, 아래의 API가 자동 생성된다.

``` 
[POST] /sample
[GET] /sample 
[GET] /sample/{id}
[PUT] /sample
[DELETE] /sample/{id}
```

## 사용 방법

### build.gradle 

라이브러리 의존성을 추가한다.     
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ecsimsw:api-shutdown:1.0.1'
}
```

### @EnableCrud 추가

@EnableCrud 로 라이브러리 사용을 활성화한다.     

``` java
@EnableCrud
@SpringBootApplication
public class MyApplication {}
```

### @CRUD 표시

CRUD 를 적용할 Entity에 @CRUD와 엔티티 Repository type 를 표시한다.

``` java
@CRUD(repositoryType = SampleRepository.class)
@Enity
class Sample {
}
```

``` java
interface SampleRepository extends JpaRepository<Sample, Long> {
}
```

### API 명세

아래 CRUD api 가 자동 생성된다.      
생성과 수정의 Request body 에는 Entity 의 프로퍼티 키와 값이 포함된다.

#### 생성 
```
[POST] /sample 
{
   "name" : "ecsimsw"
}
```

#### 조회

```
[GET] /sample/{id}
```

#### 전체 조회
```
[GET] /sample
```

#### 수정

```
[PUT] /sample/{id} 
{
   "name" : "new_name"
}
```

#### 삭제 

```
[DELETE] /sample/{id}
```

### Root path 설정

기본 API 기본 경로는 Entity 이름이다. @CRUD 어노테이션의 rootPath 값으로 API 기본 경로를 변경할 수 있다.     
아래 예시의 경우 `/api/sample` 으로 API 경로가 생성된다.

``` java
@CRUD(rootPath = "api/sample")
@Entity
class Sample {}
```

### 제외 기능 설정 

CRUD 중 제외할 기능을 명시할 수 있다.      
아래 예시의 겨우 엔티티의 수정과 삭제 기능없이, 생성과 조회 API 만 자동 생성된다.

```
@CRUD(excludeType = {CrudType.DELETE, CrudType.UPDATE})
@Entity
class Sample {
```

## 변경 사항

### v1.0.0

Spring boot 2.6 이상부터 MVC 핸들러 매핑 경로 매칭 기본 전략이 AntPathMatcher -> PathPatternParser 로 수정되었다. RequestMappingHandlerMapping 에 추가되는 RequestMappingInfo 에 PathMatcher 를 설정하는 것으로, 기본 전략은 유지한 채 라이브러리에서 자동 생성하는 RequestMapping 정보만 AntPathMatcher를 따르도록 수정한다. 

``` java
public HandlerInfo(CrudRequestHandler handlerInstance, RequestMethod httpMethod, String requestPath) {
    this.handler = handlerInstance;
    var buildConfig = new RequestMappingInfo.BuilderConfiguration();
    buildConfig.setPathMatcher(new AntPathMatcher());
    buildConfig.setPatternParser(new PathPatternParser());
    this.requestMappingInfo = RequestMappingInfo
        .paths(requestPath)
        .methods(httpMethod)
        .options(buildConfig)
        .build();
}
```


