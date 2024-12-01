# 메뉴 권한 필터

### 설명
메뉴 세팅법입니다.

- menu-auth-filter : 메뉴 필터를 끄고 킬수 있는 스위치 옵션입니다. 운영일때는   
  꼭 메뉴 필터를 켜주세요 
- cache : 매 요청마다 메뉴를 조회할 때 DB 접근 회수를 줄이기 위해 Caffeine 라이브러리를   
  사용해서 캐싱을 하도록 했습니다. 캐싱은 계속 true로 두면 되고 기본 사이즈로 1000을 했습니다.

~~~yaml
menu:
  # 운영에 배포 할 때에는 꼭 true 로 진행
  filter-setting:
    menu-auth-filter: true
  cache:
    enabled: true
    size: 1000
~~~


---
#### 캐시 강제 새로고침

DB에 직접적인 insert, update, delete 를 발생 시켜도 캐시는 바로 적용되지않아   
새로고침을 진행해주어야합니다.   
업데이트후 아래 주소로 업데이트를 진행해주세요.
어드민 JWT를 헤더에 담아서 요청해주셔야합니다.

url : "/admin/menu/cache/refresh"

```java
@GetMapping(value = "cache/refresh")
    public ResponseEntity<?> refreshCache() {
        int result = menuAuthorizationService.refreshCache();

        String message = result == 1 ? "메뉴 캐싱에 성공햇습니다." : "캐싱에 실패했습니다.";

        return ResponseUtil.createResponse(null, result, message, HttpStatus.OK);
    }

```