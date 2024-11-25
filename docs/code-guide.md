# code guide
#### 설명 : 공통 코드 및 코드 사용법, 규칙을 적어둡니다.



### 각 항목 별 페이징 처리
```java

private final MenuRepository menuRepository;

private final JPAQueryFactory queryFactory;

public JPAQuery<Menu> selectMenuListQuery(MenuSearch menuSearch) {
    QMenu menu = QMenu.menu;

    JPAQuery<Menu> returnJpaQuery = queryFactory
            .select(menu)
            .from(menu)
            .where(settingMenuSearchBuilder(menuSearch))
            .offset(menuSearch.getOffset())
            .limit(menuSearch.getPageSize());

    // 정렬 조건 추가
    returnJpaQuery = applySorting(returnJpaQuery, menuSearch, menu);

    return returnJpaQuery;
}

private JPAQuery<Menu> applySorting(JPAQuery<Menu> query, MenuSearch menuSearch, QMenu menu) {
    if (menuSearch.getSort() == null || menuSearch.getSortOrder() == null) {
        return query; // 정렬 조건이 없으면 그대로 반환
    }

    OrderSpecifier<?> orderSpecifier = getOrderSpecifier(menuSearch, menu);
    if (orderSpecifier != null) {
        query = query.orderBy(orderSpecifier);
    }

    return query;
}

private OrderSpecifier<?> getOrderSpecifier(MenuSearch menuSearch, QMenu menu) {
    return switch (menuSearch.getSort()) {
        case menuId -> menuSearch.getSortOrder().equals(CommonSearch.orderBy.asc)
                ? menu.menuId.asc() : menu.menuId.desc();
        case menuName -> menuSearch.getSortOrder().equals(CommonSearch.orderBy.asc)
                ? menu.menuName.asc() : menu.menuName.desc();
        case menuUri -> menuSearch.getSortOrder().equals(CommonSearch.orderBy.asc)
                ? menu.menuUri.asc() : menu.menuUri.desc();
        default -> null; // 예외 처리 가능
    };
}
```

프론트 유저들은 asc, desc 옵션과, 어떤 것을 정렬할지 선택할 수 있습니다.



