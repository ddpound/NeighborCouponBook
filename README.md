# 우리동네 쿠폰북 PROJECT 
# (My neighbor coupon book)

## 문서 이동
- [테이블 구조](docs/table-structure-code.md)
- [코드 가이드 문서](docs/code-guide.md)

### 설명
1. 초단기 미니프로젝트
2. 목적은 최대한 빠르게 제품을 설계, 개발(구현), 출시 가 목적

### 사양

1. BackEnd : Spring Boot version '3.4.0-M1'
2. FrontEnd : React-naitive
3. DB : postgresql
 
### 인프라 및 구조

1. ~~집에 있는 서버에서 도커로 올릴 예정~~
2. aws ec2 사용
3. 도커 사용


### 규칙

1. 모든 데이터를 저장할 때는 model만을 사용함 (저장 전 이동까지는 모두 VO)
2. 저장외의 데이터 이동은 모두 VO를 사용 (VO - model)


#### menu 에 관한 규칙
1. 모든 메뉴들은 상위의 부모 메뉴의 권한 규칙을 따른다.
2. 메뉴 테이블의 따른 규칙이 없을경우 부모의 규칙을 따라가며
메뉴 자체의 규칙이 따로 db에 등록이 되어있다면 독립적으로 동작한다.
기본 원칙은 부모를 따라간다.
3. 계층을 구분하는 것은 / 가 된다.
