# DB 시퀸스 모음

1. FILE GROUP ID 시퀸스
   2. 파일 그룹아이디를 위한 시퀸스 입니다.

~~~postgresql

CREATE SEQUENCE file_group_sequence
    INCREMENT BY 1    -- 증가값
    START WITH 1     -- 시작값
    NO MINVALUE     -- 최소값 제한 없음
    NO MAXVALUE     -- 최대값 제한 없음
    CACHE 1;        -- 메모리 캐시 크기
    
    
-- 시퀀스 목록 확인
SELECT * FROM pg_sequences;

~~~