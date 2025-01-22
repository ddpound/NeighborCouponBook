site : https://dbdiagram.io/
```
Table follows {
  following_user_id integer
  followed_user_id integer
  created_at timestamp 
}

Table coupon_user {
  user_id       integer [primary key]
  user_login_id varchar
  username      varchar
  password      varchar
  phone_number  varchar // 01011112222, - 없이
  email         varchar
  thumbnail     varchar // 썸네일
  
  // 아래는 공통 컬럼
  create_date timestamp
  update_date timestamp
  create_user integer
  update_user integer
  is_deleted  bool
  remarks      varchar
  db_remarks   varchar
}

Table posts {
  post_id integer [primary key]
  title varchar
  body text [note: 'Content of the post']
  user_id integer
  status varchar
  
  // 아래는 공통 컬럼
  create_date timestamp
  update_date timestamp
  create_user integer
  update_user integer
  is_deleted  bool
  remarks      varchar
  db_remarks   varchar
}

Table shop{
  shop_id integer [primary key]
  shop_address varchar
  business_registration_number varchar // 사업자 등록번호
  shop_description varchar // 샵 설명
  shop_owner_id integer // 샵 사장
  shop_owner_name varchar
  shop_type_id integer
  shop_thumbnail integer

  // 아래는 공통 컬럼
  create_date timestamp
  update_date timestamp
  create_user integer
  update_user integer
  is_deleted  bool
  remarks      varchar
  db_remarks   varchar
}

Table shop_type{
  shop_type_id integer [primary key]
  type_name varchar

  // 아래는 공통 컬럼
  create_date timestamp
  update_date timestamp
  create_user integer
  update_user integer
  is_deleted  bool
  remarks      varchar
  db_remarks   varchar
}



Table coupon{
  coupon_id integer [primary key]
  shop_id integer
  goals_number integer // 쿠폰 최대 숫자
  coupon_description varchar // 쿠폰 설명

  // 아래는 공통 컬럼
  create_date timestamp
  update_date timestamp
  create_user integer
  update_user integer
  is_deleted  bool
  remarks      varchar
  db_remarks   varchar
}

Table coupon_object{
  object_id integer [primary key]
  coupon_id integer

  x_coordinate integer
  y_coordinate integer
  state        integer // 속성, 이미지, 쿠폰이미지, 도장레이어 등등
  file_id integer

  // 아래는 공통 컬럼
  create_date timestamp
  update_date timestamp
  create_user integer
  update_user integer
  is_deleted  bool
  remarks      varchar
  db_remarks   varchar
}

Table coupon_history{
  history_id integer [primary key]
  owner_coupon_user integer // 쿠폰 도장 소유자
  coupon_id integer // 해당 쿠폰아이디
  stamp_count integer // 도장 찍은 횟수

  // 아래는 공통 컬럼
  create_date timestamp
  update_date timestamp
  create_user integer
  update_user integer
  is_deleted  bool
  remarks      varchar
  db_remarks   varchar
}

Table coupon_book_file{
  file_id integer [primary key]
  file_group_id integer
  file_sers_no integer
  physical_file_path varchar // 저장된 경로
  physical_file_name varchar // 물리적으로 저장되어있는 name , UUID
  original_file_name varchar // 저장했었던 이름.
}

Table board {
  board_id integer
  board_type varchar
  board_title varchar
  board_content varchar
}

Ref: posts.user_id > coupon_user.user_id // many-to-one

Ref: coupon_user.user_id < follows.following_user_id

Ref: coupon_user.user_id < follows.followed_user_id

Ref: shop.shop_owner_id > coupon_user.user_id
Ref: shop.shop_thumbnail - coupon_book_file.file_id

Ref: coupon.shop_id > shop.shop_id

Ref: coupon_object.coupon_id > coupon.coupon_id

Ref: coupon_book_file.file_id - coupon_object.file_id

Ref: coupon_user.thumbnail - coupon_book_file.file_id

// coupon history area
Ref: coupon_history.owner_coupon_user > coupon_user.user_id
Ref: coupon_history.coupon_id > coupon.coupon_id


//shop_type area
Ref: shop.shop_type_id > shop_type.shop_type_id


```