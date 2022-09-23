-- 불법 주정차 데이터 베이스
CREATE
DATABASE illegal_parking;

-- 법정동코드
-- DROP TABLE law_dong;
CREATE TABLE law_dong
(
    DongSeq INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    Code    VARCHAR(10)  NOT NULL,              -- 법정동 코드
    Name    VARCHAR(100) NOT NULL,              -- 법정동 이름
    IsDel   BOOLEAN      NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- code index 처리 필요
-- CREATE INDEX dongCode ON law_dong (Code);

-- 공영주차장 정보
-- DROP TABLE parking;
CREATE TABLE parking
(
    ParkingSeq           INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    PrkplceNo            VARCHAR(20),                        -- 주차장관리번호
    PrkplceNm            VARCHAR(30),                        -- 주차장명
    PrkplceSe            VARCHAR(10),                        -- 주차장구분
    PrkplceType          VARCHAR(10),                        -- 주차장유형
    Rdnmadr              VARCHAR(50),                        -- 소재지도로명주소
    Lnmadr               VARCHAR(50),                        -- 소재지지번주소
    Prkcmprt             INT,                                -- 주차구획수
    FeedingSe            INT,                                -- 급지구분
    EnforceSe            VARCHAR(5),                         -- 부제시행구분
    OperDay              VARCHAR(10),                        -- 운영요일
    WeekdayOperOpenHhmm  VARCHAR(5),                         -- 평일운영시작시각
    WeekdayOperColseHhmm VARCHAR(6),                         -- 평일운영종료시각
    SatOperOpenHhmm      VARCHAR(7),                         -- 토요일운영시작시각
    SatOperCloseHhmm     VARCHAR(8),                         -- 토요일운영종료시각
    HolidayOperOpenHhmm  VARCHAR(9),                         -- 공휴일운영시작시각
    HolidayOperCloseHhmm VARCHAR(10),                        -- 공휴일운영종료시각
    ParkingchrgeInfo     VARCHAR(2),                         -- 요금정보
    BasicTime            VARCHAR(5),                         -- 주차기본시간
    BasicCharge          INT,                                -- 주차기본요금
    AddUnitTime          VARCHAR(5),                         -- 추가단위시간
    AddUnitCharge        INT,                                -- 추가단위요금
    DayCmmtktAdjTime     VARCHAR(5),                         -- 1일주차권요금적용시간
    DayCmmtkt            INT,                                -- 1일주차권요금
    MonthCmmtkt          INT,                                -- 월정기권요금
    Metpay               VARCHAR(10),                        -- 결제방법
    Spcmnt               VARCHAR(50),                        -- 특기사항
    InstitutionNm        VARCHAR(20),                        -- 관리기관명
    PhoneNumber          VARCHAR(13),                        -- 전화번호
    Latitude             DECIMAL(18, 10),                    -- 위도
    Longitude            DECIMAL(18, 10),                    -- 경도
    ReferenceDate        DATE,                               -- 데이터기준일자
    IsDel                BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    Code                 VARCHAR(10) NOT NULL                -- 법정동 코드 키
) ENGINE = InnoDB
  CHARSET = utf8;

-- 불법 주정차 구역
-- DROP TABLE illegal_zone;
CREATE TABLE illegal_zone
(
    ZoneSeq   BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name      VARCHAR(30) NULL,                   -- 불법 구역 이름
    Polygon   POLYGON     NOT NULL,               -- 불법 구역
    IsDel     BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    StartTime VARCHAR(5) NULL,                    -- 시작 시간
    EndTime   VARCHAR(5) NULL,                    -- 종료 시간
    Code      VARCHAR(10) NOT NULL,               -- 법정동 코드 키
    Illegaltype   INT         NOT NULL                -- 타입 키
) ENGINE = InnoDB
  CHARSET = utf8;

-- 불법 주정차 구역
-- DROP TABLE illegal_type;
CREATE TABLE illegal_type
(
    TypeSeq INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name    VARCHAR(30) NOT NULL,              -- 불법 구역 타입 이름 ( 예> 불법주청자, 5분주차, 탄력주차, 샘플주차 )
    IsDel   BOOLEAN     NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 회원 정보
-- DROP TABLE user;
CREATE TABLE user
(
    UserSeq  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name     VARCHAR(10) NOT NULL,               -- 회원 이름
    Email    VARCHAR(20) NOT NULL,               -- email (id)
    Password VARCHAR(50) NOT NULL,               -- 패스워드
    UserCode BIGINT      NOT NULL,               -- 사용자 고유 체번 ( 예> 기관 사람 / 일반 사용자 구분 )
    Role     INT         NOT NULL,               -- 역할 ( USER / ADMIN )
    IsDel    BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt    Datetime NULL                       -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 차량 번호 정보
CREATE TABLE car
(
    CarSeq  INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    CarNum  VARCHAR(10) NULL,               -- 차 번호
    UserSeq BIGINT  NOT NULL,               -- 사용자 키
    IsAlarm BOOLEAN NOT NULL DEFAULT FALSE, -- 알림 받기 여부
    DelDt   Datetime NULl,                  -- 삭제 일자
    IsDel   BOOLEAN NOT NULL DEFAULT FALSE  -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 신고 접수
CREATE TABLE report
(
    ReportSeq      INT AUTO_INCREMENT PRIMARY KEY,
    ZoneSeq        INT      NOT NULL,               -- 불법 구역
    RegDt          Datetime NOT NULL,               -- 신고 등록 일자
    ReceiptUserSeq INT      NOT NULL,               -- 사용자 ( 접수자 일반 사용자 )
    ReportUserSeq  INT NULL,                        -- 사용자 ( 기관 사용자 )
    IsReg          BOOLEAN NULL,                    -- 신고 접수 등록 여부 ( 정부 기관 사람 )
    CarNum         VARCHAR(10) NULL,                -- 차량 번호
    IsDel          BOOLEAN  NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt          Datetime NULL,                   -- 삭제 일자
    Note           VARCHAR(100)                     -- 비고
) ENGINE = InnoDB
  CHARSET = utf8;

-- 신고 등록
CREATE TABLE receipt
(
    ReceiptSeq INT AUTO_INCREMENT PRIMARY KEY,
    ZoneSeq    INT          NOT NULL,               -- 불법 구역
    RegDt      Datetime     NOT NULL,               -- 신고 등록 일자
    UserSeq    INT          NOT NULL,               -- 사용자 ( 일반 사용자 )
    CarNum     VARCHAR(10) NULL,                    -- 차량 번호
    IsDel      BOOLEAN      NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt      Datetime NULL,                       -- 삭제 일자
    FileName   VARCHAR(50)  NOT NULL,               -- 파일 이름
    Note       VARCHAR(100) NOT,                    -- 사유 또는 비고
    State      INT          NOT                     -- 현재 상태 ( 접수중, 접수 완료 )
) ENGINE = InnoDB
  CHARSET = utf8;

-- ---------------------------------------------------------------------------------------------------
