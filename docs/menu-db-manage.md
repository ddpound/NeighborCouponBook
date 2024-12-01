# 메뉴 관리 가이드
=============

### 설명
엔드포인트 코드 작성시 지켜야할 가이드 문서 입니다.  
<label style="color: red">이 문서는 시스템 초기 세팅 할 때 관련되어있습니다.</label>  
<label style="color: red">시스템 운영중에는 Swagger인 Operation을 제외하면 사용하는 일이 없습니다. </label>  

설정은 apllication.yml의 JPA DDLAUTO가 CREATE 일 때만  
작동하도록 세팅 해두었습니다.



### CODE CASE 1
가장 기본적인 케이스라고 할 수 있습니다.   

Swagger의 @Operation 어노테이션만 신경 써서 작성해주세요.   

@MenuInformation의 모든 옵션들은 기본적인 세팅(default option)이 되어서 따로 옵션을 넣어줄 필요없습니다.

- @Operation은 Swagger의 기본 어노테이션입니다.
- @MenuInformation 어노테이션을 등록해주어야 자동으로 메뉴가 등록됩니다.
- swagger의 summary가 컬럼 user_name으로 저장됩니다.
- swagger의 description은 컬럼 remarks에 저장됩니다.

~~~java
   @Operation(
            summary = "내 유저 정보 데이터 가져오기",
            description = "내 유저 정보와 권한 정보를 모두 가져옵니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = CouponUserWithUserRole.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러"
                    )
            }
    )
    @MenuInformation(
        menuAuthDetail = {
                @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
        })
    @GetMapping(value = "get/my-data")
    public ResponseEntity<?> getUserAllData(CouponUserSearch search) {

        Long nowLoginId = Objects.requireNonNull(AuthUtil.getLoginUserData()).getUserId();

        if(nowLoginId != null) {
            search.setUserId(nowLoginId);

            List<CouponUserWithUserRole> selectUserWithUserRoleList =
                    couponUserService.selectCouponUserQueryJoinUserRole(search);

            if(selectUserWithUserRoleList != null && !selectUserWithUserRoleList.isEmpty()) {
                return ResponseUtil.createResponse(
                        selectUserWithUserRoleList,
                        1,
                        "리스트 반환 완료",
                        HttpStatus.OK
                );
            }
        }

        return ResponseUtil.createSuccessResponse(-1, "데이터가 없습니다");
    }
~~~

<hr/>

### @MenuInformation

- @MenuInformation 어노테이션을 등록하면 시스템 구동시 자동으로 DB에 등록됩니다. (해당 어노테이션이 없으면 DB에 등록되지 않습니다.)
- @MenuInformation 자체가 menu 테이블에 저장
- @MenuInformation.MenuRoleDetail은 해당 메뉴의 관련된 유저 권한을 넣어주는 설정입니다.
- 그냥 모든 컨트롤러 위에 해당 어노테이션만 복사 붙여넣기 해주셔도 됩니다.

~~~java
@MenuInformation(
        menuAuthDetail = {
                @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
        })
~~~

<br/>
<br/>


엔드포인트 등록은 하지만 DB에 저장하고 싶지 않는다면 이렇게 세팅하시면 됩니다.
~~~java
 @MenuInformation(
            menuDirectDBSave = false,
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
    })
~~~

<hr/>

### MenuAuth 코드
아래 코드를 통해 기본옵션이 어떤 것인지 확인 해주세요.
~~~java
/**
 * 메뉴 정보를 저장하기 위한 커스텀 어노테이션
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MenuInformation {

    // 전체 데이터 직접 저장 할지 선택 옵션
    boolean menuDirectDBSave() default true;

    // REST, PAGE 인지 구분해주는 요소, 보통은 REST 타입
    Menu.MenuType menuType() default Menu.MenuType.REST;
    MenuRoleDetail[] menuAuthDetail() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @interface MenuRoleDetail {
        String rolesName() default "";
        long roleId();                        // 권한 ID
        boolean read()     default true;
        boolean write()    default true;
        boolean upload()   default true;
        boolean download() default true;

        // 바로 저장할지 선택해주는 요소
        boolean menuRoleDirectDBSave() default true;
    }
}
~~~

---

### 주석

1. 초기 세팅만 한 이유.
    - 나중에 권한을 추가하게 된다면 모든 @MenuInfomation에 다 접근해서 코드를 추가해야   
      하기 때문에 초기 디비 세팅에만 편하게 등록하고 관리할수 있도록 했습니다.
