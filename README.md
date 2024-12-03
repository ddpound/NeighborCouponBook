# 우리동네 쿠폰북 PROJECT 
# (My neighbor coupon book)

## 문서 이동
- [규칙](docs/project-role.md)
- [테이블 구조](docs/table-structure-code.md)
- [코드 가이드 문서](docs/code-guide.md)
- [메뉴 필터 문서](docs/menu-filter.md)
- [초기 구동시 메뉴 자동등록 관련 문서](docs/menu-db-manage.md)
- [DB 시퀸스 모음](docs/db-sequences.md)

### 계획
1. google play store 정책 변경으로 2주간의 서비스 테스트를 반드시 진행해야함.
2. 12월 22일까지 개발을 마치고 서비스 준비 진행
3. 12월 23일 ~ 12월 28일 AWS 서버 구축 준비
4. 12월 28일 부터 1월 10일까지는 play store 테스트 진행, 통합 테스트 진행
5. 1월 15일 ~ 1월 17일 사이 정식 서비스 오픈
6. 이후에는 작은 홍보, 피드백 진행, 유지보수

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


### Swagger가 추가되었습니다.

uri : /swagger-ui/index.html#/

### Test 코드

시나리오를 작성하여 테스트 코드도 짜보려고 합니다. 참고해주세요
